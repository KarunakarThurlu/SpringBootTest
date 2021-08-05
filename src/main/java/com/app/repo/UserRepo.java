package com.app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Integer> {

	void save(Optional<User> u);
	
	//@Cacheable
	public User findByUserEmail(String email);

	@Query(value = "SELECT ut.* FROM user_table ut WHERE ut.user_name LIKE CONCAT('%',:key,'%')  OR ut.user_email LIKE CONCAT('%',:key,'%') OR ut.user_language LIKE CONCAT('%',:key,'%') OR ut.user_phono  LIKE CONCAT('%',:key,'%')  OR ut.user_gender  ILIKE CONCAT('%',:key,'%')   limit 10 offset 0 ", nativeQuery = true)
    List<User> getUsersByDynamicSearch(@Param("key") String searchKey);
}
