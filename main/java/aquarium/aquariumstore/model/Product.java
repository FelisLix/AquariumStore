package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Component
public class Product {
    private int ID;
    private String name;
    private int typeID;
    private String type;
    private double price;
    private int quantity;
    private String size;
    private String description;
    private Timestamp createdAt;
    private int imageID;
    private String imageURL;
    private int lifespan;
    private String origin;
}



