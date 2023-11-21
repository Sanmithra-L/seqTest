package com.example.captchabackend2;

import com.example.captchabackend2.entity.UserInfo;
import com.example.captchabackend2.repo.UserRepo;
import com.example.captchabackend2.service.CustomUserDetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class CaptchaBackend2ApplicationTests {

	@Test
	void contextLoads() {
	}
//	@Mock
//	private UserRepo userRepo;
//	@Test
//	public void UserRepo_FindByUsername_ReturnUserInfo() {
//		// Create a user and save it to the database
//		UserInfo user = UserInfo.builder()
//				.username("dump")
//				.email("dump@gmail.com")
//				.captcha("abcdef")
//				.password("1234")
//				.build();
//		UserInfo saveUser=userRepo.save(user);
//
//		// Retrieve the user by username
//		List<UserInfo> savedUser = userRepo.findAllByUsername("dump");
//		System.out.println("save: " + saveUser);
//		System.out.println("savedUser: " + savedUser);
//
//		// Assert that the savedUser is not null
//		for (UserInfo userInfo : savedUser) {
//			System.out.println(userInfo);
//		}
//		assertNotNull(savedUser, "User with username 'dump' not found in the database.");
//
//	}
//	@InjectMocks
//	private CustomUserDetailService customUserDetailService;
//	@Test
//	public void CustomUserDetailSevice_SaveUserDetails_ReturnUserDetails(){
//		UserInfo userInfo=UserInfo.builder().username("dump2").password("12345").email("dump2@gmail.com").build();
//		when(userRepo.save(Mockito.any(UserInfo.class))).thenReturn(userInfo);
//		UserInfo savedUser=customUserDetailService.saveUserDetails(userInfo);
//		Assertions.assertNotNull(savedUser);
//	}

}
