package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class OrderLineInfo {
    private int orderLineId;
    private int orderID;
    private int productID;
    private String productName;
    private  double productPrice;
    private int quantity;
}