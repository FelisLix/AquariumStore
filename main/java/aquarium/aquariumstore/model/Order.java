package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Getter
@Setter
@ToString
@Component
public class Order {
    private int ID;
    private String status;
    private int customerID;
    private int employeeID;
    private double total;
    private Date orderDate;
}
