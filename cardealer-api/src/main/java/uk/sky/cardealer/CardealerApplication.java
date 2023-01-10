package uk.sky.cardealer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class CardealerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardealerApplication.class, args);
    }

}
