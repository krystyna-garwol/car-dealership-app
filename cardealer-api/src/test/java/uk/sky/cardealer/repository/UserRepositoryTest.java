package uk.sky.cardealer.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import uk.sky.cardealer.CardealerApplication;
import uk.sky.cardealer.model.User;

@ContextConfiguration(classes = CardealerApplication.class)
@AutoConfigureMockMvc
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void userRepositoryCanSaveUsersCorrectly() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Bobbingtons");
        user.setEmail("bob.bobbingtons@gmail.com");

        assertEquals(user, userRepository.save(user));
    }

    @Test
    @Order(2)
    public void userRepositoryCanFindAUserByEmail() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Bobbingtons");
        user.setEmail("bob.bobbingtons@gmail.com");

        User user2 = userRepository.findUserByEmail("bob.bobbingtons@gmail.com").get();

        assertEquals(user.getFirstName(), user2.getFirstName());
        assertEquals(user.getLastName(), user2.getLastName());
        assertEquals(user.getEmail(), user2.getEmail());
    }

    @Test
    @Order(3)
    public void userRepositoryCanFindAUserByFirstAndLastName() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Bobbingtons");
        user.setEmail("bob.bobbingtons@gmail.com");

        User user2 = userRepository.findUserByFirstNameAndLastName("Bob", "Bobbingtons").get();

        assertEquals(user.getFirstName(), user2.getFirstName());
        assertEquals(user.getLastName(), user2.getLastName());
        assertEquals(user.getEmail(), user2.getEmail());
    }
}
