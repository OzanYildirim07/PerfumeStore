package com.perfumestore.repository;

import com.perfumestore.entity.Order;
import com.perfumestore.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findTop3ByOrderByTotalAmountDesc();

    List<Order> findByStatus(Order.OrderStatus status);
}
