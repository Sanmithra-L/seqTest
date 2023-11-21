package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
//    public void saveUserDetails(UserInfo user) {
//        // Ensure userRepo is not null before using it
//        if (userRepo != null) {
//            userRepo.save(user);
//        } else {
//            // Handle the case where userRepo is null (e.g., log a message)
//            System.err.println("UserRepo is null!");
//        }
//    }

    public UserInfo saveUserDetails(UserInfo userInfo){
        return userRepo.save(userInfo);
    }
    public CustomUserDetailService(UserRepo userRepo){
        this.userRepo=userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    public UserInfo loadIdbyUsername(String username){
        return userRepo.findByUsername(username);

    }
    public String loginUser(String username, String password) {
        UserInfo temp = userRepo.findByUsername(username);

        if (temp == null) {
            return "Username or password incorrect";
        } else if (password == null || !temp.getPassword().equals(password)) {
            return "Username or password incorrect";
        } else {
            return temp.getId().toString();
        }
    }

    public String loadUserbyName(String name){
        UserInfo temp=userRepo.findByUsername(name);
        if(temp!=null){
            return "User Exits";
        }
       else {
           return null;
        }
    }
}
