package com.ecommerce.jewelleryMart.repository;

import com.ecommerce.jewelleryMart.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {
}
