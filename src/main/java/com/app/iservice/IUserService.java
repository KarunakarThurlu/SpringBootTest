package com.app.iservice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.model.User;
import com.app.pojos.UserDTO;

public interface IUserService {
	User getUserForAuthCheck(String email);
	UserDTO saveUser(UserDTO u);
	User findByUserEmail(String email);
	void deleteUserById(Integer userId);
	List<UserDTO> findAllUsers();
	UserDTO findById(Integer userId);
	String sendMail();
	String uploadFile(MultipartFile file);
	UserDTO updateUser(UserDTO user);
	List<UserDTO> searchUser(String searchKey);
	String uploadFile(UserDTO userDTO);
}
