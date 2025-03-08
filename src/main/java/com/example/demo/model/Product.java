package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class Product {

    private String title;
    private String description;
    private int price;
    private String stock;
    private Review review;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Review{
        private int rate;
        private int counts;
    }
}
