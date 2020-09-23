package com.spring.cosmos.ebookstore.model.order;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface OrderRepository extends CosmosRepository<Order, String> {
    List<Order> getOrdersByCustomerId(String customerId);
}
