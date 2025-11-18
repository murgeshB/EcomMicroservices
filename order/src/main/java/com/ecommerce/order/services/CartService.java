package com.ecommerce.order.services;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartService {

   private final CartItemRepository cartItemRepository;

    Logger logger = LogManager.getLogger(CartService.class);

   public boolean addToCart(String userId, CartItemRequest request){
    /* Optional<Product> optionalProduct   =productRepository.findById(request.getProductId());
     if(optionalProduct.isEmpty()){return false;}
     Product product =optionalProduct.get();
     if(product.getStockQuantity()<request.getQuantity()){return false;}
      Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));
     if(userOpt.isEmpty()){return false;}
     User user=userOpt.get();*/
     CartItem existingCartItem =cartItemRepository.findByUserIdAndProductId(Long.valueOf(userId), request.getProductId());
     if(existingCartItem!=null){
         existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
         existingCartItem.setPrice(BigDecimal.valueOf(1000));
         cartItemRepository.save(existingCartItem);

     }
     else {
         CartItem cartItem=new CartItem();
         cartItem.setUserId(Long.valueOf(userId));
         cartItem.setProductId(request.getProductId());
         cartItem.setQuantity(request.getQuantity());
         cartItem.setPrice(BigDecimal.valueOf(1000));
         cartItemRepository.save(cartItem);
         }
     return true;
   }
    @Transactional
    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem =cartItemRepository.findByUserIdAndProductId(Long.valueOf(userId), Long.valueOf(productId));

        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
      return cartItemRepository.findByUserId(Long.valueOf(userId));
    }

    public void clearCart(String userId) {
       cartItemRepository.deleteByUserId(Long.valueOf(userId));
    }
}
