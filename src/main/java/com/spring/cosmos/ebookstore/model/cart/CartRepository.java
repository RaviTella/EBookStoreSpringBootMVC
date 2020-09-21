package com.spring.cosmos.ebookstore.model.cart;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import java.math.BigDecimal;

public interface CartRepository extends CosmosRepository<Cart, String> {

}
