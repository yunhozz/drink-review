package drinkreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DrinkReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrinkReviewApplication.class, args);
	}
}
