package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Order_line {
    private int ID;
    private int orderID;
    private int productID;
    private int quantity;
}
