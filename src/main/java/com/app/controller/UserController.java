package com.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.constants.CommonConstants;
import com.app.iservice.IUserService;
import com.app.model.Response;
import com.app.model.User;
import com.app.pojos.LoginPojo;
import com.app.pojos.UserDTO;
import com.app.securityconfig.JwtConstants;
import com.app.securityconfig.JwtUtil;
import com.app.serviceimpl.GetUserDetailsUtil;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private GetUserDetailsUtil getUserDetails;

	@PostMapping(value = "/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginPojo user) {
		String message = "Messasge";
		Map<String, String> m = new HashMap<>();
		try {
			final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final String token = jwtTokenUtil.generateToken(authentication);
			m.put("AuthToken", token);
			m.put(message, CommonConstants.LOGIN_SUCCESS);
			return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
		} catch (BadCredentialsException e) {
			m.put(message, CommonConstants.AUTH_FAIL);
			logger.error("Authentication fails for : {}",user.getEmail());
			return new ResponseEntity<>(m, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			m.put(message, e.getMessage());
			return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/signup")
	public Response<UserDTO> saveUser(@RequestBody UserDTO user) {
		User userExists = userService.findByUserEmail(user.getUserEmail());
		if (userExists != null) {
			return new Response<>(user, "User Already exists with this " + user.getUserEmail() + " email", HttpStatus.BAD_REQUEST);
		} else {
			user.setUserId(null);
			user.setStatus(User.STATUS.ACTIVE.name());
			UserDTO savedUser = userService.saveUser(user);
			return new Response<>(savedUser, "User Saved Successfully", HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/getuser/{id}")
	public Response<UserDTO> getUserById(@PathVariable("id") int userId) {
		UserDTO userDTO =  userService.findById(userId);
		return new Response<>(userDTO, "OK", HttpStatus.OK);
	}

	@GetMapping("/getallusers")
	public Response<List<UserDTO>> getAllUsers() {
		List<UserDTO> users = userService.findAllUsers();
		return new Response<>(users, "OK", HttpStatus.OK);
	}

	@DeleteMapping("/deleteuser/{id}")
	public Response<UserDTO> deleteUserById(@PathVariable("id") Integer userId) {
		UserDTO user = userService.findById(userId);
		if (user != null)
			userService.deleteUserById(userId);
		return new Response<>(null, "User Deleted Successfully", HttpStatus.OK);
	}

	@PutMapping("/updateuser")
	public Response<UserDTO> updateUser(@RequestBody UserDTO user) {
		logger.info("udating user");
		if (user.getUserId() != null) {
			UserDTO updatedUser = userService.updateUser(user);
			return new Response<>(updatedUser, "User Details Updated Successfully", HttpStatus.OK);
		}
		return new Response<>(null, "UserId is required for updating user.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/searchuser")
	public Response<List<UserDTO>> searchUser(@RequestParam String searchKey) {
		List<UserDTO> users = userService.searchUser(searchKey);
		if (users.isEmpty())
			return new Response<>(null, "Users Not Found", HttpStatus.BAD_REQUEST);
		else
			return new Response<>(users, "OK", HttpStatus.OK);
	}

	@PostMapping("/uploadprofile")
	public Response<String> uploadFile(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "file") MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			User user=getUserDetails.getUserDetailsFromAuthToken(request.getHeader(JwtConstants.HEADER_STRING));
			UserDTO userDTO=new UserDTO();
			userDTO.setUserProfile(file.getBytes());
			userDTO.setUserId(user.getUserId());
			String result = userService.uploadFile(userDTO);
			return new Response<>(null, result, HttpStatus.OK);
		} else {
			return new Response<>(null, "Please select file", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getprofile/{userId}")
	public ResponseEntity<byte[]> uploadFile(@PathVariable Integer userId) {
		if (userId != null ) {
			UserDTO userDto = userService.findById(userId);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(userDto.getUserProfile());
		} else {
			return null;
		}
	}

}
