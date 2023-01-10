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
import uk.sky.cardealer.model.Car;
import uk.sky.cardealer.service.BookingService;
import uk.sky.cardealer.service.CarService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarControllerTest {

    @Autowired
    CarController carController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private BookingService bookingService;

    private List<Car> cars = new ArrayList<>();

    Car carOne;
    Car carTwo;
    Car carThree;

    @BeforeAll
    public void before() {
        carOne = new Car("1", "TT", new Date(), (long) 30000, (long) 20000, "Green", "123");
        carTwo = new Car("2", "A3", new Date(), (long) 30000, (long) 20000, "Green", "123");
        carThree = new Car("3", "X5", new Date(), (long) 100000, (long) 1000, "Navy", "456");
        cars.add(carOne);
        cars.add(carTwo);
        cars.add(carThree);
    }

    @Test
    public void contextLoads() {
        assertThat(carController).isNotNull();
    }

    @Test
    public void shouldReturnCreatedCarWhenPosted() throws Exception {
        when(carService.saveCar(any())).thenReturn(carOne);

        this.mockMvc.perform(post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carOne)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(carOne.getName()))
                .andExpect(jsonPath("$.yearMade").exists())
                .andExpect(jsonPath("$.price").value(carOne.getPrice()))
                .andExpect(jsonPath("$.mileage").value(carOne.getMileage()))
                .andExpect(jsonPath("$.colour").value(carOne.getColour()))
                .andExpect(jsonPath("$.brandId").value(carOne.getBrandId()))
                .andDo(print());
    }

    @Test
    public void shouldReturnBadRequestWhenPostingWithNoContent() throws Exception {
        this.mockMvc.perform(post("/car"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfAllCars() throws Exception {
        when(carService.findAll()).thenReturn(cars);

        this.mockMvc.perform(get("/car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(carOne.getName()))
                .andExpect(jsonPath("$[1].colour").value(carTwo.getColour()))
                .andExpect(jsonPath("$[2].brandId").value(carThree.getBrandId()))
                .andDo(print());
    }

    @Test
    public void shouldReturnCarById() throws Exception {
        when(carService.getCar(anyString())).thenReturn(Optional.of(carOne));

        this.mockMvc.perform(get("/car/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(carOne.getName()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfCarsByBrand() throws Exception {
        List<Car> brandCars = new ArrayList<>();
        brandCars.add(carOne);
        brandCars.add(carTwo);

        when(carService.findAllByBrandId(anyString())).thenReturn(brandCars);

        this.mockMvc.perform(get("/car?brandId=123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brandId").value(carOne.getBrandId()))
                .andExpect(jsonPath("$[1].brandId").value(carTwo.getBrandId()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfCarsByColour() throws Exception {
        List<Car> colourCars = new ArrayList<>();
        colourCars.add(carOne);
        colourCars.add(carTwo);

        when(carService.findAllByColour(anyString())).thenReturn(colourCars);

        this.mockMvc.perform(get("/car?colour=green"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].colour").value(carOne.getColour()))
                .andExpect(jsonPath("$[1].colour").value(carTwo.getColour()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfCarsByPrice() throws Exception {
        List<Car> priceCars = new ArrayList<>();
        priceCars.add(carOne);
        priceCars.add(carTwo);

        when(carService.findAllByPrice(anyLong())).thenReturn(priceCars);

        this.mockMvc.perform(get("/car?price=30000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(carOne.getPrice()))
                .andExpect(jsonPath("$[1].price").value(carTwo.getPrice()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfCarsByMileage() throws Exception {
        List<Car> mileageCars = new ArrayList<>();
        mileageCars.add(carOne);
        mileageCars.add(carTwo);

        when(carService.findAllByMileage(anyLong())).thenReturn(mileageCars);

        this.mockMvc.perform(get("/car?mileage=20000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].mileage").value(carOne.getMileage()))
                .andExpect(jsonPath("$[1].mileage").value(carTwo.getMileage()))
                .andDo(print());
    }

    @Test
    public void shouldReturnListOfCarsByYearMade() throws Exception {
        List<Car> yearMadeCars = new ArrayList<>();
        yearMadeCars.add(carOne);
        yearMadeCars.add(carTwo);

        when(carService.findAllByYearMade(any())).thenReturn(yearMadeCars);

        this.mockMvc.perform(get("/car?yearMade=2021-10-02"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].yearMade").exists())
                .andExpect(jsonPath("$[1].yearMade").exists());
    }

    @Test
    public void shouldReturnListOfCarsAvailableBetweenDates() throws Exception {
        List<Booking> datedCars = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Booking bookingOne = new Booking("1", "12", "123", new Date(), calendar.getTime());
        datedCars.add(bookingOne);

        when(bookingService.findBookingsBetweenTwoDates(any(), any())).thenReturn(datedCars);

        when(carService.findAll()).thenReturn(cars);

        this.mockMvc.perform(get("/car?available=2021-10-02~2021-10-08"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
