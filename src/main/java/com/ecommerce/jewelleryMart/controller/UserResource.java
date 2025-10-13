package com.ecommerce.jewelleryMart.controller;


import com.ecommerce.jewelleryMart.SecurityConfig.JwtProvider;
import com.ecommerce.jewelleryMart.model.User;
import com.ecommerce.jewelleryMart.repository.UserRepository;
import com.ecommerce.jewelleryMart.response.AuthResponse;
import com.ecommerce.jewelleryMart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (<a href="https://www.getarrays.io">Get Arrays, LLC</a>)
 * @email getarrayz@gmail.com
 * @since 11/22/2023
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserResource {
    @Autowired
    private final UserRepository userRepo;
    @Autowired
    private final UserService userService;
        @Autowired
    private PasswordEncoder passwordEncoder;
    private LocalDate localDate = LocalDate.now(ZoneId.of("GMT+02:30"));

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        User savedUser = userService.createUser(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        authResponse.setUser(savedUser);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PutMapping("/user")
    public ResponseEntity<AuthResponse> updateUserHandler(@RequestBody User user) throws Exception {



        User savedUser = userService.updateUser(user);


        AuthResponse authResponse = new AuthResponse();
        if(savedUser!=null){
            authResponse.setMessage(" update Success!");
authResponse.setUser(savedUser);
        }
        else{
            authResponse.setMessage("Details not found!");


        }


        return new ResponseEntity<>(authResponse,HttpStatus.OK);


    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
 
        System.out.println(email+"-------"+password);
        User user=userRepo.findByMail(email);
        Authentication authentication = authenticate(email,password, user.isAdmin());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = getAuthResponse(user, token, authentication);


        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }

    private static AuthResponse getAuthResponse(User user, String token, Authentication authentication) {
        AuthResponse authResponse = new AuthResponse();

        if(user !=null){
            authResponse.setMessage("Login success");
            authResponse.setJwt(token);
            authResponse.setStatus(true);
            authResponse.setUser(user);
        }
        else{
            authResponse.setMessage("Details not found");

        authResponse.setStatus(false);
        }
        if(authentication.isAuthenticated()){


        }
        else{
            authResponse.setMessage("Email is not authenticate!");

            authResponse.setStatus(false);
        }
        return authResponse;
    }


    private Authentication authenticate(String email, String password,boolean role) {

        System.out.println(email+"---++----"+password);
        UserDetails userDetails=userService.loadUserByRole(email,role);



        System.out.println("Sign-in in user details"+ userDetails);

        if(userDetails == null) {
            System.out.println("Sign in details - null" );

            throw new BadCredentialsException("Invalid email and password");
        }
        if(role && !password.equalsIgnoreCase(userDetails.getPassword()) ||
        !role &&  !passwordEncoder.matches(password,userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch"+userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    @PutMapping("/reset-password")
    public ResponseEntity<AuthResponse> updatePassword(@RequestBody User updateRequest) {
        String email = updateRequest.getEmail();
        String password = updateRequest.getPassword();

        System.out.println(email+"-------"+password);

        User user=userService.updatePassword(email, password);
        AuthResponse authResponse = new AuthResponse();
        if(user!=null){
            authResponse.setMessage("Password updated!");

        }
        else{
            authResponse.setMessage("Details not found!");


        }


        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }



    }
