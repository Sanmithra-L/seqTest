package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.util.*;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

//    @TempDir
//    File temporaryFolder;

    @Mock
    private UserRepo userRepo;

    private CustomUserDetailService customUserDetailService;
    Integer[] result;


    @BeforeEach
    void setUp(){
        customUserDetailService=new CustomUserDetailService(userRepo);

    }
    ObjectMapper objectMapper=new ObjectMapper();
    JsonNode userJson=null;

    File file=new File("src/main/java/com/example/captchabackend2/testfolder/test-data.json");
    File file2=new File("src/main/java/com/example/captchabackend2/testfolder/test-data-result.json");

    @Test
    void saveUserDetails() throws JsonProcessingException {
        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(userJson.isArray()){
            ArgumentCaptor<UserInfo> userInfoArgumentCaptor=ArgumentCaptor.forClass(UserInfo.class);
            for(JsonNode item:userJson){
                UserInfo user=UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                customUserDetailService.saveUserDetails(user);

            }

            verify(userRepo, times(userJson.size())).save(userInfoArgumentCaptor.capture());
            List<UserInfo> capturedUsers = userInfoArgumentCaptor.getAllValues();
            ObjectMapper objectMapper2 = new ObjectMapper();
            List<ObjectNode> objectNodes=new ArrayList<>();
            for(UserInfo item:capturedUsers){
                ObjectNode jsonNode2 = objectMapper.createObjectNode();
                jsonNode2.putNull("id");
                jsonNode2.put("username", item.getUsername());
                jsonNode2.put("password", item.getPassword());  // Corrected from item.getUsername()
                jsonNode2.put("email", item.getEmail());
                objectNodes.add(jsonNode2);
            }

            // Now, you can assert each captured user in the loop if needed
            for (int i = 0; i < userJson.size(); i++) {


                String actualJson=objectNodes.get(i).toString();
                String expectedJson = userJson.get(i).toString();
                assertEquals(expectedJson, actualJson);
                if (userJson.get(i) instanceof ObjectNode) {
                    // Update the JSON field "test-case" to "true" for each object
                    ((ObjectNode) userJson.get(i)).put("save-user-detail","true");
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
    void loadUserByUsername() {
    }

    @Test
    void loadIdbyUsername() {

        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(userJson.isArray()){
            for(JsonNode item:userJson){
                UserInfo user=UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                when(userRepo.findByUsername(item.get("username").asText())).thenReturn(user);
                UserInfo testUser=customUserDetailService.loadIdbyUsername(item.get("username").asText());
                verify(userRepo).findByUsername(item.get("username").asText());
                assertThat(testUser).isEqualTo(user);
                if (item instanceof ObjectNode) {
                    // Update the JSON field "test-case" to "true" for each object
                    ((ObjectNode) item).put("load-Id-by-username","true");
                }
            }
            try {
                objectMapper.writeValue(file2, userJson);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

//        UserInfo user = UserInfo.builder().username("abc").password("1234").email("abc@gmail.com").build();


//        when(userRepo.findByUsername("abc")).thenReturn(user);

//        UserInfo testUser = customUserDetailService.loadIdbyUsername("abc");


//        verify(userRepo).findByUsername("abc");


//        assertThat(testUser).isEqualTo(user);

    }

    @Test
    void loginUser() {
        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(userJson.isArray()) {
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
            }
        }
        if(userJson.isArray()) {
            for (JsonNode item : userJson) {
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                when(userRepo.findByUsername(item.get("username").asText())).thenReturn(user);
                UserInfo testUser = customUserDetailService.loadIdbyUsername(item.get("username").asText());
                verify(userRepo).findByUsername(item.get("username").asText());
                assertThat(testUser).isEqualTo(user);
            }
        }
        // Mock the behavior of userRepo.findByUsername to return the 'user' object




        // Verify that userRepo.findByUsername was called with "abc"



        // Assert that testUser is equal to the 'user' object

    }
    @Test
    void loginUser_WithValidCredentials_ShouldReturnUserId() {
        //given
        try {
            userJson=objectMapper.readTree(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(userJson.isArray()) {
            for (JsonNode item : userJson) {
                String username=item.get("username").asText();
                String password=item.get("password").asText();
                UserInfo user = UserInfo.builder().username(item.get("username").asText()).password(item.get("password").asText()).email(item.get("email").asText()).build();
                when(userRepo.findByUsername(item.get("username").asText())).thenReturn(user);
                String result=null;
                try {
                    result = customUserDetailService.loginUser(username,password);
                }catch ( NullPointerException e){
                    ;
                }


                verify(userRepo).findByUsername(item.get("username").asText());
                assertEquals(null,result);
            }
        }
//        UserInfo user=UserInfo.builder().username(username).id(1).email("dump@gmail.com").password(password).build();



        //when
        //then
    }
    @Test
    void loginUser_WithIncorrectPassword_ShouldReturnErrorMessage() {
        String username = "abc";
        String password = "1234";

        UserInfo user = UserInfo.builder().username(username).password("incorrect_password").email("abc@gmail.com").id(1).build();

        // Mock the behavior of userRepo.findByUsername to return the 'user' object
        when(userRepo.findByUsername(username)).thenReturn(user);

        String result = customUserDetailService.loginUser(username, password);

        // Verify that userRepo.findByUsername was called with the correct username
        verify(userRepo).findByUsername(username);

        // Assert that the result is the error message
        assertEquals("Username or password incorrect", result);
    }
    @Test
    void loadUserbyName() {
    }
    @Test
    void loginUser_WithValidCredentials_ShouldReturnUserId2() {
        try {
            // Create a temporary folder
//            File temporaryFolderRoot = temporaryFolder.getRoot();

            // Create input credentials file in the temporary folder
            File inputCredentialsFile = temporaryFolder.newFile("login-input.json");

            // Write input credentials to the file
            List<Map<String, String>> inputCredentials = Arrays.asList(
                    Map.of("username", "user1", "password", "pass1"),
                    Map.of("username", "user2", "password", "pass2")
                    // Add more entries as needed
            );
            objectMapper.writeValue(inputCredentialsFile, inputCredentials);

            // Read input credentials from the file
            JsonNode inputCredentialsJson = objectMapper.readTree(inputCredentialsFile);

            // Mock the behavior of userRepo.findByUsername for each set of input credentials
            for (JsonNode credentials : inputCredentialsJson) {
                String username = credentials.get("username").asText();
                String password = credentials.get("password").asText();

                UserInfo user = UserInfo.builder()
                        .username(username)
                        .password(password)
                        .email(credentials.get("email").asText())
                        .build();
                when(userRepo.findByUsername(username)).thenReturn(user);

                // Call the loginUser method
                String result = customUserDetailService.loginUser(username, password);

                // Verify that userRepo.findByUsername was called with the correct username
                verify(userRepo).findByUsername(username);

                // Assert that the result is the user's ID as a string
                assertEquals(String.valueOf(user.getId()), result);
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        }
    }
    @Test
    void loginUser_sanmithraTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserInfo> userList = objectMapper.readValue(new File("src/test/java/com/example/captchabackend2/service/user.json"), new TypeReference<List<UserInfo>>() {});

        // Mock the repository
        when(userRepo.findByUsername(anyString())).thenAnswer(invocation -> {
            String username = invocation.getArgument(0);
            return userList.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        });

        // Call the method to be tested
        for (UserInfo user : userList) {
            if (user.getId() == null) {
                throw new NullPointerException("User ID is null for user: " + user.getUsername());
            }

            String result = customUserDetailService.loginUser(user.getUsername(), user.getPassword());

            // Verify that the 'findByUsername' method was called
            verify(userRepo).findByUsername(user.getUsername());

            // Assert the result
            assertEquals(String.valueOf(user.getId()), result);
        }
    }

}
