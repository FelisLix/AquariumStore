package aquarium.aquariumstore.repository;


import aquarium.aquariumstore.model.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Objects;


// Клас OrderRepo є репозиторієм для взаємодії з базою даних, зокрема для обробки замовлень.
// Він використовує JdbcTemplate для виконання SQL-запитів.

@Getter
@Repository
public class OrderRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Order.
    RowMapper<Order> mapperOrder = (rs, rowNum) -> {
        Order order = new Order();
        order.setID(rs.getInt(1));
        order.setStatus(rs.getString(2));
        order.setCustomerID(rs.getInt(3));
        order.setEmployeeID(rs.getInt(4));
        order.setTotal(rs.getDouble(5));
        order.setOrderDate(rs.getDate(6));
        return order;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Address.
    RowMapper<Address> mapperAddress = (rs, rowNum) -> {
        Address address = new Address();
        address.setID(rs.getInt(1));
        address.setPostalCode(rs.getString(2));
        address.setCity(rs.getString(3));
        address.setStreet(rs.getString(4));
        address.setBuilding(rs.getString(5));
        address.setDetails(rs.getString(6));
        address.setCustomerID(rs.getInt(7));
        address.setCountry(rs.getString(8));
        return address;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Payment.
    RowMapper<Payment> mapperPayment = (rs, rowNum) -> {
        Payment payment = new Payment();
        payment.setID(rs.getInt(1));
        payment.setStatus(rs.getString(2));
        payment.setMethod(rs.getString(3));
        payment.setOrderID(rs.getInt(4));
        return payment;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Order_line.
    RowMapper<Order_line> mapperOrderLine = (rs, rowNum) -> {
        Order_line order = new Order_line();
        order.setID(rs.getInt(1));
        order.setOrderID(rs.getInt(2));
        order.setProductID(rs.getInt(3));
        order.setQuantity(rs.getInt(4));
        return order;
    };

    // Виконує SQL запит до БД для додавання нового замовлення та
    // повертає згенерований унікальний ID цього замовлення.
    public int addNewOrder(int customerId, double total, Date orderDate) {
        String SQL = "INSERT INTO \"order\"(status, customer_id, total, order_date) VALUES('В обробці', ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL, new String[]{"order_id"});
            ps.setInt(1, customerId);
            ps.setDouble(2, total);
            ps.setDate(3, orderDate);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    // Виконує SQL запит до БД для додавання нової адреси покупця
    public void addNewAddress(Address address) {
        String SQL = "INSERT INTO address(postal_code, city, street, building, details, customer_id, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(SQL, address.getPostalCode(), address.getCity(),
                address.getStreet(), address.getBuilding(), address.getDetails(),
                address.getCustomerID(), address.getCountry());
    }

    // Виконує SQL запит до БД для додавання нової оплати замовлення
    public void addNewPayment(Payment payment) {
        String SQL = "INSERT INTO payment(status, method, order_id) VALUES ('Не оплачено', ?, ?)";
        jdbcTemplate.update(SQL, payment.getMethod(), payment.getOrderID());
    }

    // Виконує SQL запит до БД для додавання нового елемента замовлення
    public void addNewOrderLine(int orderId, Cart.CartItem cartItem) {
        String SQl = "INSERT INTO order_line(order_id, product_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(SQl, orderId, cartItem.getProduct().getID(), cartItem.getQuantity());
    }

    // Виконує SQL запит до БД для додавання усіх товарів з кошика до БД
    public void addAllOrderLines(int orderId, Cart cart) {
        for (int i = 0; i < cart.showCart().size(); i++) {
            addNewOrderLine(orderId, cart.showCart().get(i));
        }
    }
}
