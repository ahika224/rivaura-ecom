package com.ecommerce.jewelleryMart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter

@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String profilePic; // (base64 or URL)
    private boolean isAdmin = false; // default false
    private LocalDate userCreated;
    private String phone;
    private LocalDate lastLoggedIn;

    // Default constructor (often needed by frameworks like Spring Data)
    public User() {
    }

    // Constructor with common fields
    public User(String username, String email, String password, String profilePic,LocalDate userCreated,LocalDate lastLoggedIn,String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.userCreated=userCreated;
        this.lastLoggedIn=lastLoggedIn;
        this.phone=phone;
    }

    // Getters and Setters for all fields

}