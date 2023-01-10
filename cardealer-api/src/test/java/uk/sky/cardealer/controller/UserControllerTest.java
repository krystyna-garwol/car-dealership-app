package uk.sky.cardealer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.sky.cardealer.model.User;
import uk.sky.cardealer.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void shouldReturn404WithEmptyGetRequest() throws Exception {
        this.mockMvc.perform(get("/user")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn400BadRequestWhenEmailDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/user?email=test@test.com")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400BadRequestWhenFirstAndLastNameDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/user?firstName=Don&lastName=Donningtons")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAUserWithAValidEmail() throws Exception {
        User user = new User();
        user.setId("12345");
        user.setLastName("Jimmington");
        user.setFirstName("Jim");
        user.setEmail("test@test.com");
        when(userService.getUserByEmailAddress(user.getEmail())).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/user?email=test@test.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnAUserWithAValidFirstAndLastName() throws Exception {
        User user = new User();
        user.setId("12345");
        user.setLastName("Jimmington");
        user.setFirstName("Jim");
        user.setEmail("test@test.com");
        when(userService.getUserByFirstAndLastName(user.getFirstName(), user.getLastName())).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/user?firstName=Jim&lastName=Jimmington"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnCreatedUserWhenPosted() throws Exception {
        User user = new User();
        user.setId("12345");
        user.setLastName("Jimmington");
        user.setFirstName("Jim");
        user.setEmail("test@test.com");
        when(userService.addUser(any(User.class))).thenReturn(user);

        this.mockMvc.perform(post("/user").content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnBadRequestWhenPostingWithNoContent() throws Exception {
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenPostingWithImproperContent() throws Exception {
        this.mockMvc.perform(post("/user").content("{ test: 1 }").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
