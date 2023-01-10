package uk.sky.cardealer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.sky.cardealer.model.Brand;

import java.util.Optional;

public interface BrandRepository extends MongoRepository<Brand, String> {

    long count();

    Optional<Brand> findByName(String brand);
}
