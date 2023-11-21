package com.example.captchabackend2.repo;

import com.example.captchabackend2.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserInfo,Integer> {

    public UserInfo findByUsername(String username);

    List<UserInfo> findAllByUsername(String dump);
}
