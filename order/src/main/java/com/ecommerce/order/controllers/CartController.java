package com.ecommerce.order.controllers;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    Logger logger = LogManager.getLogger(CartController.class);
    @PostMapping("/")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID")  String userId,@RequestBody CartItemRequest request){
         if(!cartService.addToCart(userId,request)) return ResponseEntity.badRequest().body("Out of Stock");
         return ResponseEntity.status(HttpStatus.CREATED).body("Added Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,@PathVariable("id") String productId){
        boolean deleted=cartService.deleteItemFromCart(userId,productId);
        logger.info(deleted);
        if(deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("/")
    public ResponseEntity<List<CartItem>> getCartItem(@RequestHeader("X-USER-ID") String userId){
       return ResponseEntity.ok(cartService.getCart(userId));

    }
}
