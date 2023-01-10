package uk.sky.cardealer.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import uk.sky.cardealer.model.Booking;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    Booking booking = new Booking();
    private Date start = new Date();
    private Calendar calendar = Calendar.getInstance();


    @BeforeAll
    public void setup() {
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        booking.setId("12345");
        booking.setUserId("54321");
        booking.setCarId("23415");
        booking.setStartDate(start);
        booking.setEndDate(calendar.getTime());
    }

    @Test
    @Order(1)
    public void bookingRepositoryCanSaveBookingCorrectly() {
        assertEquals(booking, bookingRepository.save(booking));
    }

    @Test
    @Order(2)
    public void bookingRepositoryFindAllByUserIdReturnsCorrectly() {
        List<Booking> allBookings = bookingRepository.findAllByUserId(booking.getUserId());

        Booking newBooking = allBookings.get(0);

        assertEquals(booking.getUserId(), newBooking.getUserId());
    }

    @Test
    @Order(3)
    public void bookingRepositoryFindAllByCarIdReturnsCorrectly() {
        List<Booking> allBookings = bookingRepository.findAllByCarId(booking.getCarId());

        Booking newBooking = allBookings.get(0);

        assertEquals(booking.getCarId(), newBooking.getCarId());
    }

    @Test
    @Order(4)
    public void bookingRepositoryFindAllByStartDateReturnsCorrectly() {
        List<Booking> allBookings = bookingRepository.findAllByStartDate(booking.getStartDate());

        Booking newBooking = allBookings.get(0);

        assertEquals(booking.getStartDate(), newBooking.getStartDate());
    }

    @Test
    @Order(5)
    public void bookingRepositoryFindAllByEndDateReturnsCorrectly() {
        List<Booking> allBookings = bookingRepository.findAllByEndDate(booking.getEndDate());

        Booking newBooking = allBookings.get(0);

        assertEquals(booking.getEndDate(), newBooking.getEndDate());
    }

    @Test
    @Order(6)
    public void bookingRepositoryCanFindAllBetweenTwoDates() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        List<Booking> bookings = bookingRepository.findAllBetweenTwoDates(start, calendar.getTime());

        Booking theBooking = bookings.get(0);

        assertEquals(booking.getCarId(), theBooking.getCarId());
    }
}
