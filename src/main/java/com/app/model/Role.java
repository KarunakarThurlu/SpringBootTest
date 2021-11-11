package com.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="Roles_table")
public class Role  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static enum ROLES{
		USER,ADMIN,SUPERADMIN
	}
	@Id
	@GeneratedValue
	private Integer roleId;
	
	
	private String roleName;
	
	public Role() {
		super();
	}
	public Role(Integer roleId, String roleName) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", getRoleId()=" + getRoleId() + ", getRoleName()="
				+ getRoleName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}

