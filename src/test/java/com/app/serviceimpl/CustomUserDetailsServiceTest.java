package com.app.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.model.Role;
import com.app.model.User;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepo;

@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceTest  {

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserRepo userRepo;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@Mock
	private UserServiceImpl userService;

	private User testUser;


	@Test
	void testLoadUserByUsername_ValidUser() {
		testUser = new User();
		testUser.setUserEmail("test@example.com");
		testUser.setUserPwd("password123");
		Role role = new Role();
		role.setRoleName("USER");
		testUser.setRoles(Set.of(role));
		// Arrange
		String username = "test@example.com";

		// Mock the UserService's findByUserEmail method to return the testUser
		when(userRepo.findByUserEmail(any(String.class))).thenReturn(testUser);
		when(userService.findByUserEmail(any(String.class))).thenReturn(testUser);
		

		// Act
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

		// Assert
		assertNotNull(userDetails);
		assertEquals(username, userDetails.getUsername());
		assertEquals("password123", userDetails.getPassword());

		// Check if the user has the role "USER"
		Set<GrantedAuthority> authorities = new HashSet<>(userDetails.getAuthorities());
		assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
	}
	
	@Test
    void testLoadUserByUsername_InvalidUser() {
        // Arrange
        String username = "invalid@example.com";

        // Mock the UserService's findByUserEmail method to return null, simulating an invalid user
        when(userService.findByUserEmail(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }

}
