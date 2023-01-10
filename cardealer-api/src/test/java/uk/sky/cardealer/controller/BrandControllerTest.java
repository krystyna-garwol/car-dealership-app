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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.sky.cardealer.model.Brand;
import uk.sky.cardealer.model.File;
import uk.sky.cardealer.service.BrandService;
import uk.sky.cardealer.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrandControllerTest {

    @Autowired
    BrandController brandController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BrandService brandService;

    @MockBean
    FileService fileService;

    private List<Brand> brandList = new ArrayList<>();
    private Brand brand1 = new Brand();
    private Brand brand2 = new Brand();


    @BeforeAll
    public void beforeAll() {
        brand1.setName("Ford");
        brand1.setId("1234");
        brand2.setName("BMW");
        brand2.setId("4321");
        brand2.setFileId("1234");
        brandList.add(brand1);
        brandList.add(brand2);
    }

    @Test
    public void contextLoads() {
        assertThat(brandController).isNotNull();
    }

    @Test
    public void getAllBrandsShouldReturnAnArray() throws Exception {
        when(brandService.getAll()).thenReturn(brandList);

        this.mockMvc.perform(get("/brand"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(brand1.getName()))
                .andExpect(jsonPath("$[1].name").value(brand2.getName()));
    }

    @Test
    public void getBrandByIdShouldReturnOneBrand() throws Exception {
        when(brandService.getById(any())).thenReturn(Optional.of(brand1));

        this.mockMvc.perform(get("/brand?id=1234"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(brand1.getName()))
                .andExpect(jsonPath("$.id").value(brand1.getId()));
    }

    @Test
    public void getBrandByNameShouldReturnOneBrand() throws Exception {
        when(brandService.getBrandByName(any())).thenReturn(Optional.of(brand1));

        this.mockMvc.perform(get("/brand?name=Ford"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(brand1.getName()))
                .andExpect(jsonPath("$.id").value(brand1.getId()));
    }


    @Test
    public void uploadLogoShouldRunSuccessfully() throws Exception {
        when(brandService.getById(anyString())).thenReturn(Optional.of(brand1));
        when(brandService.updateBrandImage(anyString(), anyString())).thenReturn(true);
        when(fileService.uploadImage(any(), anyString())).thenReturn("1234");

        MockMultipartFile fileToUpload = new MockMultipartFile(
                "file",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "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".getBytes()
        );

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/brand/image/1234")
                .file(fileToUpload)
                .param("name", "newName"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void downloadLogoShouldRunSuccessfully() throws Exception {
        when(brandService.getById(anyString())).thenReturn(Optional.of(brand2));
        File file = new File();
        file.setFilename("image.png");
        file.setFileSize("45642");
        file.setFileType("image/png");
        file.setFile("0x89 0x50 0x4E 0x47 0x0D 0x0A 0x1A 0x0A".getBytes());
        when(fileService.downloadImage(anyString())).thenReturn(file);

        this.mockMvc.perform(get("/brand/image/1234").contentType(MediaType.IMAGE_PNG))
                .andDo(print())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"image.png\""))
                .andExpect(status().isOk());
    }

    @Test
    public void postingToAddABrandAddsABrand() throws Exception {
        when(brandService.addBrand(any())).thenReturn(brand1);

        this.mockMvc.perform(post("/brand").content(asJsonString(brand1))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value(brand1.getName()))
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
