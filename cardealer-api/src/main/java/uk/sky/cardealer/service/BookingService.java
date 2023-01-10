package uk.sky.cardealer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.sky.cardealer.model.Booking;
import uk.sky.cardealer.repository.BookingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    public Optional<Booking> findById(String id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findBookingsByUserId(String userId) {
        return bookingRepository.findAllByUserId(userId);
    }

    public List<Booking> findBookingsByCarId(String carId) {
        return bookingRepository.findAllByCarId(carId);
    }

    public List<Booking> findBookingsByStartDate(Date startDate) {
        return bookingRepository.findAllByStartDate(startDate);
    }

    public List<Booking> findBookingsByEndDate(Date endDate) {
        return bookingRepository.findAllByEndDate(endDate);
    }

    public long countAllBookings() {
        return bookingRepository.count();
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> findBookingsBetweenTwoDates(Date start, Date end) {
        return bookingRepository.findAllBetweenTwoDates(start, end);
    }
}
