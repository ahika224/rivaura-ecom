package com.ecommerce.jewelleryMart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name  = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private List<Long> productIds; // List of product IDs in the cart
    private List<Integer> quantities; // Corresponding quantities for each product in productIds
    private List<Double> grams;       // NEW
    private List<Double> finalPrices;  // NEW

  public Cart() {
        this.productIds = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.grams = new ArrayList<>();
        this.finalPrices = new ArrayList<>();
    }

    public Cart(String userId, List<Long> productIds, List<Integer> quantities, List<Integer> grams, List<Double> finalPrices) {
        this.userId = userId;
        this.productIds = productIds != null ? new ArrayList<>(productIds) : new ArrayList<>();
        this.quantities = quantities != null ? new ArrayList<>(quantities) : new ArrayList<>();
        this.grams = new ArrayList<>();
        this.finalPrices = finalPrices != null ? new ArrayList<>(finalPrices) : new ArrayList<>();
    }
    // --- Getters and Setters ---


  }
