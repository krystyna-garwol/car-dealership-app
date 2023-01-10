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
import uk.sky.cardealer.model.Booking;
import uk.sky.cardealer.repository.BookingRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CardealerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private final Booking booking = new Booking();
    private List<Booking> allBookings = new ArrayList<>();

    @BeforeAll
    public void before() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        booking.setId("12345");
        booking.setUserId("123");
        booking.setCarId("456");
        booking.setStartDate(new Date());
        booking.setEndDate(calendar.getTime());
        allBookings.add(booking);
    }

    @Test
    public void findByIdReturnsCorrectly() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));

        Booking newBooking = bookingService.findById(booking.getId()).get();

        assertEquals(booking.getId(), newBooking.getId());
    }

    @Test
    public void findBookingsByUserIdReturnsCorrectly() {
        when(bookingRepository.findAllByUserId(anyString())).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findBookingsByUserId(booking.getUserId());

        assertEquals(allBookings, retrievedBookings);
    }

    @Test
    public void findBookingsByCarIdReturnsCorrectly() {
        when(bookingRepository.findAllByCarId(anyString())).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findBookingsByCarId(booking.getCarId());

        assertEquals(allBookings, retrievedBookings);
    }

    @Test
    public void findBookingsByStartDateReturnsCorrectly() {
        when(bookingRepository.findAllByStartDate(any())).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findBookingsByStartDate(booking.getStartDate());

        assertEquals(allBookings, retrievedBookings);
    }

    @Test
    public void findBookingsByEndDateReturnsCorrectly() {
        when(bookingRepository.findAllByEndDate(any())).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findBookingsByEndDate(booking.getEndDate());

        assertEquals(allBookings, retrievedBookings);
    }

    @Test
    public void countAllBookingsReturnsCorrectly() {
        when(bookingRepository.count()).thenReturn(allBookings.stream().count());

        long retrievedBookings = bookingService.countAllBookings();

        assertEquals(allBookings.stream().count(), retrievedBookings);
    }

    @Test
    public void saveBookingsReturnsCorrectly() {
        when(bookingRepository.save(any())).thenReturn(booking);

        Booking newBooking = bookingService.saveBooking(booking);

        assertEquals(booking, newBooking);
    }

    @Test
    public void canFindAllBookings() {
        when(bookingRepository.findAll()).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findAllBookings();

        assertEquals(allBookings, retrievedBookings);
    }

    @Test
    public void findBookingsBetweenTwoDatesIsCorrect() {
        when(bookingRepository.findAllBetweenTwoDates(any(), any())).thenReturn(allBookings);

        List<Booking> retrievedBookings = bookingService.findBookingsBetweenTwoDates(new Date(), new Date());

        assertEquals(allBookings, retrievedBookings);
    }

}
