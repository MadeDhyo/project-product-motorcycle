package project_testing.demo.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import project_testing.demo.model.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Products", description = "Products CRUD API")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ItemRepository itemRepository;

    public ProductController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Operation(summary = "Get all products", description = "Fetch all products with pagination")
    @GetMapping
    public ItemListResponse readAllItems(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false) String category,
    @RequestParam(required = false) Long brandId // Added brandId here
) {
    List<Item> results;
        // 1. Check if filtering by Brand ID
        if (brandId != null) {
            results = itemRepository.findByBrandId(brandId);
            return new ItemListResponse(results.size(), results);
        } 
        // 2. Check if filtering by Category
        if (category != null && !category.isEmpty()) {
            results = itemRepository.findByCategoriesContainingIgnoreCase(category);
            return new ItemListResponse(results.size(), results);
        }
        // 3. Default: Paginated Return
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> pageResult = itemRepository.findAll(pageable);
        return new ItemListResponse((int) pageResult.getTotalElements(), pageResult.getContent());
    }

    @Operation(summary = "Filter products", description = "Filter items by brand name or category")
    @GetMapping("/filter")
    public ItemListResponse filterItems(
        @RequestParam(required = false) String name,      // Add this
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Long brandId
    ) {
        List<Item> results;
        
        if (name != null && !name.trim().isEmpty()) {
            results = itemRepository.findByNameContainingIgnoreCase(name);
        } else if (brandId != null) {
            results = itemRepository.findByBrandId(brandId);
        } else if (category != null && !category.trim().isEmpty()) {
            results = itemRepository.findByCategoriesContainingIgnoreCase(category);
        } else {
            results = itemRepository.findAll();
        }
        
        return new ItemListResponse(results.size(), results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> readOneItem(@PathVariable String id) {
        return itemRepository.findById(id)
            .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item newItem) {
        Item saved = itemRepository.save(newItem);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Upload Bulk Products", description = "API for uploading multiple products at once")
    @PostMapping("/bulk")
    public ResponseEntity<List<Item>> createBulkItems(@RequestBody List<Item> newItems) {
        List<Item> savedItems = itemRepository.saveAll(newItems);
        return new ResponseEntity<>(savedItems, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItemName(@PathVariable String id, @RequestBody Map<String, String> updates) {
        return itemRepository.findById(id).map(existingItem -> {
            // Extract "name" from the request body
            if (updates.containsKey("name")) {
                existingItem.setName(updates.get("name"));
            }
            
            // Save the updated entity back to the database
            Item saved = itemRepository.save(existingItem);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // 1. DELETE SINGLE ITEM
    // URL: DELETE http://localhost:8080/api/v1/products/{uuid}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable String id) {
        try {
            return itemRepository.findById(id).map(item -> {
                itemRepository.delete(item);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // This will print the EXACT database error in your VS Code terminal
            e.printStackTrace(); 
            return new ResponseEntity<>("Database Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }   
}


    // 2. DELETE BULK ITEMS
    // URL: DELETE http://localhost:8080/api/v1/products/bulk
    // Body (JSON): ["uuid-1", "uuid-2", "uuid-3"]
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulkItems(@RequestBody List<String> ids) {
        itemRepository.deleteAllById(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}