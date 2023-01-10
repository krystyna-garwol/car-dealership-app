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
import uk.sky.cardealer.model.Brand;
import uk.sky.cardealer.repository.BrandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CardealerApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private final Brand brand = new Brand();

    @BeforeAll
    public void before() {
        brand.setId("1234");
        brand.setName("Ford");
    }

    @Test
    public void findBrandByNameReturnsCorrectly() {
        when(brandRepository.findByName("Ford")).thenReturn(Optional.of(brand));

        Brand brand2 = brandService.getBrandByName("Ford").get();

        assertEquals(brand, brand2);
    }

    @Test
    public void brandServiceSavesANewBrandSuccessfully() {
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        assertTrue(brandService.addBrand(brand) instanceof Brand);
    }

    @Test
    public void brandServiceReturnsAllCorrectly() {
        List<Brand> brands = new ArrayList<>();
        brands.add(brand);

        when(brandRepository.findAll()).thenReturn(brands);

        assertEquals(brandService.getAll(), brands);
    }

    @Test
    public void brandServiceUpdatesBrandImageSuccessfully() {
        when(brandRepository.findById(anyString())).thenReturn(Optional.of(brand));
        when(brandRepository.save(any())).thenReturn(brand);

        assertTrue(brandService.updateBrandImage("12", "123"));
    }

    @Test
    public void brandServiceFindByIdReturnsCorrectly() {
        when(brandRepository.findById(anyString())).thenReturn(Optional.of(brand));
        assertEquals(brandService.getById(brand.getId()).get(), brand);
    }
}
