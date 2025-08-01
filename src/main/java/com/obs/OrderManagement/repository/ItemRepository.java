package com.obs.OrderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.OrderManagement.models.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
}
