package uk.sky.cardealer.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import uk.sky.cardealer.CardealerApplication;
import uk.sky.cardealer.model.Car;
import uk.sky.cardealer.repository.CarRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CardealerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private final Car car = new Car();

    private final List<Car> list = new ArrayList<>();

    @BeforeAll
    public void before() {
        car.setId("1");
        car.setName("Bob");
        car.setColour("Brown");
        car.setMileage((long) 10000);
        car.setPrice((long) 250000);
        car.setYearMade(new Date());
        car.setBrandId("1234");
        list.add(car);
    }

    @Test
    public void findByNameReturnsCorrectly() {
        when(carRepository.findByName(anyString())).thenReturn(Optional.of(car));

        Car retrievedCar = carService.findByName("Bob").get();

        assertEquals(car, retrievedCar);
    }

    @Test
    public void findAllByYearMadeReturnsCorrectly() {
        when(carRepository.findAllByYearMade(any())).thenReturn(list);

        List<Car> retrievedList = carService.findAllByYearMade(car.getYearMade());

        assertEquals(list, retrievedList);
    }

    @Test
    public void findAllByPriceReturnsCorrectly() {
        when(carRepository.findAllByPrice(anyLong())).thenReturn(list);

        List<Car> retrievedList = carService.findAllByPrice(car.getPrice());

        assertEquals(list, retrievedList);
    }

    @Test
    public void findAllByMileageReturnsCorrectly() {
        when(carRepository.findAllByMileage(anyLong())).thenReturn(list);

        List<Car> retrievedList = carService.findAllByMileage(car.getMileage());

        assertEquals(list, retrievedList);
    }

    @Test
    public void findAllByColourReturnsCorrectly() {
        when(carRepository.findAllByColour(anyString())).thenReturn(list);

        List<Car> retrievedList = carService.findAllByColour(car.getColour());

        assertEquals(list, retrievedList);
    }

    @Test
    public void findAllByBrandIdReturnsCorrectly() {
        when(carRepository.findAllByBrandId(anyString())).thenReturn(list);

        List<Car> retrievedList = carService.findAllByBrandId(car.getBrandId());

        assertEquals(list, retrievedList);
    }

    @Test
    public void saveCarReturnsCorrectly() {
        when(carRepository.save(any())).thenReturn(car);

        Car newCar = carService.saveCar(car);

        assertEquals(car, newCar);
    }

    @Test
    public void findAllReturnsCorrectly() {
        when(carRepository.findAll()).thenReturn(list);

        List<Car> retrievedList = carService.findAll();

        assertEquals(list, retrievedList);
    }

    @Test
    public void findCarByIdReturnsCorrectly() {
        when(carRepository.findById(anyString())).thenReturn(Optional.of(car));

        Car foundCar = carService.getCar(car.getId()).get();

        assertEquals(car, foundCar);
    }
}
