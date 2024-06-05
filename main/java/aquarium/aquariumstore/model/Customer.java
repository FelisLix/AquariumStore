package aquarium.aquariumstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Customer {
    private int ID;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
