package uk.sky.cardealer.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import uk.sky.cardealer.CardealerApplication;
import uk.sky.cardealer.model.Brand;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = CardealerApplication.class)
@AutoConfigureMockMvc
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @Order(1)
    public void brandRepositoryCanSaveBrandsCorrectly() {
        Brand brand = new Brand();
        brand.setName("Ford");

        assertEquals(brand, brandRepository.save(brand));
    }

    @Test
    @Order(2)
    public void brandRepositoryCanFindBrandByName() {
        Brand brand = new Brand();
        brand.setName("Ford");

        Brand brand2 = brandRepository.findByName(brand.getName()).get();

        assertEquals(brand.getName(), brand2.getName());
    }
}
