package com.app.serviceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.SpringBootTestTests;
import com.app.model.Role;
import com.app.model.User;
import com.app.repo.UserRepo;

import pl.pojo.tester.api.assertion.Method;

@RunWith(SpringRunner.class)
class UserServiceImplTest extends SpringBootTestTests {
	

	@Autowired
	private UserServiceImpl userService;

	@MockBean
	private UserRepo userRepo;


	@Test
	void getAllusersTest(){
		when(userRepo.findAll()).thenReturn(List.of(new User(1,"Karun","karun@gmail.com","karun"),new User(2,"varun","varun@gmail.com","varun"),new User(3,"Tarun","Tarun@gmail.com","Tarun")));
		assertEquals(3,userService.findAllUsers().size());
	}

	@Test
	void findByUserEmailTest(){
		User user=new User(1,"Karun","karun@gmail.com","karun");
		when(userRepo.findByUserEmail("karun@gmail.com")).thenReturn(user);
		assertEquals(user,userService.findByUserEmail("karun@gmail.com"));
	}

	@Test
	void saveUserTest(){
		User user=new User(1,"Karun","karun@gmail.com","karun");
		user.setRoles(Set.of(new Role(1,Role.ROLES.USER.name())));
		when(userRepo.save(user)).thenReturn(user);
		assertEquals(user,userService.saveUser(user));
	}

	@Test
	void deleteUserById(){
		User user=new User(1,"Karun","karun@gmail.com","karun");
		userService.deleteUserById(user.getUserId());
		verify(userRepo,times(1)).deleteById(user.getUserId());
	}

	@Test
	void findByIdTest(){
		User user=new User(1,"Karun","karun@gmail.com","karun");
		when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
		Optional<User> optionalUser=userService.findById(user.getUserId());
		assertEquals(user,optionalUser.get());
	}
//	@Test
//	void findByIdNullTest(){
//		User user=new User(1,"Karun","karun@gmail.com","karun");
//		userService.findById(user.getUserId());
//		assertThrows(NoSuchElementException.class,()->userService.findById(null).get());
//	}



	@Test
	void testPojos() {
		final Class<?>[] classesUnderTest = {
				User.class,
				Role.class,
		};

		for (Class<?> classUnderTest : classesUnderTest) {
			assertPojoMethodsFor(classUnderTest).testing(Method.GETTER, Method.SETTER).areWellImplemented();
		}
	}

}