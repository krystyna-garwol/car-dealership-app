package uk.sky.cardealer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.sky.cardealer.model.User;
import uk.sky.cardealer.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<User> getUser(@RequestParam(name = "firstName", required = false) String firstName, @RequestParam(name = "lastName", required = false) String lastName, @RequestParam(name = "email", required = false) String email) {
        if (email != null) {
            Optional<User> userOptional = userService.getUserByEmailAddress(email);
            return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        } else if (firstName != null && lastName != null) {
            Optional<User> userOptional = userService.getUserByFirstAndLastName(firstName, lastName);
            return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        Optional<User> emailCheck = userService.getUserByEmailAddress(user.getEmail());
        Optional<User> nameCheck = userService.getUserByFirstAndLastName(user.getFirstName(), user.getLastName());
        if (emailCheck.isPresent() || nameCheck.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
