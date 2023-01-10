package uk.sky.cardealer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.sky.cardealer.model.Car;
import uk.sky.cardealer.repository.CarRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    public Optional<Car> findByName(String name) {
        return carRepository.findByName(name);
    }

    public List<Car> findAllByYearMade(Date yearMade) {
        return carRepository.findAllByYearMade(yearMade);
    }

    public List<Car> findAllByPrice(Long price) {
        return carRepository.findAllByPrice(price);
    }

    public List<Car> findAllByMileage(Long mileage) {
        return carRepository.findAllByMileage(mileage);
    }

    public List<Car> findAllByColour(String colour) {
        return carRepository.findAllByColour(colour);
    }

    public List<Car> findAllByBrandId(String brandId) {
        return carRepository.findAllByBrandId(brandId);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Optional<Car> getCar(String id) {
        return carRepository.findById(id);
    }
}
