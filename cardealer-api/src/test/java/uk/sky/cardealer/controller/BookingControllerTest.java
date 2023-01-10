package uk.sky.cardealer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.sky.cardealer.model.Booking;
import uk.sky.cardealer.service.BookingService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingControllerTest {

    @Autowired
    BookingController bookingController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    private Calendar calendar;
    private Booking bookingOne;
    private Booking bookingTwo;
    private List<Booking> allBookings = new ArrayList<>();

    @BeforeAll
    public void beforeAll() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        bookingOne = new Booking("1", "12", "123", new Date(), calendar.getTime());
        bookingTwo = new Booking("2", "23", "234", new Date(), calendar.getTime());
        allBookings.add(bookingOne);
        allBookings.add(bookingTwo);
    }


    @Test
    public void contextLoads() {
        assertThat(bookingController).isNotNull();
    }

    @Test
    public void shouldReturnCreatedBookingWhenPosted() throws Exception {
        when(bookingService.saveBooking(any())).thenReturn(bookingOne);

        this.mockMvc.perform(post("/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingOne)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(bookingOne.getUserId()))
                .andExpect(jsonPath("$.carId").value(bookingOne.getCarId()))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andDo(print());
    }

    @Test
    public void shouldReturnBookingWhenIdIsCorrect() throws Exception {
        when(bookingService.findById(anyString())).thenReturn(Optional.of(bookingOne));

        this.mockMvc.perform(get("/booking/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(bookingOne.getUserId()))
                .andExpect(jsonPath("$.carId").value(bookingOne.getCarId()))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists());
    }

    @Test
    public void shouldReturnAllBookings() throws Exception {
        when(bookingService.findAllBookings()).thenReturn(allBookings);

        this.mockMvc.perform(get("/booking"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId").value(bookingOne.getUserId()))
                .andExpect(jsonPath("$[0].carId").value(bookingOne.getCarId()))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[1].userId").value(bookingTwo.getUserId()))
                .andExpect(jsonPath("$[1].carId").value(bookingTwo.getCarId()))
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists());
    }

    @Test
    public void shouldReturnAllBookingsByUserId() throws Exception {
        List<Booking> userList = new ArrayList<>();
        userList.add(bookingOne);
        when(bookingService.findBookingsByUserId(anyString())).thenReturn(userList);

        this.mockMvc.perform(get("/booking?userId=12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value(bookingOne.getUserId()))
                .andExpect(jsonPath("$[0].carId").value(bookingOne.getCarId()))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists());
    }

    @Test
    public void shouldReturnAllBookingsByCarId() throws Exception {
        List<Booking> carList = new ArrayList<>();
        carList.add(bookingOne);
        when(bookingService.findBookingsByCarId(anyString())).thenReturn(carList);

        this.mockMvc.perform(get("/booking?carId=123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value(bookingOne.getUserId()))
                .andExpect(jsonPath("$[0].carId").value(bookingOne.getCarId()))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists());
    }


}
