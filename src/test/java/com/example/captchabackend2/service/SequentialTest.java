package com.example.captchabackend2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SequentialTest {

    @Test
    public static void main(String[]args) {
        AutomationTest a = new AutomationTest();


        try {
            Path tempDir = Files.createTempDirectory("tempDirPrefix");
            a.saveUserDetailsTest2(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        a.loginUser_WithValidCredentials_ShouldReturnUserId2();

    }

}
