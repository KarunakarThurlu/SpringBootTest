package com.app.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.iservice.IUserService;
import com.app.model.User;
import com.app.securityconfig.JwtConstants;
import com.app.securityconfig.JwtUtil;

@Component
public class GetUserDetailsUtil {

	@Autowired
	private IUserService userService;

	@Autowired
	private JwtUtil jwtTokenUtil;

	public User getUserDetailsFromAuthToken(String token) {
		String actualToken = token.replace(JwtConstants.TOKEN_PREFIX, "");
		String userEmail = jwtTokenUtil.getUsernameFromToken(actualToken);
		return userService.getUserForAuthCheck(userEmail);
	};
}
