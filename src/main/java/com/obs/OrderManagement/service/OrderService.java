package com.obs.OrderManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.obs.OrderManagement.exceptions.InsufficientStockException;
import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.models.Order;
import com.obs.OrderManagement.repository.ItemRepository;
import com.obs.OrderManagement.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ItemRepository itemRepo;

    @Transactional
    public Order saveOrder(Order order) {
        Item item = itemRepo.findById(order.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item dengan id=" + order.getItem().getId() + " tidak ditemukan"));

        if (item.getStock() < order.getQuantity()) {
            throw new InsufficientStockException(
                    item.getId(), order.getQuantity(), item.getStock());
        }

        item.setStock(item.getStock() - order.getQuantity());
        itemRepo.save(item);
        log.info("Success save item with id : {}", item.getId());
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
