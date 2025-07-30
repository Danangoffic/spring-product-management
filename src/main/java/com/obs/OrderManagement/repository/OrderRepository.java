package com.obs.OrderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obs.OrderManagement.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    
}
