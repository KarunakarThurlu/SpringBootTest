package com.app.pojos;

import java.time.LocalDate;

public class UserDTO {
	
	private Integer userId;
	private String userName;
	private String userEmail;
	private String userPwd;
	private String userPhono;
	private String userGender;
	private String userLanguage;
	private LocalDate userDob;
	private String status;
	
	public UserDTO() {
		super();
	}

	public UserDTO(Integer userId, String userName, String userEmail, String userPwd, String userPhono,
			String userGender, String userLanguage, LocalDate userDob, String status) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPwd = userPwd;
		this.userPhono = userPhono;
		this.userGender = userGender;
		this.userLanguage = userLanguage;
		this.userDob = userDob;
		this.status = status;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserPhono() {
		return userPhono;
	}

	public void setUserPhono(String userPhono) {
		this.userPhono = userPhono;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserLanguage() {
		return userLanguage;
	}

	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public LocalDate getUserDob() {
		return userDob;
	}

	public void setUserDob(LocalDate userDob) {
		this.userDob = userDob;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
}
