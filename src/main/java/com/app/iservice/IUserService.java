package com.app.iservice;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.app.model.User;

public interface IUserService {

	public User saveUser(User u);
	public User findByUserEmail(String email);
	public void deleteUserById(Integer userId);
	public List<User> findAllUsers();
	public Optional<User> findById(Integer userId);
	public String sendMail();
	public String uploadFile(MultipartFile file);
	public User updateUser(User user);

	List<User> searchUser(String searchKey);
}
