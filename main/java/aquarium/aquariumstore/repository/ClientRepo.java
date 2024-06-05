package aquarium.aquariumstore.repository;

import aquarium.aquariumstore.model.Customer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

// Клас ClientRepo є репозиторієм для взаємодії з базою даних,
// зокрема для роботи з таблицею customer.
// Він використовує JdbcTemplate для виконання SQL-запитів.

@Getter
@Repository
public class ClientRepo {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Customer.
    RowMapper<Customer> mapperClient = (rs, rowNum) -> {
        Customer customer = new Customer();
        customer.setID(rs.getInt(1));
        customer.setFirstName(rs.getString(2));
        customer.setLastName(rs.getString(3));
        customer.setPhone(rs.getString(4));
        customer.setEmail(rs.getString(5));
        return customer;
    };

    // Виконує SQL запит до БД для додавання нового клієнта і
    // повертає згенерований унікальний ID цього клієнта.
    public int addNewCustomer(Customer customer) {
        String SQL = "INSERT INTO customer(first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL, new String[]{"customer_id"});
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    //Виконує SQL запит до БД для пошуку усіх клієнтів та
    // повертає список клієнтів.
    public List<Customer> findAll(){
        String SQL = """
                SELECT * FROM customer
                ORDER BY customer_id DESC
                """;
        return jdbcTemplate.query(SQL, mapperClient);
    }

    // Виконує SQL-запит для видалення покупця
    public void deleteCustomer(int customerID){
        String SQL = """
                BEGIN;
                                
                DELETE FROM address
                WHERE customer_id = ?;
                                
                DELETE FROM customer
                WHERE customer_id = ?;
                               
                COMMIT;
                """;
        jdbcTemplate.update(SQL, customerID, customerID);
    }
}
