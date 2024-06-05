package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Category {
    private int ID;
    private String name;
}
