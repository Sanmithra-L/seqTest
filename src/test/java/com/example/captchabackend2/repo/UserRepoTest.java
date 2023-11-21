package com.example.captchabackend2.repo;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.service.CustomUserDetailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepoTest {
    @Autowired
    private UserRepo userRepo;
    @AfterEach
    void deleteData(){
        userRepo.deleteAll();
    }

    @Test
    void findByUsername() {
        //given
        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode userJson=null;
        File file=new File("src/main/java/com/example/captchabackend2/testfolder/test-data.json");
        File file2=new File("src/main/java/com/example/captchabackend2/testfolder/test-data-result.json");
        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }

        //UserInfo user=UserInfo.builder().email("abc@gmail.com").username("abc").password("123").build();
        if(userJson.isArray()){
            for(JsonNode item:userJson){
                UserInfo user=UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                userRepo.save(user);
            }
        }
        if(userJson.isArray()){
            for(JsonNode item:userJson){
                String username=item.get("username").asText();


                //when
                UserInfo savedUser=userRepo.findByUsername(username);
                //then
                assertThat(savedUser).isNotNull();

                if (item instanceof ObjectNode) {
                    // Update the JSON field "test-case" to "true" for each object
                    ((ObjectNode) item).put("test-case", "true");
                }

            }
            try {
                objectMapper.writeValue(file2, userJson);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void findAllByUsername() {
    }
}