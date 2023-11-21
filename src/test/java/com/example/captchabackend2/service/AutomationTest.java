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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class AutomationTest {
//    @Rule
//    public TemporaryFolder temporaryFolder = new TemporaryFolder();
@TempDir
File temporaryFolder;

    private CustomUserDetailService customUserDetailService;

    @Mock
    private UserRepo userRepo;

    private Path outputDirectory; // Move the variable here

    private Path outputFilePath;


    @BeforeEach
    void setUp() {
        customUserDetailService = new CustomUserDetailService(userRepo);
    }

    @AfterEach
    void cleanup() {
        if (temporaryFolder.exists()) {
            temporaryFolder.delete();
        }
    }

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Order(2)
    void saveUserDetailsTest2(@TempDir Path tempDir) throws IOException {
        Path inputFile = tempDir.resolve("input.json");
        Files.writeString(inputFile, "{\"username\": \"user1\", \"password\": \"pass1\", \"email\": \"user1@example.com\"}");

        // Specify the directory where you want to save the output file
        outputDirectory = Paths.get("src/main/java/com/example/captchabackend2/testfolder/");
//        Files.createDirectories(outputDirectory);  // Create the directory if it doesn't exist

        Path outputFile = outputDirectory.resolve("output.json");

        System.out.println("Absolute Path of output.json: " + outputFile.toAbsolutePath());



        try (InputStream inputStream = Files.newInputStream(inputFile)) {
            JsonNode userJson = objectMapper.readTree(inputStream);
            if (userJson.isArray()) {
                ArgumentCaptor<UserInfo> userInfoArgumentCaptor = ArgumentCaptor.forClass(UserInfo.class);
                for (JsonNode item : userJson) {
                    UserInfo user = UserInfo.builder()
                            .username(item.get("username").asText())
                            .password(item.get("password").asText())
                            .email(item.get("email").asText())
                            .build();
                    customUserDetailService.saveUserDetails(user);
                }



                verify(userRepo, times(userJson.size())).save(userInfoArgumentCaptor.capture());
                List<UserInfo> capturedUsers = userInfoArgumentCaptor.getAllValues();
                List<ObjectNode> objectNodes = new ArrayList<>();
                for (UserInfo item : capturedUsers) {
                    ObjectNode jsonNode2 = objectMapper.createObjectNode();
                    jsonNode2.putNull("id");
                    jsonNode2.put("username", item.getUsername());
                    jsonNode2.put("password", item.getPassword());
                    jsonNode2.put("email", item.getEmail());
                    objectNodes.add(jsonNode2);
                }


                for (int i = 0; i < userJson.size(); i++) {
                    String actualJson = objectNodes.get(i).toString();
                    String expectedJson = userJson.get(i).toString();
                    assertEquals(expectedJson, actualJson);
                    if (userJson.get(i) instanceof ObjectNode) {
                        ((ObjectNode) userJson.get(i)).put("save-user-detail", "true");
                    }
                }


                try (OutputStream outputStream = Files.newOutputStream(outputFile)) {
                    objectMapper.writeValue(outputStream, userJson);

                }
                System.out.println("Content of userJson: " + userJson.toString());

            }
        }
        outputFilePath = outputFile;
        System.out.println();

    }



    @Test
    @Order(1)
    void loginUser_WithValidCredentials_ShouldReturnUserId2() {
        try {
            // Call the saveUserDetailsTest2 method
            //saveUserDetailsTest2(tempDir);
            outputDirectory = Paths.get("src/main/java/com/example/captchabackend2/testfolder/");

            outputFilePath = outputDirectory.resolve("output.json");

            // Read input credentials from the file
            if (outputFilePath != null && Files.exists(outputFilePath)) {
                JsonNode outputCredentialsJson = objectMapper.readTree(outputFilePath.toFile());


                // Mock the behavior of userRepo.findByUsername for each set of input credentials
                for (JsonNode credentials : outputCredentialsJson) {
                    String username = credentials.get("username").asText();
                    String password = credentials.get("password").asText();

                   // String email = credentials.has("email") ? credentials.get("email").asText() : "";
                    UserInfo user = UserInfo.builder()
                            .username(username)
                            .password(password)
                           // .email(email)
                            .build();
                    when(userRepo.findByUsername(username)).thenReturn(user);

                    // Call the loginUser method
                    String result = customUserDetailService.loginUser(username, password);

                    // Verify that userRepo.findByUsername was called with the correct username
                    verify(userRepo).findByUsername(username);

                    // Assert that the result is the user's ID as a string
                    assertEquals(String.valueOf(user.getId()), result);
                }
            }
            else {
                    fail("Output file path is not set or does not exist");
                }

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }


}
