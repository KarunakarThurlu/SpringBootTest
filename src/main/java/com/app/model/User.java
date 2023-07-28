package com.app.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
