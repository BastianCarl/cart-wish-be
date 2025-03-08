package com.example.demo.controllers;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItemDTO;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@RestController
public class CartController {
    public static Map<String, Set<CartItemDTO>> cart = new HashMap<>();
    @Autowired
    JwtService jwtService;

    public CartController() throws IOException {}
    @PostMapping("/api/cart/add")
    public void updateCart(@RequestBody CartItemDTO cartItem, HttpServletRequest request) throws IOException {
        String userName = jwtService.extractUserName(request.getHeader("Authorization").substring(7));
        if (cart.containsKey(userName)) {
            Set<CartItemDTO> products = cart.get(userName);
            boolean found = false;
            for (CartItemDTO product : products) {
                if (product.getTitle().equals(cartItem.getTitle())) {
                    product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                    found = true;
                    break;
                }
            }
            if (!found) {
                products.add(cartItem);
            }
        } else {
            cart.put(userName, new HashSet<>(Collections.singletonList(cartItem)));
        }
    }

    @GetMapping("/api/cart/get")
    public Set<CartItemDTO> getCart(HttpServletRequest request){
        String userName = jwtService.extractUserName(request.getHeader("Authorization").substring(7));
        return cart.get(userName);
    }
}
