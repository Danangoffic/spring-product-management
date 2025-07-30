package com.obs.OrderManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.obs.OrderManagement.exceptions.ResourceNotFoundException;
import com.obs.OrderManagement.models.Inventory;
import com.obs.OrderManagement.models.InventoryType;
import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.repository.InventoryRepository;
import com.obs.OrderManagement.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Inventory> listInv(int page, int size) {
        return inventoryRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Inventory saveInv(Inventory inv) {
        // update stock di Item
        Item item = itemRepository.findById(inv.getItem().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        int delta = inv.getType() == InventoryType.T ? inv.getQuantity() : -inv.getQuantity();
        item.setPrice(item.getPrice() + delta);
        itemRepository.save(item);
        inv.setTimestamp(LocalDateTime.now());
        return inventoryRepository.save(inv);
    }

    public void deleteInv(Long id) {
        inventoryRepository.deleteById(id);
    }
}
