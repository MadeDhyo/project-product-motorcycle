package project_testing.demo.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import project_testing.demo.model.Item;


@RestController
@RequestMapping("/api/items")
public class HelloController {
    private final List<Item> items = new ArrayList<>();

    @GetMapping
    public List<Item> readAllItems() {
        return items;
    }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody Item newItem) {
        items.add(newItem);
        return new ResponseEntity<>("Created item: " + newItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> readOneItem(@PathVariable String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return new ResponseEntity<>(item, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id, @RequestBody Item updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            Item existingItem = items.get(i);
            if (existingItem.getId().equals(id)) {
                updatedItem.setId(id);
                items.set(i, updatedItem);
                return new ResponseEntity<>(items.get(i), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                Item deletedItem = items.remove(i); 
                return new ResponseEntity<>(deletedItem, HttpStatus.OK);
            }
        }
    
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}