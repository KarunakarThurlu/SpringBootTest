package com.app.serviceimpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.model.User;
import com.app.securityconfig.JwtUtil;

@ExtendWith(SpringExtension.class)
class GetUserDetailsUtilTest {

	@Mock
	private UserServiceImpl userService;

	@Mock
	private JwtUtil jwtTokenUtil;
	
	@InjectMocks
	private GetUserDetailsUtil getUserDetailsUtil;
	
	@Test
	void getUserDetailsFromAuthToken() {
		User user= User.builder().userId(1).userName("test").userEmail("test@gmail.com").build();
		when(jwtTokenUtil.getUsernameFromToken(any(String.class))).thenReturn("test@gmail.com");
		when(userService.getUserForAuthCheck(any(String.class))).thenReturn(user);
		User actual = getUserDetailsUtil.getUserDetailsFromAuthToken("test@gmail.com");
		assertNotNull(actual);
		assertEquals(user.getUserEmail(), actual.getUserEmail());
	}

}
