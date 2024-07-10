package com.heli.order_service.service;

import com.heli.order_service.client.InventoryClient;
import com.heli.order_service.dto.OrderRequest;
import com.heli.order_service.model.Order;
import com.heli.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock){
            // map OrderRequest to Order object
            Order orders = new Order();
            orders.setOrderNumber(UUID.randomUUID().toString());
            orders.setPrice(orderRequest.price());
            orders.setSkuCode(orderRequest.skuCode());
            orders.setQuantity(orderRequest.quantity());

            // save order to OrderRepository
            orderRepository.save(orders);
        }else {
            throw new RuntimeException("Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
        }


    }

}
