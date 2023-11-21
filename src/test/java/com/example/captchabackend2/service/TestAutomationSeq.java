package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAutomationSeq {

    @Mock
    private UserRepo userRepo;

    private CustomUserDetailService customUserDetailService;
    Integer[] result;


    @BeforeEach
    void setUp() {
        customUserDetailService = new CustomUserDetailService(userRepo);

    }

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode userJson = null;
    File file = new File("src/main/java/com/example/captchabackend2/testfolder/test-data.json");
    File outFile = new File("src/main/java/com/example/captchabackend2/testfolder/output.json");


    @Test
    @Order(1)
    void saveUserDetails() throws JsonProcessingException {
        try {
            userJson = objectMapper.readTree(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userJson.isArray()) {
            ArgumentCaptor<UserInfo> userInfoArgumentCaptor = ArgumentCaptor.forClass(UserInfo.class);
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                customUserDetailService.saveUserDetails(user);

            }

            verify(userRepo, times(userJson.size())).save(userInfoArgumentCaptor.capture());
            List<UserInfo> capturedUsers = userInfoArgumentCaptor.getAllValues();
            ObjectMapper objectMapper2 = new ObjectMapper();
            List<ObjectNode> objectNodes = new ArrayList<>();
            for (UserInfo item : capturedUsers) {
                ObjectNode jsonNode2 = objectMapper.createObjectNode();
                jsonNode2.put("id", item.getId());
                jsonNode2.put("username", item.getUsername());
                jsonNode2.put("password", item.getPassword());  // Corrected from item.getUsername()
                jsonNode2.put("email", item.getEmail());
                objectNodes.add(jsonNode2);
            }

            // Now, you can assert each captured user in the loop if needed
            for (int i = 0; i < userJson.size(); i++) {


                String actualJson = objectNodes.get(i).toString();
                String expectedJson = userJson.get(i).toString();
                assertEquals(expectedJson, actualJson);
                if (userJson.get(i) instanceof ObjectNode) {
                    // Update the JSON field "test-case" to "true" for each object
                    //((ObjectNode) userJson.get(i)).put("save-user-detail","true");
                }
            }
            try {
                objectMapper.writeValue(outFile, userJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Test
    @Order(2)
    void loginUser() {
        try {
            String fileContents = Files.readString(outFile.toPath());
            System.out.println("File Contents: " + fileContents);

            if (fileContents.isEmpty()) {
                // Fail the test explicitly if the file is empty
                Assertions.fail("File is empty");
            }

            userJson = objectMapper.readTree(fileContents);

            if (userJson.isArray()) {
                for (JsonNode item : userJson) {
                    UserInfo user = UserInfo.builder()
                            .username(item.get("username").asText())
                            .password(item.get("password").asText())
                            .email(item.get("email").asText())
                            .build();
                    when(userRepo.findByUsername(item.get("username").asText())).thenReturn(user);
                    UserInfo testUser = customUserDetailService.loadIdbyUsername(item.get("username").asText());
                    verify(userRepo).findByUsername(item.get("username").asText());
                    assertThat(testUser).isEqualTo(user);
                }
            } else {
                // Fail the test explicitly if userJson is not an array
                Assertions.fail("Invalid JSON structure: expected JSON array");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Error reading file: " + e.getMessage());
        }
        eraseOutputFileContents();

    }

    private void eraseOutputFileContents() {
        try {
            Files.write(outFile.toPath(), new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("Error erasing file contents: " + e.getMessage());
        }

    }
}
