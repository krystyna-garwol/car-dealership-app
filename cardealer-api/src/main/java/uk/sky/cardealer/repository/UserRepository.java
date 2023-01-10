package uk.sky.cardealer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.sky.cardealer.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    long count();

    Optional<User> findUserByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findUserByEmail(String email);
}
