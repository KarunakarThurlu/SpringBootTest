package com.app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="User_Table")
public class User  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum STATUS{
		ACTIVE,INACTIVE,INVITED
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userName;
	private String userEmail;
	private String userPwd;
	private String userPhono;
	private String userGender;
	private String userLanguage;
	private LocalDate userDob;
	private String status;
	
	@Lob
	private byte[] userProfile;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Company company;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name="User_Roles_Table",
	           joinColumns = { @JoinColumn(name="userId")},
	           inverseJoinColumns = {@JoinColumn(name="roleId")}
	          )
	private Set<Role> roles;


	
}
