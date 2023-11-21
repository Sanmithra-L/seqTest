package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.util.*;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationTest2 {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp() {
        customUserDetailService = new CustomUserDetailService(userRepo);
        MockitoAnnotations.openMocks(this);

    }

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode userJson = null;

    File file = new File("src/main/java/com/example/captchabackend2/testfolder/test-data.json");
    File file2 = new File("src/main/java/com/example/captchabackend2/testfolder/test-data-result.json");


    @Test
    @Order(3)
    void saveUserDetailsTest() throws JsonProcessingException {

        try {
            userJson = objectMapper.readTree(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userJson.isArray()) {
            ArgumentCaptor<UserInfo> userInfoArgumentCaptor = ArgumentCaptor.forClass(UserInfo.class);

            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder()
                        .username(item.get("username").asText())
                        .password(item.get("password").asText())
                        .email(item.get("email").asText())
                        .build();

                // Mock the save method of the repository
                customUserDetailService.saveUserDetails(user);

                // Update the JSON file with the "save-user-detail" field
                if (item instanceof ObjectNode) {
                    ((ObjectNode) item).put("save-user-detail", "true");
                }
            }

            // Verify that the save method was called with the correct user (outside the loop)
            verify(userRepo, times(userJson.size())).save(userInfoArgumentCaptor.capture());

            // Iterate over captured users and perform assertions if needed
            List<UserInfo> capturedUsers = userInfoArgumentCaptor.getAllValues();
            for (int i = 0; i < userJson.size(); i++) {
                UserInfo capturedUser = capturedUsers.get(i);
                JsonNode item = userJson.get(i);

                // Assert that the saved user matches the expected user
                assertEquals(item.get("username").asText(), capturedUser.getUsername());
                assertEquals(item.get("password").asText(), capturedUser.getPassword());
                assertEquals(item.get("email").asText(), capturedUser.getEmail());
            }

            try {
                objectMapper.writeValue(file2, userJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    @Order(1)
    void loginUserTest() {

        try {
            userJson = objectMapper.readTree(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userJson.isArray()) {
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder()
                        .username(item.get("username").asText())
                        .password(item.get("password").asText())
                        .email(item.get("email").asText())
                        .build();

                // Debug statement to check user details
                System.out.println("User: " + user);

                // Mock the repository to return a user when findByUsername is called
                when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

                // Perform login
                UserInfo foundUser = userRepo.findByUsername(user.getUsername());

                // Debug statement to check foundUser details
                System.out.println("Found User: " + foundUser);

                // Verify repository method call
                verify(userRepo).findByUsername(user.getUsername());

                // Assert the login result
                assertNotNull(foundUser, "User not found in the repository");

                // Debug statement to check user.getId() value
                System.out.println("User ID: " + user.getId());

                assertNull(foundUser.getId(), "User ID should be null");
            }
        }
    }


    @Test
    @Order(2)
    void loginUser() {
        try {
            userJson = objectMapper.readTree(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userJson.isArray()) {
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
            }
        }
        if (userJson.isArray()) {
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                when(userRepo.findByUsername(item.get("username").asText())).thenReturn(user);
                UserInfo testUser = customUserDetailService.loadIdbyUsername(item.get("username").asText());
                verify(userRepo).findByUsername(item.get("username").asText());
                assertThat(testUser).isEqualTo(user);
            }
        }

    }
}
