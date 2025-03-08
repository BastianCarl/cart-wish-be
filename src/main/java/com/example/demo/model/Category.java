package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Category {
    private String name;
    private String image;
    private List<Product> products;
}
