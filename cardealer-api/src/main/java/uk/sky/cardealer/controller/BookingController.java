package uk.sky.cardealer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.sky.cardealer.model.Booking;
import uk.sky.cardealer.service.BookingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("")
    public ResponseEntity<Booking> addBooking(@RequestBody Booking booking) {
        List<Booking> bookings = bookingService.findAllBookings();
        for(Booking item : bookings) {
            if(item.equals(booking)) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
        Booking newBooking = bookingService.saveBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable String id) {
        Optional<Booking> booking = bookingService.findById(id);
        return booking.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @GetMapping("")
    public ResponseEntity<List<Booking>> getAllBookings(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "carId", required = false) String carId) {
        if(userId != null) {
            return new ResponseEntity<>(bookingService.findBookingsByUserId(userId), HttpStatus.OK);
        } else if(carId != null) {
            return new ResponseEntity<>(bookingService.findBookingsByCarId(carId), HttpStatus.OK);
        }
        List<Booking> allBookings = bookingService.findAllBookings();
        return new ResponseEntity<>(allBookings, HttpStatus.OK);
    }

}
