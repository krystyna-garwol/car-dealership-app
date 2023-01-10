package uk.sky.cardealer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.sky.cardealer.model.User;
import uk.sky.cardealer.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> getUserByFirstAndLastName(String firstName, String lastName) {
        return userRepository.findUserByFirstNameAndLastName(firstName, lastName);
    }

    public Optional<User> getUserByEmailAddress(String email) {
        return userRepository.findUserByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }
}
