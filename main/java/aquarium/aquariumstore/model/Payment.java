package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Payment {
    private int ID;
    private String status;
    private String method;
    private int orderID;
}
