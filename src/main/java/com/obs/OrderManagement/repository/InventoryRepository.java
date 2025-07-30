package com.obs.OrderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.OrderManagement.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
}
