package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import java.util.Date;

@Getter
@Setter
@ToString
@Component
public class Employee {
    private int ID;
    private String firstName;
    private String lastName;
    private String title;
    private String phone;
    private String email;
    private String country;
    private String city;
    private Date hireDate;
    private Date birthDate;
}
