package uk.sky.cardealer.controller;

import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.sky.cardealer.model.Brand;
import uk.sky.cardealer.model.File;
import uk.sky.cardealer.service.BrandService;
import uk.sky.cardealer.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @Autowired
    FileService fileService;

    @GetMapping("")
    public ResponseEntity<?> getBrands(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "name", required = false) String name) {
        if (id != null) {
            Optional<Brand> brand = brandService.getById(id);
            if (brand.isPresent()) {
                return new ResponseEntity<>(brand.get(), HttpStatus.OK);
            }
        } else if (name != null) {
            Optional<Brand> brand = brandService.getBrandByName(name);
            if (brand.isPresent()) {
                return new ResponseEntity<>(brand, HttpStatus.OK);
            }
        }
        List<Brand> brands = brandService.getAll();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<Map> uploadLogo(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @PathVariable("id") String id) {
        if (!file.getContentType().equals("image/png") || file.getContentType() == null) {
            JSONObject failed = new JSONObject()
                    .put("success", false)
                    .put("error", "Invalid type");
            return new ResponseEntity<>(failed.toMap(), HttpStatus.BAD_REQUEST);
        }
        if (brandService.getById(id).isEmpty()) {
            JSONObject failed = new JSONObject()
                    .put("success", false)
                    .put("error", "Invalid brand");
            return new ResponseEntity<>(failed.toMap(), HttpStatus.BAD_REQUEST);
        }
        try {
            String fileId = fileService.uploadImage(file, name);
            brandService.updateBrandImage(id, fileId);
            JSONObject success = new JSONObject()
                    .put("success", true)
                    .put("fileId", fileId);
            return new ResponseEntity<>(success.toMap(), HttpStatus.OK);
        } catch (IOException e) {
            JSONObject failed = new JSONObject()
                    .put("success", false)
                    .put("error", e.getMessage());
            return new ResponseEntity<>(failed.toMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> downloadLogo(@PathVariable("id") String id) throws IOException {
        Optional<Brand> optionalBrand = brandService.getById(id);
        System.out.println(optionalBrand);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            if (brand.getFileId() != null) {
                File file = fileService.downloadImage(brand.getFileId());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(file.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(new ByteArrayResource(file.getFile()));
            }
        }
        return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
    }


    @PostMapping("")
    public ResponseEntity<Brand> addBrand(@RequestBody Brand brand) {
        Optional<Brand> nameCheck = brandService.getBrandByName(brand.getName());
        if (nameCheck.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        Brand newBrand = brandService.addBrand(brand);
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }
}
