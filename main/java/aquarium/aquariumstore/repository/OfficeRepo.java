package aquarium.aquariumstore.repository;

import aquarium.aquariumstore.model.AboutOrder;

import aquarium.aquariumstore.model.Employee;
import aquarium.aquariumstore.model.OrderLineInfo;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.List;


// Клас OfficeRepo є репозиторієм для взаємодії з базою даних.
// Він використовує JdbcTemplate для виконання SQL-запитів.

@Getter
@Repository
public class OfficeRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу AboutOrder.
    RowMapper<AboutOrder> mapperOrderInfo = (rs, rowNum) -> {
        AboutOrder aboutOrder = new AboutOrder();
        aboutOrder.setOrderId(rs.getInt(1));
        aboutOrder.setOrderStatus(rs.getString(2));
        aboutOrder.setCustomer(rs.getString(3));
        aboutOrder.setPhone(rs.getString(4));
        aboutOrder.setCustomerEmail(rs.getString(5));
        aboutOrder.setPostalCode(rs.getString(6));
        aboutOrder.setCountry(rs.getString(7));
        aboutOrder.setCity(rs.getString(8));
        aboutOrder.setStreet(rs.getString(9));
        aboutOrder.setBuilding(rs.getString(10));
        aboutOrder.setDetails(rs.getString(11));
        aboutOrder.setPaymentStatus(rs.getString(12));
        aboutOrder.setPaymentMethod(rs.getString(13));
        Integer employeeId = rs.getObject(14, Integer.class);
        aboutOrder.setEmployee(employeeId != null ? employeeId : 0);
        aboutOrder.setTotal(rs.getDouble(15));
        aboutOrder.setOrderDate(rs.getDate(16));
        return aboutOrder;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Employee.
    RowMapper<Employee> mapperEmployee = (rs, rowNum) -> {
        Employee employee = new Employee();
        employee.setID(rs.getInt(1));
        employee.setFirstName(rs.getString(2));
        employee.setLastName(rs.getString(3));
        employee.setTitle(rs.getString(4));
        employee.setPhone(rs.getString(5));
        employee.setEmail(rs.getString(6));
        employee.setCountry(rs.getString(7));
        employee.setCity(rs.getString(8));
        employee.setHireDate(rs.getDate(9));
        employee.setBirthDate(rs.getDate(10));
        return employee;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу OrderLineInfo.
    RowMapper<OrderLineInfo> infoOrderLine = (rs, rowNum) -> {
        OrderLineInfo orderLine = new OrderLineInfo();
        orderLine.setOrderLineId(rs.getInt(1));
        orderLine.setOrderID(rs.getInt(2));
        orderLine.setProductID(rs.getInt(3));
        orderLine.setProductName(rs.getString(4));
        orderLine.setProductPrice(rs.getDouble(5));
        orderLine.setQuantity(rs.getInt(6));
        return orderLine;
    };


    // Виконує SQL запит до БД для пошуку інформації про усі замовлення та
    // повертає список замовлень.
    public List<AboutOrder> findAllOrdersInfo(){
        String SQL = """
                SELECT "order".order_id, "order".status, customer.first_name || ' ' || customer.last_name,
                customer.phone, customer.email, address.postal_code, address.country, address.city,
                address.street, address.building, address.details, payment.status, payment.method, "order".employee_id,
                "order".total, "order".order_date
                FROM "order"
                JOIN customer ON customer.customer_id = "order".customer_id
                JOIN address ON customer.customer_id = address.customer_id
                JOIN payment ON "order".order_id = payment.order_id
                ORDER BY "order".order_id DESC""";
        return jdbcTemplate.query(SQL, mapperOrderInfo);
    }

    // Виконує SQL запит до БД для пошуку усіх менеджерів з продажу (пошук всіх співробітників, які мають у своєму
    // посадовому званні слово "Sales Representative") та
    // повертає список таких співробітників.
    public List<Employee> findAllSalesRepresentative(){
        String SQL = """
                SELECT * FROM employee
                WHERE title LIKE '%Sales%Representative%'
                ORDER BY employee_id ASC\s
                """;
        return jdbcTemplate.query(SQL, mapperEmployee);
    }


    // Виконує SQL-запит для отримання інформації про всі елементи замовлення (OrderLineInfo)
    // за конкретним ідентифікатором замовлення (orderId).
    public List<OrderLineInfo> findLines(int orderId){
        String SQL = """
                SELECT order_line.order_line_id, order_line.order_id, order_line.product_id,\s
                product.name, product.price, order_line.quantity\s
                FROM public.order_line
                JOIN product ON product.product_id = order_line.product_id
                WHERE order_line.order_id = ?
                """;
        return jdbcTemplate.query(SQL, infoOrderLine, orderId);
    }

    // Виконує SQL-запит для зміни статусу замовлення
    public void setOrderStatus(int orderId, String status, int employeeId){
        String SQL = """
                UPDATE "order"
                SET status = ?, employee_id = ?
                WHERE order_id = ?
                """;
        jdbcTemplate.update(SQL, status, employeeId, orderId);
    }


    // Виконує SQL-запит для зміни статусу оплати замовлення
    public void setPaymentStatus(int orderId, String status){
        String SQL = """
                UPDATE payment
                SET status = ?
                WHERE order_id = ?
                """;
        jdbcTemplate.update(SQL, status, orderId);
    }

    // Виконує SQL-запит для видалення замовлення
    public void deleteOrder(int orderId){
        String SQL = """
                BEGIN;

                DELETE FROM order_line
                WHERE order_id = ?;

                DELETE FROM payment
                WHERE order_id = ?;

                DELETE FROM "order"
                WHERE order_id = ?;

                COMMIT;
                """;
        jdbcTemplate.update(SQL, orderId, orderId, orderId);
    }


    // Виконує SQL-запит для знаходження всіх співробітників
    public List<Employee> findAllEmployees(){
        String SQL = """
                SELECT * FROM employee
                ORDER BY employee_id ASC
                """;
        return jdbcTemplate.query(SQL, mapperEmployee);
    }

    // Виконує SQL-запит для оновлення інформації про співробітника
    public void updateEmployee(int employeeId, String firstName, String lastName,
                               String title, String phone, String email, String country,
                               String city, String hireDate, String birthDate) {
        String SQL = """
                UPDATE employee
                SET first_name = ?,
                    last_name = ?,
                    title = ?,
                    phone = ?,
                    email = ?,
                    country = ?,
                    city = ?,
                    hire_date = ?,
                    birth_date = ?
                WHERE employee_id = ?;
                """;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date hireDateSql = new Date(sdf.parse(hireDate).getTime());
            Date birthDateSql = new Date(sdf.parse(birthDate).getTime());
            jdbcTemplate.update(SQL, firstName, lastName, title, phone, email, country, city, hireDateSql, birthDateSql, employeeId);
        } catch (ParseException ignored) {
        }
    }

    // Виконує SQL-запит для додавання нового співробітника
    public void addNewEmployee(String firstName, String lastName,
                               String title, String phone, String email, String country,
                               String city, String hireDate, String birthDate) {
        String SQL = """

                INSERT INTO employee(first_name, last_name, title, phone, email, country, city, hire_date, birth_date)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date hireDateSql = new Date(sdf.parse(hireDate).getTime());
            Date birthDateSql = new Date(sdf.parse(birthDate).getTime());
            jdbcTemplate.update(SQL, firstName, lastName, title, phone, email, country, city, hireDateSql, birthDateSql);
        } catch (ParseException ignored) {
        }
    }
}
