package com.obs.OrderManagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obs.OrderManagement.dto.ApiResponse;
import com.obs.OrderManagement.dto.ItemRequest;
import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.service.ItemService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/item")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;


    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Item>>> listItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Item> items = itemService.listItems(page, size);
        log.info("All items get successfully with page {} and size {}", page, size);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> getItem(@PathVariable Long id) {
        log.info("get item with id {}", id);
        Item item = itemService.getItem(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item dengan id=" + id + " tidak ditemukan"));
        log.info("item got with data {}", item);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Item>> createItem(
            @Valid @RequestBody ItemRequest req) {
        log.info("create item with data {}", req);
        Item toSave = new Item();
        toSave.setName(req.getName());
        toSave.setStock(req.getStock());
        Item saved = itemService.saveItem(toSave);
        log.info("item successfully created with data {}", saved);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest req) {
        log.info("update item with id {} and data {}", id, req);
        Item toUpdate = new Item();
        toUpdate.setName(req.getName());
        toUpdate.setStock(req.getStock());
        Item updated = itemService.updateItem(id, toUpdate);
        log.info("item successfully updated with data {}", updated);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        log.info("delete item with id {}", id);
        itemService.deleteItem(id);
        return ResponseEntity
                .ok(ApiResponse.success(null));
    }
}
