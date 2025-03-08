package com.example.demo.controllers;

import com.example.demo.model.Category;
import com.example.demo.model.CategoryDTO;
import com.example.demo.model.Product;
import com.example.demo.model.ProductDTO;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class CatalogueController {
    List<LinkedTreeMap> categoryList;
    int pagination = 8;

    @PostConstruct
    public void init() {
        try {
            JSONParser parser = new JSONParser();
            //Use JSONObject for simple JSON and JSONArray for array of JSON.
            JSONArray data = (JSONArray) parser.parse(
                    new FileReader("src/main/resources/static/data.json"));//path to the JSON file.

            String json = data.toJSONString();
            Gson gson = new Gson();
            categoryList = gson.fromJson(json, List.class);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/api/category")
    public CategoryDTO getCategories() throws InterruptedException {
        Thread.sleep(1000);
        List<Category> categories = new ArrayList<>();
        for (LinkedTreeMap category : categoryList) {
            Category c = new Category();
            c.setName((String) category.get("name"));
            c.setImage((String) category.get("image"));
            categories.add(c);
        }
        return new CategoryDTO(categories, categories.size());
    }

    @GetMapping("/api/products")
    public ProductDTO getProductsByCategory(@RequestParam(required = false) String category, @RequestParam(required = false) String page) throws InterruptedException {
        Thread.sleep(1000);
        List<Product> products = new ArrayList<>();

        for (LinkedTreeMap categoryJSON : categoryList) {
            if (StringUtils.isEmpty(category) || categoryJSON.get("name").toString().equalsIgnoreCase(category)) {
                products.addAll((Collection<? extends Product>) categoryJSON.get("products"));
            }
        }
        if (!StringUtils.isEmpty(page) && StringUtils.isEmpty(category)) {
            int pageAsInt = Integer.parseInt(page);
            return new ProductDTO(products.subList(pagination * (pageAsInt - 1), pagination * (pageAsInt) - 1), products.size());
        }
        return new ProductDTO(products, products.size());
    }


    @GetMapping("/api/single-product/{id}")
    public Product getProductsByCategory(@PathVariable String id) {
        List<LinkedTreeMap> products = new ArrayList<>();
        for (LinkedTreeMap categoryJSON : categoryList) {
                products.addAll((Collection<? extends LinkedTreeMap>) categoryJSON.get("products"));
        }
        for (LinkedTreeMap product : products) {
            if (product.get("title").toString().equalsIgnoreCase(id)) {
                Product p = new Product();
                p.setDescription(product.get("description").toString());
                p.setTitle(product.get("title").toString());
                p.setStock(product.get("stock").toString());
                p.setPrice(100);
                p.setReview(new Product.Review(5, 100));
                return p;
            }
        }
        return null;
    }

    @GetMapping(value = {"/products/{image}", "/category/{image}"})
    public ResponseEntity<Resource> getImageProduct(@PathVariable String image) throws MalformedURLException, InterruptedException {
        Path path = Paths.get("src/main/java/com/example/demo/assets" + File.separator + image);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
