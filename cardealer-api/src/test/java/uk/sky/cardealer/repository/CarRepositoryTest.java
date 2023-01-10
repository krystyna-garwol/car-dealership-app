package uk.sky.cardealer.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import uk.sky.cardealer.model.Car;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;
    Car car = new Car();

    @BeforeAll
    public void setUp() {
        car.setId("1");
        car.setName("Bob");
        car.setColour("Brown");
        car.setMileage((long) 10000);
        car.setPrice((long) 250000);
        car.setYearMade(new Date());
        car.setBrandId("1234");
    }

    @Test
    @Order((1))
    public void carRepositoryCanSaveCarCorrectly() {
        assertEquals(car, carRepository.save(car));
    }

    @Test
    @Order((2))
    public void carRepositoryCanFindCarByName() {
        assertEquals(car.getName(), carRepository.findByName(car.getName()).get().getName());
    }

    @Test
    @Order((3))
    public void carRepositoryCanFindAllCarsByYearMade() {
        List<Car> allCars = carRepository.findAllByYearMade(car.getYearMade());
        Car carOne = allCars.get(0);
        assertEquals(car.getYearMade(), carOne.getYearMade());
    }

    @Test
    @Order((4))
    public void carRepositoryCanFindAllCarsByPrice() {
        List<Car> allCars = carRepository.findAllByPrice(car.getPrice());
        Car carOne = allCars.get(0);
        assertEquals(car.getPrice(), carOne.getPrice());
    }

    @Test
    @Order((5))
    public void carRepositoryCanFindAllCarsByMileage() {
        List<Car> allCars = carRepository.findAllByMileage(car.getMileage());
        Car carOne = allCars.get(0);
        assertEquals(car.getMileage(), carOne.getMileage());
    }

    @Test
    @Order((6))
    public void carRepositoryCanFindAllCarsByColour() {
        List<Car> allCars = carRepository.findAllByColour(car.getColour());
        Car carOne = allCars.get(0);
        assertEquals(car.getColour(), carOne.getColour());
    }

    @Test
    @Order((7))
    public void carRepositoryCanFindAllCarsByBrandId() {
        List<Car> allCars = carRepository.findAllByBrandId(car.getBrandId());
        Car carOne = allCars.get(0);
        assertEquals(car.getBrandId(), carOne.getBrandId());
    }

}
