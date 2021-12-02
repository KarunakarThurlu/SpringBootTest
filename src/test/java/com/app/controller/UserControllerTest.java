package com.app.controller;

import com.app.constants.CommonConstants;
import com.app.exceptions.CustomException;
import com.app.pojos.UserDTO;
import com.app.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration
class UserControllerTest  {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@Test
	void saveUser() throws  Exception {
		UserDTO userDTO = UserDTO.builder().userName("user").userEmail("test@gmail.com").userPwd("test").build();
		when(userServiceImpl.saveUser(any(UserDTO.class))).thenReturn(userDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/signup")
				.content("{\n"
						+ "	\"userName\":\"tarun\",\n"
						+ "	\"userEmail\":\"tarun@gmail.com\",\n"
						+ "	\"userPwd\":\"t\",\n"
						+ "	\"userPhono\":\"77777776763\",\n"
						+ "	\"userGender\":\"MALE\",\n"
						+ "	\"roles\":[{\n"
						+ "		\"roleName\":\"USER\"\n"
						+ "	},{\n"
						+ "		\"roleName\":\"ADMIN\"\n"
						+ "	}]\n"
						+ "	\n"
						+ "}")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetUser() throws Exception {
		UserDTO userDTO = UserDTO.builder().userId(1).userName("user").userEmail("test@gmail.com").userPwd("test").build();
		when(userServiceImpl.findById(1)).thenReturn(userDTO);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/getuser/{id}",1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
				
	}
	
	@Test
	void testUserNotFound() throws Exception {
		UserDTO userDTO = UserDTO.builder().userName("user").userEmail("test@gmail.com").userPwd("test").build();
		doThrow(new CustomException(CommonConstants.NOT_FOUND,CommonConstants.USER_NOT_FOUND)).when(userServiceImpl.findById(userDTO.getUserId()));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/getuser/{id}",1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	void testDeleteUser() throws Exception {
		userServiceImpl.deleteUserById(1);
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/deleteuser/{id}",1)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
}