package com.app.pojos;

import java.time.LocalDate;
import java.util.Set;

import com.app.model.Company;
import com.app.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	private Set<Role> roles;
	private Company company;
	private byte[] userProfile;
	
	
}
