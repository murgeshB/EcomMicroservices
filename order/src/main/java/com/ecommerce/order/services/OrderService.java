package com.ecommerce.order.services;


import com.ecommerce.order.ModelMapperConfig;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;

    private final OrderRepository orderRepository;

    @Transactional
    public Optional<OrderResponse> createOrder(String userId) {
        //validate for CartItems
        List<CartItem> cartItems=cartService.getCart(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        /*//validate for User
        Optional<User> userOpt =userService.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            return Optional.empty();
        }
        User user=userOpt.get();*/
        //calculate total cost
        BigDecimal totalPrice =cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        //create Order
        Order order=new Order();
        order.setUserId(Long.valueOf(userId));
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem>  orderItems=cartItems.stream().map(item -> new OrderItem(null,order,item.getProductId(),item.getPrice(),item.getQuantity())).toList();
        order.setItems(orderItems);
        Order saveOrder=orderRepository.save(order);
        //clear the Cart
        cartService.clearCart(userId);
        return  Optional.of(mapToOrderResponse(saveOrder));

    }

    private OrderResponse mapToOrderResponse(Order saveOrder) {
        return new  OrderResponse(saveOrder.getId(),
                saveOrder.getTotalAmount(),
                saveOrder.getOrderStatus(),
                saveOrder.getItems().stream().map(orderItem -> new OrderItemDTO(
                        orderItem.getId(),
                        orderItem.getProductId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()
                )).toList(),
                saveOrder.getCreationDate()
        );
    }
}
