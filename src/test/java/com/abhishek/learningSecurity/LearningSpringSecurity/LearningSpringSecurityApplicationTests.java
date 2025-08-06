package com.abhishek.learningSecurity.LearningSpringSecurity;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.User;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.impl.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LearningSpringSecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		User user = new User(4L,"abhi@gmail.com","1234","Abhi");
		String token = jwtService.generateToken(user);
		System.out.println(token);

		Long id = jwtService.getUserIdFromToken(token);
		System.out.println(id);
	}

}
