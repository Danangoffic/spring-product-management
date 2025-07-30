package com.obs.OrderManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.obs.OrderManagement.models.Item;
import com.obs.OrderManagement.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemService {
    @Autowired
    private ItemRepository itemRepo;

    public List<Item> listItems(int page, int size) {
        return itemRepo.findAll(PageRequest.of(page, size)).getContent();
    }

    public Optional<Item> getItem(Long id) {
        return itemRepo.findById(id);
    }

    public Item saveItem(Item i) {
        return itemRepo.save(i);
    }

    public Item updateItem(Long id, Item i) {
        i.setId(id);
        return itemRepo.save(i);
    }

    public void deleteItem(Long id) {
        itemRepo.deleteById(id);
    }
}
