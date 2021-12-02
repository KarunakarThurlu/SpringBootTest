package com.app.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.model.Role;
import com.app.model.User;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {
	
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@BeforeEach
	void setUp() {
		User user=User.builder().roles(Set.of(new Role(1,"USER"))).userId(1).userEmail("test@gmail.com").userName("test").build();
		entityManager.persist(user);
		
	}

	@Test
	void testFindById() {
		User expected=userRepo.findById(1).get();
		assertEquals(expected.getUserEmail(),"test@gmail.com");
	}

}
