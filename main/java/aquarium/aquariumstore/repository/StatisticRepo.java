package aquarium.aquariumstore.repository;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

// Клас OrderRepo є репозиторієм для взаємодії з базою даних, зокрема для обробки запитів пов’язаних із статистикою.
// Він використовує JdbcTemplate для виконання SQL-запитів.

@Getter
@Repository
public class StatisticRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Клас для отримання інформації у форматі Double+String
    @Getter
    @Setter
    @Component
    public class ChartData {
        private double value;
        private String name;
    }

    // Клас для отримання інформації у форматі Int+String
    @Getter
    @Setter
    @Component
    public class ChartDataInt {
        private int value;
        private String name;
    }

    // Клас для отримання інформації у форматі Double
    @Getter
    @Setter
    @Component
    public class Value {
        private double value;
    }

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу ChartData.
    RowMapper<ChartData> mapper = (rs, rowNum) -> {
        ChartData chartData = new ChartData();
        chartData.setValue(rs.getInt(1));
        chartData.setName(rs.getString(2));
        return chartData;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу ChartDataInt.
    RowMapper<ChartDataInt> mapperInt = (rs, rowNum) -> {
        ChartDataInt chartData = new ChartDataInt();
        chartData.setValue(rs.getInt(1));
        chartData.setName(rs.getString(2));
        return chartData;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Value.
    RowMapper<Value> mapperOne = (rs, rowNum) -> {
       Value value = new Value();
        value.setValue(rs.getDouble(1));
        return value;
    };


    // Виконує SQL запит до БД для знаходження топ 10 товарів які продавалися найбільше за весь час
    public List<ChartDataInt> getTopProducts(){
        String SQL = """
                SELECT subquery.number, product.name
                FROM (
                	SELECT COUNT(order_line.order_id) AS "number", order_line.product_id
                	FROM order_line
                	GROUP BY order_line.product_id
                ) AS subquery
                JOIN product ON subquery.product_id = product.product_id
                JOIN type ON product.type_id = type.type_id
                JOIN category ON type.category_id = category.category_id
                ORDER BY subquery."number" DESC
                LIMIT 10
                """;
        return jdbcTemplate.query(SQL, mapperInt);
    }

    // Виконує SQL запит до БД для знаходження топ 5 міст у які найбільше відправлялися замовлення за весь час
    public List<ChartDataInt> getTopCity() {
        String SQL = """
                SELECT COUNT(city), city
                FROM address
                GROUP BY city
                ORDER BY COUNT(city) DESC
                LIMIT 5;
                """;
        return jdbcTemplate.query(SQL, mapperInt);
    }

    // Виконує SQL запит до БД для знаходження топ 10 підкатегорій, товари яких продавалися найбільше за весь час
    public List<ChartDataInt> getTopType() {
        String SQL = """
                SELECT subquery.number, type.name
                FROM ( SELECT COUNT(order_line.order_id) AS number, type.type_id
                FROM order_line
                JOIN product ON order_line.product_id = product.product_id
                JOIN type ON product.type_id = type.type_id
                GROUP BY type.type_id
                ) AS subquery
                JOIN type ON subquery.type_id = type.type_id
                ORDER BY subquery."number" DESC
                LIMIT 10;""";
        return jdbcTemplate.query(SQL, mapperInt);
    }

    // Виконує SQL запит до БД для знаходження прибутку за кожен місяць
    public List<ChartData> getTopMonthsProfit() {
        String SQL = """
                SELECT SUM(total) AS monthly_total, TO_CHAR(order_date, 'YYYY-MM') AS month
                FROM "order"
                GROUP BY TO_CHAR(order_date, 'YYYY-MM');
                """;
        return jdbcTemplate.query(SQL, mapper);
    }

    // Виконує SQL запит до БД для знаходження кількості замовлень за кожен місяць
    public List<ChartData> getTopMonthsOrders() {
        String SQL = """
                SELECT COUNT(total) AS monthly_total, TO_CHAR(order_date, 'YYYY-MM') AS month
                FROM "order"
                GROUP BY TO_CHAR(order_date, 'YYYY-MM');
                """;
        return jdbcTemplate.query(SQL, mapper);
    }

    // Виконує SQL запит до БД для знаходження прибутку за поточний місяць
    public List<Value> getThisMonthProfit(){
        String SQL = """
                SELECT SUM(total) AS monthly_total
                FROM "order"
                WHERE EXTRACT(YEAR FROM order_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                AND EXTRACT(MONTH FROM order_date) = EXTRACT(MONTH FROM CURRENT_DATE);
                """;
        return jdbcTemplate.query(SQL, mapperOne);
    }

    // Виконує SQL запит до БД для знаходження прибутку за поточний місяць
    public List<Value> getThisMonthOrders(){
        String SQL = """
                SELECT COUNT(total) AS monthly_total
                FROM "order"
                WHERE EXTRACT(YEAR FROM order_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                AND EXTRACT(MONTH FROM order_date) = EXTRACT(MONTH FROM CURRENT_DATE);
                """;
        return jdbcTemplate.query(SQL, mapperOne);
    }

    // Виконує SQL запит до БД для знаходження кількості проданих товарів за поточний місяць
    public List<Value> getThisMonthProducts(){
        String SQL = """
                SELECT SUM(order_line.quantity) AS monthly_total
                FROM "order"
                JOIN order_line ON "order".order_id = order_line.order_id
                WHERE EXTRACT(YEAR FROM order_date) = EXTRACT(YEAR FROM CURRENT_DATE)
                AND EXTRACT(MONTH FROM order_date) = EXTRACT(MONTH FROM CURRENT_DATE);
                """;
        return jdbcTemplate.query(SQL, mapperOne);
    }
}
