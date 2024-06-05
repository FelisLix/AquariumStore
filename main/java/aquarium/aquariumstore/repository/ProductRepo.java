package aquarium.aquariumstore.repository;

import aquarium.aquariumstore.model.Category;
import aquarium.aquariumstore.model.Image;
import aquarium.aquariumstore.model.Product;
import aquarium.aquariumstore.model.Type;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

// Клас ProductRepo є репозиторієм для взаємодії з базою даних, зокрема для обробки товарів.
// Він використовує JdbcTemplate для виконання SQL-запитів.

@Getter
@Repository
public class ProductRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Product.
    RowMapper<Product> mapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setID(rs.getInt(1));
        product.setName(rs.getString(2));
        product.setTypeID(rs.getInt(3));
        product.setType(rs.getString(4));
        product.setPrice(rs.getDouble(5));
        product.setQuantity(rs.getInt(6));
        product.setSize(rs.getString(7));
        product.setDescription(rs.getString(8));
        product.setCreatedAt(rs.getTimestamp(9));
        product.setImageID(rs.getInt(10));
        product.setImageURL(rs.getString(11));
        product.setLifespan(rs.getInt(12));
        product.setOrigin(rs.getString(13));
        return product;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Type.
    RowMapper<Type> mapperType = (rs, rowNum) -> {
        Type type = new Type();
        type.setID(rs.getInt(1));
        type.setName(rs.getString(2));
        type.setCategoryID(rs.getInt(3));
        return type;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Category.
    RowMapper<Category> mapperCategory = (rs, rowNum) -> {
        Category category = new Category();
        category.setID(rs.getInt(1));
        category.setName(rs.getString(2));
        return category;
    };

    // Використовується для перетворення рядків результатів SQL-запиту на об'єкти класу Image.
    RowMapper<Image> mapperImage = (rs, rowNum) -> {
        Image image = new Image();
        image.setID(rs.getInt(1));
        image.setURL(rs.getString(2));
        return image;
    };

    // Виконує SQL запит до БД для знаходження усіх категорій
    public List<Category> findAllCategories(){
        String SQL = """
                SELECT * FROM category
                ORDER BY category_id
                """;
        return jdbcTemplate.query(SQL, mapperCategory);
    }

    // Виконує SQL запит до БД для знаходження усіх підкатегорій
    public List<Type> findAllTypes(){
        String SQL = "SELECT * FROM type";
        return jdbcTemplate.query(SQL, mapperType);
    }

    // Виконує SQL запит до БД для знаходження усіх медіафайлів
    public List<Image> findAllImages(){
        String SQL ="SELECT * FROM image";
        return  jdbcTemplate.query(SQL, mapperImage);
    }

    // Виконує SQL запит до БД для додавання нових медіафайлів
    public void addImage(String url){
        String SQL = "INSERT INTO image(url) VALUES(?)";
        jdbcTemplate.update(SQL, url);
    }

    // Виконує SQL запит до БД для видалення медіафайлів
    public void deleteImage(int id){
        String SQL = "DELETE FROM image WHERE image_id = ?";
        jdbcTemplate.update(SQL, id);
    }

    // Виконує SQL запит до БД для знаходження товарів
    public List<Product> findAll(String sortField, String sortDir) {
        String SQL = "SELECT product.product_id, product.name, product.type_id, type.name, product.price,\n" +
                "product.quantity, product.size, product.description, product.created_at, product.image_id, \n" +
                "image.url, product.lifespan, product.origin\n" +
                "FROM product\n" +
                "JOIN type ON product.type_id = type.type_id\n" +
                "JOIN image ON product.image_id = image.image_id\n" +
                "ORDER BY product." + sortField + " " + sortDir;
        return jdbcTemplate.query(SQL, mapper);
    }

    // Виконує SQL запит до БД для знаходження товару за ID
    public Product getProductByID(int ID) {
        String SQL = """
                SELECT product.product_id, product.name, product.type_id, type.name, product.price,
                product.quantity, product.size, product.description, product.created_at, product.image_id,
                image.url, product.lifespan, product.origin
                FROM product
                JOIN type ON product.type_id = type.type_id
                JOIN image ON product.image_id = image.image_id
                WHERE product_id = ?""";
        return this.jdbcTemplate.queryForObject(SQL, mapper, ID);
    }

    // Виконує SQL запит до БД для знаходження товарів за певною підкатегорією
    public List<Product> getProductsByType(int ID, String sortField, String sortDir) {
        String SQL = "SELECT product.product_id, product.name, product.type_id, type.name, product.price, " +
                "product.quantity, product.size, product.description, product.created_at, product.image_id, " +
                "image.url, product.lifespan, product.origin " +
                "FROM product " +
                "JOIN type ON product.type_id = type.type_id " +
                "JOIN image ON product.image_id = image.image_id " +
                "WHERE type.type_id = ? " +
                "ORDER BY product." + sortField + " " + sortDir;
        return this.jdbcTemplate.query(SQL, mapper, ID);
    }

    // Виконує SQL запит до БД для знаходження товарів за певною категорією
    public List<Product> getProductsByCategory(int ID, String sortField, String sortDir) {
        String SQL = "SELECT product.product_id, product.name, product.type_id, type.name, product.price," +
                "product.quantity, product.size, product.description, product.created_at, product.image_id," +
                "image.url, product.lifespan, product.origin " +
                "FROM product " +
                "JOIN type ON product.type_id = type.type_id " +
                "JOIN image ON product.image_id = image.image_id " +
                "JOIN category ON type.category_id = category.category_id " +
                "WHERE category.category_id = ? " +
                "ORDER BY product." + sortField + " " + sortDir;
        return this.jdbcTemplate.query(SQL, mapper, ID);
    }

    // Виконує SQL запит до БД для знаходження всіх товарів
    public List<Product> findAllProducts() {
        String SQL = """
                SELECT product.product_id, product.name, product.type_id, type.name, product.price,
                product.quantity, product.size, product.description, product.created_at, product.image_id,
                image.url, product.lifespan, product.origin
                FROM product
                JOIN type ON product.type_id = type.type_id
                JOIN image ON product.image_id = image.image_id
                ORDER BY product.product_id ASC
                """;
        return jdbcTemplate.query(SQL, mapper);
    }

    // Виконує SQL запит до БД для оновлення даних про товар
    public void updateProduct(int productId, String name, double price, int quantity,
                              String size, String description, String imageURL,
                              int lifespan, String origin, int imageID) {
        String SQL = """
                BEGIN;
                
                UPDATE image
                SET url = ?
                WHERE image_id = ?;
                
                UPDATE product
                SET name = ?,
                    price = ?,
                    quantity = ?,
                    size = ?,
                    description = ?,
                    lifespan = ?,
                    origin = ?
                WHERE product_id = ?;
                
                COMMIT;
                """;

        jdbcTemplate.update(SQL,imageURL, imageID, name, price, quantity, size, description, lifespan, origin, productId);
    }

    // Виконує SQL запит до БД для видалення товару за ID
    public void deleteProduct(int id) {
        String SQL = """
                DELETE FROM product
                WHERE product_id = ?
                """;
        jdbcTemplate.update(SQL, id);

    }

    // Виконує SQL запит до БД для пошуку товару за назвою
    public List<Product> findProductByName(String name) {
        String SQL = "SELECT product.product_id, product.name, product.type_id, type.name, product.price," +
                "product.quantity, product.size, product.description, product.created_at, product.image_id," +
                "image.url, product.lifespan, product.origin " +
                "FROM product " +
                "JOIN type ON product.type_id = type.type_id " +
                "JOIN image ON product.image_id = image.image_id " +
                "JOIN category ON type.category_id = category.category_id " +
                "WHERE product.name LIKE ?";

        return jdbcTemplate.query(SQL, new Object[]{"%" + name + "%"}, mapper);
    }

    // Виконує SQL запит до БД для додавання нового товару
    public void addNewProduct(String name, int type, double price, int quantity, String size, String description,
                              Timestamp createdAt, int imageId, int lifespan, String origin){
        String SQL = """
                INSERT INTO product(name, type_id, price, quantity, size, description, created_at, image_id, lifespan, origin)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(SQL, name, type, price, quantity, size, description, createdAt, imageId, lifespan, origin);
    }

    // Виконує SQL запит до БД для додавання нової категорії
    public void addNewCategory(String title){
        String SQL = """
                INSERT INTO category(name) VALUES (?);
                """;
        jdbcTemplate.update(SQL, title);
    }

    // Виконує SQL запит до БД для додавання нової підкатегорії
    public void addNewType(String title, int catId){
        String SQL = """
                INSERT INTO type(name, category_id) VALUES (?, ?);
                """;
        jdbcTemplate.update(SQL, title, catId);

    }

    // Виконує SQL запит до БД для видалення категорії
    public void deleteCategory(int id) {
        String SQL = """
                BEGIN;
                DELETE FROM product WHERE type_id IN (SELECT type_id FROM type WHERE category_id = ?);
                DELETE FROM type WHERE category_id = ?;
                DELETE FROM category WHERE category_id = ?;
                COMMIT;
                """;
        jdbcTemplate.update(SQL, id, id, id);
    }

    // Виконує SQL запит до БД для видалення підкатегорії
    public void deleteType(int id){
        String SQL = """
                BEGIN;
                DELETE FROM product WHERE type_id = ?;
                DELETE FROM type WHERE type_id = ?;
                COMMIT;
                """;
        jdbcTemplate.update(SQL, id, id);

    }
}
