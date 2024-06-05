package aquarium.aquariumstore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class Image {
    private int ID;
    private String URL;
}

