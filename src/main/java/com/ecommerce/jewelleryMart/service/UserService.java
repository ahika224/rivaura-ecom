package com.ecommerce.jewelleryMart.service;

import com.ecommerce.jewelleryMart.model.User;
import com.ecommerce.jewelleryMart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (<a href="https://www.getarrays.io">Get Arrays, LLC</a>)
 * @email getarrayz@gmail.com
 * @since 11/22/2023
 */

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private LocalDate localDate = LocalDate.now(ZoneId.of("GMT+02:30"));


    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByMail(email);
        System.out.println(user);
       
        if(user==null) {
            throw new UsernameNotFoundException("User not found with this Number!"+email);

        }

        
        System.out.println("Loaded user: " + user.getEmail() + ", Admin: " + user.isAdmin());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
    public UserDetails loadUserByRole(String email, boolean role) throws UsernameNotFoundException {
        User user = userRepo.findByMail(email);
        System.out.println(user);
       
        if(user==null) {
            throw new UsernameNotFoundException("User not found with this email number"+email);

        }
        if(user.isAdmin()!=role){
            throw new UsernameNotFoundException("User role does not support to log in! "+email);
        }

       user.setLastLoggedIn(localDate);
        userRepo.save(user);
        
        System.out.println("Loaded user: " + user.getEmail() + ", Role: " + user.isAdmin());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
    public User updatePassword(String email, String password)  {
        User user=userRepo.findByMail(email);
        if(user!=null) {
            if(user.isAdmin()){
                user.setPassword(password);

            }
            else{
                user.setPassword(passwordEncoder.encode(password));

            }

            userRepo.save(user);
        }
        return user;
    }




    public User updateUser(User user) throws Exception {


        String username = user.getUsername();
        String phone = user.getPhone();
        String email=user.getEmail();


        User updatededUser = userRepo.findById(String.valueOf(user.getId())).get();


        if (!email.equalsIgnoreCase(updatededUser.getEmail())) {
            User isEmailExist = userRepo.findByMail(email);
            if(isEmailExist!=null) {
                throw new Exception("Email Is Already Used By Another Account");
            }
            updatededUser.setEmail(email);
        }
        updatededUser.setPhone(phone);

        updatededUser.setUsername(username);


        return userRepo.save(updatededUser);
    }

    public User createUser(User user) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();

        String phone=user.getPhone();
        LocalDate createdDate=localDate;

        User isPhoneExist = userRepo.findByMail(phone);
        if (isPhoneExist != null) {
            throw new Exception("Phone Is Already Used By Another Account");

        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPhone(phone);
        createdUser.setUsername(username);

        createdUser.setUserCreated(createdDate);
        createdUser.setAdmin(user.isAdmin());
        if(user.isAdmin()){
            createdUser.setPassword(password);

        }
        else{
            createdUser.setPassword(passwordEncoder.encode(password));

        }
        return userRepo.save(createdUser);
    }
}
