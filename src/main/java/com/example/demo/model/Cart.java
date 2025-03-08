package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Cart {
    public static Map<String, List<CartItemDTO>> cart = new HashMap<>();
}
