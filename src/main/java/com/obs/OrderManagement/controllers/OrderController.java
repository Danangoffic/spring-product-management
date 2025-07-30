package com.obs.OrderManagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.obs.OrderManagement.dto.OrderRequest;
import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.models.Order;
import com.obs.OrderManagement.service.ItemService;
import com.obs.OrderManagement.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Order>>> listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Order> orders = orderService.listOrders(page, size);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order dengan id=" + id + " tidak ditemukan"));
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @Valid @RequestBody OrderRequest req) {
        Order o = new Order();
        Item item = itemService.getItem(req.getItemId());
        o.setItem(item);
        o.setQuantity(req.getQuantity());
        Order saved = orderService.saveOrder(o);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
