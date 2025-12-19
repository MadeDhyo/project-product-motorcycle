package project_testing.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project_testing.demo.model.Brand;
import project_testing.demo.model.BrandRepository;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @PostMapping
    public Brand createBrand(@RequestBody Brand brand) {
        return brandRepository.save(brand);
    }

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // 1. DELETE SINGLE BRAND
    // URL: DELETE http://localhost:8080/api/brands/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        return brandRepository.findById(id).map(brand -> {
            brandRepository.delete(brand);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 2. DELETE BULK BRANDS
    // URL: DELETE http://localhost:8080/api/brands/bulk
    // Body: [1, 2, 3]
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkBrands(@RequestBody List<Long> ids) {
        brandRepository.deleteAllById(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}