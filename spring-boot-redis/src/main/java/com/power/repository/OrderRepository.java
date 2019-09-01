package com.power.repository;

import com.power.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wwupower
 * @Title: OrderRepository
 * @history 2019年09月01日
 * @since JDK1.8
 */
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("select max(orderNo) from Order")
    Long getMaxId();

}
