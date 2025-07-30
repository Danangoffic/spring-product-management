package com.obs.OrderManagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.obs.OrderManagement.dto.ApiResponse;
import com.obs.OrderManagement.dto.InventoryRequest;
import com.obs.OrderManagement.models.Inventory;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.service.InventoryService;
import com.obs.OrderManagement.service.ItemService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/inventories")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Inventory>>> listInventories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Inventory> invs = inventoryService.listInv(page, size);
        return ResponseEntity.ok(ApiResponse.success(invs));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Inventory>> addInventory(
            @Valid @RequestBody InventoryRequest req) {
        Inventory inv = new Inventory();
        inv.setItem(itemService.getItem(req.getItemId()));
        inv.setType(req.getType());
        inv.setQuantity(req.getQuantity());
        Inventory saved = inventoryService.saveInv(inv);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInv(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
