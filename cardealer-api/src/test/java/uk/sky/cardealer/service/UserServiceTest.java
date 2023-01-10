package uk.sky.cardealer.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import uk.sky.cardealer.CardealerApplication;
import uk.sky.cardealer.model.User;
import uk.sky.cardealer.repository.UserRepository;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DataMongoTest
@ContextConfiguration(classes = CardealerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User user = new User();

    @BeforeAll
    public void before() {
        user.setEmail("example@example.com");
        user.setFirstName("Bob");
        user.setLastName("Bobbinson");
    }

    @Test
    public void findUserByEmailReturnsCorrectly() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        User user2 = userService.getUserByEmailAddress("example@example.com").get();

        assertEquals(user, user2);
    }

    @Test
    public void findUserByFirstAndLastNameReturnsCorrectly() {
        when(userRepository.findUserByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.of(user));

        User user2 = userService.getUserByFirstAndLastName("Bob", "Bobbinson").get();

        assertEquals(user, user2);
    }

    @Test
    public void userServiceSavesANewUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertTrue(userService.addUser(user) instanceof User);
    }
}
