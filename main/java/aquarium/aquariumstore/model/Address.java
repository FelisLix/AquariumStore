package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Address {
    private int ID;
    private String postalCode;
    private String city;
    private String street;
    private String building;
    private String details;
    private int customerID;
    private String country;
}
