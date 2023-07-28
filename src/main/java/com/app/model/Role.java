package com.app.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name="Roles_table")
@AllArgsConstructor
@NoArgsConstructor
public class Role  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public  enum ROLES{
		USER,ADMIN,SUPERADMIN
	}
	
	@Id
	@GeneratedValue
	private Integer roleId;
	private String roleName;

}

