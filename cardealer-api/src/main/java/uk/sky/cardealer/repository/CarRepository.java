package uk.sky.cardealer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.sky.cardealer.model.Car;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends MongoRepository<Car, String> {

    Optional<Car> findByName(String name);
    List<Car> findAllByYearMade(Date yearMade);
    List<Car> findAllByPrice(Long price);
    List<Car> findAllByMileage(Long mileage);
    List<Car> findAllByColour(String colour);
    List<Car> findAllByBrandId(String brandId);
}
