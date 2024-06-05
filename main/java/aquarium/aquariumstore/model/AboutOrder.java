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
public class AboutOrder {
    int orderId;
    String orderStatus;
    String customer;
    String phone;
    String customerEmail;
    String postalCode;
    String country;
    String city;
    String street;
    String building;
    String details;
    String paymentStatus;
    String paymentMethod;
    int employee;
    double total;
    Date orderDate;
}
