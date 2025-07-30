package com.obs.OrderManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.obs.OrderManagement.exceptions.InsufficientStockException;
import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Inventory;
import com.obs.OrderManagement.models.Order;
import com.obs.OrderManagement.repository.InventoryRepository;
import com.obs.OrderManagement.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public Order saveOrder(Order order) {
        Inventory inventory = inventoryRepository.findByItemId(order.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item dengan id=" + order.getItem().getId() + " tidak ditemukan"));

        if (inventory.getQuantity() < order.getQuantity()) {
            log.error("Insufficient stock!");
            throw new InsufficientStockException(
                    inventory.getId(), order.getQuantity(), inventory.getQuantity());
        }

        inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
        inventoryRepository.save(inventory);
        log.info("Success save item with id : {}", inventory.getId());
        Double totalPrice = order.getQuantity() * inventory.getItem().getPrice();
        log.info("total price : {}", totalPrice);
        order.setPrice(totalPrice);
        order.setOrderNo("O-"+System.currentTimeMillis());
        order.setOrderDate(LocalDateTime.now());
        return orderRepo.save(order);
    }

    public List<Order> listOrders(int page, int size) {
        return orderRepo.findAll(PageRequest.of(page, size)).getContent();
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepo.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }
}
