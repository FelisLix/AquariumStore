package aquarium.aquariumstore;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AquariumStoreApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AquariumStoreApplication.class, args);
	}

}
