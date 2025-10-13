package com.ecommerce.jewelleryMart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name ="coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    private double discountAmount;

    public Coupon() {}

    public Coupon(Long code, double discountAmount) {
        this.code = code;
        this.discountAmount = discountAmount;
    }

   }
