package uk.sky.cardealer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.sky.cardealer.model.Booking;
import uk.sky.cardealer.model.Car;
import uk.sky.cardealer.service.BookingService;
import uk.sky.cardealer.service.CarService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    CarService carService;

    @Autowired
    BookingService bookingService;

    @PostMapping("")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Optional<Car> existingCar = carService.findByName(car.getName());
        if (existingCar.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        carService.saveCar(car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Car>> getAllCars(
            @RequestParam(value = "brandId", required = false) String brandId,
            @RequestParam(value = "colour", required = false) String colour,
            @RequestParam(value = "mileage", required = false) Long mileage,
            @RequestParam(value = "yearMade", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date yearMade,
            @RequestParam(value = "price", required = false) Long price,
            @RequestParam(value = "available", required = false) String dateSearch) throws ParseException {
        List<Car> cars;
        if (brandId != null) {
            cars = carService.findAllByBrandId(brandId);
        } else if (colour != null) {
            cars = carService.findAllByColour(colour);
        } else if (mileage != null) {
            cars = carService.findAllByMileage(mileage);
        } else if (yearMade != null) {
            cars = carService.findAllByYearMade(yearMade);
        } else if (price != null) {
            cars = carService.findAllByPrice(price);
        } else if (dateSearch != null) {
            String[] dates = dateSearch.split("~");
            Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dates[0]);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dates[1]);

            List<Booking> carsUnavailable = bookingService.findBookingsBetweenTwoDates(date1, date2);
            List<Car> allCars = carService.findAll();
            List<Car> availableCars = new ArrayList<>();
            allCars.forEach(car -> {
                if (carsUnavailable.stream().noneMatch(booking -> booking.getCarId().equals(car.getId()))) {
                    availableCars.add(car);
                }
            });
            return new ResponseEntity<>(availableCars, HttpStatus.OK);
        } else {
            cars = carService.findAll();
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getSingleCar(@PathVariable String id) {
        Optional<Car> car = carService.getCar(id);
        return car.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }
}
