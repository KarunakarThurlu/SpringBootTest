package com.app.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.app.constants.CommonConstants;
import com.app.model.User;
import com.app.pojos.LoginPojo;
import com.app.pojos.UserDTO;
import com.app.securityconfig.JwtAuthenticationFilter;
import com.app.securityconfig.JwtConstants;
import com.app.securityconfig.JwtUtil;
import com.app.serviceimpl.CustomUserDetailsService;
import com.app.serviceimpl.GetUserDetailsUtil;
import com.app.serviceimpl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@InjectMocks
	private UserController userController;

	@MockBean
	private AuthenticationManager authenticationManager;;

	@MockBean
	private GetUserDetailsUtil getUserDetailsUtil;

	@MockBean
	private JwtUtil jwtTokenUtil;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	
	@Mock
	private HttpServletRequest httpServletRequest;
	
	@Mock
	private HttpServletResponse httpServletResponse;

	private UserDTO userOne;
	private UserDTO userTwo;

	@BeforeEach
	void init() {
		userOne = UserDTO.builder().userId(1).userName("test").userEmail("test@gmail.com").userPwd("test").userProfile(new byte[] {1}).build();
		userTwo = UserDTO.builder().userName("foo").userEmail("foo@gmail.com").userPwd("foo").build();
	}

	@Test
	@DisplayName("Login")
	void testLogin() throws JsonProcessingException, Exception {
		//Login Success
		LoginPojo loginPojo = new LoginPojo("test@example.com", "password123");

		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
		when(jwtTokenUtil.generateToken(any(Authentication.class))).thenReturn("abc123");
		
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginPojo)))
				.andDo(print())
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.message", is(CommonConstants.LOGIN_SUCCESS)));
		
		
		//Invalid UserName & Password
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginPojo)))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message", is(CommonConstants.AUTH_FAIL)));
		
		//Runtime Exception
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Unknown Error"));
		mockMvc.perform(post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginPojo)))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Save User")
	void testSaveUser() throws Exception {
		when(userServiceImpl.saveUser(any(UserDTO.class))).thenReturn(userOne);
		String userDTOJson = objectMapper.writeValueAsString(userOne);
		mockMvc.perform(post("/api/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userDTOJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("User Saved Successfully")));
		
		when(userServiceImpl.saveUser(any(UserDTO.class))).thenReturn(userTwo);
		when(userServiceImpl.findByUserEmail(any(String.class))).thenReturn(new User());
		mockMvc.perform(post("/api/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content( objectMapper.writeValueAsString(userTwo)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("User Already exists with this foo@gmail.com email")));
	}

	@Test
	@DisplayName("Update User")
	void testUpdateUser() throws Exception {
		when(userServiceImpl.updateUser(any(UserDTO.class))).thenReturn(userTwo);
		mockMvc.perform(put("/api/updateuser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userOne)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("User Details Updated Successfully")));
		
		when(userServiceImpl.updateUser(any(UserDTO.class))).thenReturn(userTwo);
		mockMvc.perform(put("/api/updateuser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userTwo)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message", is("UserId is required for updating user.")));
	}

	@Test
	@DisplayName("Get User")
	void testGetUser() throws Exception {
		UserDTO userTwo = UserDTO.builder().userId(3).userName("foo").userEmail("foo@gmail.com").userPwd("foo").build();
		when(userServiceImpl.findById(any(Integer.class))).thenReturn(userTwo);
		mockMvc.perform(get("/api/getuser/{id}", userTwo.getUserId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.userName", is("foo")));
	}

	@Test
	@DisplayName("Get All Users")
	void testGetAllUsers() throws Exception {
		when(userServiceImpl.findAllUsers()).thenReturn(List.of(userOne, userTwo));
		mockMvc.perform(get("/api/getallusers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].userName").value("test"));
	}

	@Test
	@DisplayName("Delete User")
	void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/api/deleteuser/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
		verify(userServiceImpl, times(1)).deleteUserById(1);
	}
	
	@Test
	@DisplayName("Search User")
	void testSearchUser() throws Exception {
		when(userServiceImpl.searchUser(any(String.class))).thenReturn(List.of(userOne,userTwo));
		mockMvc.perform(get("/api/searchuser")
				.queryParam("searchKey", "foo"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].userName").value("test"));
		
		when(userServiceImpl.searchUser(any(String.class))).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/api/searchuser")
				.queryParam("searchKey", "foo"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Users Not Found"));
	}
	
	@Test
	@DisplayName("Get User Profile")
	void testGetProfilePhoto() throws Exception {
		when(userServiceImpl.findById(any(Integer.class))).thenReturn(userOne);
		mockMvc.perform(get("/api/getprofile/{userId}", userOne.getUserId()))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/api/getprofile/{userId}",userTwo.getUserId()))
		.andDo(print())
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Upload Profile Photo")
	void testUploadProfilePhoto() throws Exception {
		byte[] fileContent = "Sample file content".getBytes();

        MockMultipartFile multipartFile = new MockMultipartFile("file","filename.txt","text/plain",fileContent);
        User user = new User();
        user.setUserId(1);
        user.setUserName("Foo");

        when(getUserDetailsUtil.getUserDetailsFromAuthToken(any(String.class))).thenReturn(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserProfile(fileContent);
        userDTO.setUserId(user.getUserId());


        when(userServiceImpl.uploadFile(any(UserDTO.class))).thenReturn("Upload success!");
        mockMvc.perform(multipart("/api/uploadprofile")
                .file(multipartFile)
                .header(JwtConstants.HEADER_STRING, "abc123"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Upload success!")));
        
        MockMultipartFile emptyMultipartFile = new MockMultipartFile("file","filename.txt","text/plain",new byte[] {0});
        when(userServiceImpl.uploadFile(any(UserDTO.class))).thenReturn("Please select file");
        mockMvc.perform(multipart("/api/uploadprofile")
        		.file(emptyMultipartFile)
                .header(JwtConstants.HEADER_STRING, "abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Please select file")));
	}
}