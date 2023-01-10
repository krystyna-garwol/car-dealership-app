package uk.sky.cardealer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.sky.cardealer.model.Brand;
import uk.sky.cardealer.repository.BrandRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    @Autowired
    BrandRepository brandRepository;

    public Optional<Brand> getBrandByName(String name) {
        return brandRepository.findByName(name);
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public boolean updateBrandImage(String id, String fileId) {
        Optional<Brand> optionalBrand = brandRepository.findById(id);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            brand.setFileId(fileId);
            brandRepository.save(brand);
            return true;
        }
        return false;
    }

    public Optional<Brand> getById(String id) {
        return brandRepository.findById(id);
    }
}
