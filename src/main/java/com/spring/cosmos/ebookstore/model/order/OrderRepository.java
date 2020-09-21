package com.spring.cosmos.ebookstore.model.order;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

public interface OrderRepository extends CosmosRepository<Order, String> {
     //@Query(value = "select * from c where c.customerId = @customerId")
    List<Order> getOrdersByCustomerId(String customerId);
}
