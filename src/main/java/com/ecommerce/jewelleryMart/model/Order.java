package com.ecommerce.jewelleryMart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


@Entity
@Getter
@Setter

@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private List<Long> productIds;
    private List<Integer> quantities;
    private double totalAmount;
    private Date createdAt;
    private List<Double> grams;
    private boolean paid;

    private String deliveryName;
    private String deliveryContact;
    private String deliveryAddress;
    private String deliveryCity;


    // Default constructor
    public Order() {
        this.productIds = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.createdAt = new Date(); // Set creation date by default
    }

    // Parameterized constructor
    public Order(String userId, List<Long> productIds, List<Integer> quantities, double totalAmount) {
        this.userId = userId;
        this.productIds = productIds != null ? new ArrayList<>(productIds) : new ArrayList<>();
        this.quantities = quantities != null ? new ArrayList<>(quantities) : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.createdAt = new Date(); // Set creation date when order is created
    }


}
