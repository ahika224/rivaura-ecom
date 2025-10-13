package com.ecommerce.jewelleryMart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter

@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private String category;
    private String metalType; // e.g., gold, silver, diamond
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images;
    private String description;
    private double weight;


    public Product() {}

    public Product(String name, double price, String category, String metalType, List<String> images, String description) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.metalType = metalType;
        this.images = images;
        this.description = description;
        this.weight = weight;
    }

   }
