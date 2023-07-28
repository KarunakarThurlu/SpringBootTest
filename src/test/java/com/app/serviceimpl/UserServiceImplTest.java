package com.app.serviceimpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.constants.CommonConstants;
import com.app.exceptions.CustomException;
import com.app.model.Role;
import com.app.model.User;
import com.app.pojos.UserDTO;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepo;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void testSaveUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserEmail("test@gmail.com");
        userDTO.setUserName("test");
        userDTO.setRoles(Set.of(new Role(1,"ADMIN")));
        userDTO.setUserPwd(bCryptPasswordEncoder.encode("test"));
        when(userRepo.save(ArgumentMatchers.any(User.class))).thenReturn(convertUserDtoToUserEntity.apply(userDTO));
        UserDTO savedUser= userServiceImpl.saveUser(userDTO);
        assertEquals(userDTO.getUserEmail(),savedUser.getUserEmail());
    }

    @Test
    void testGetUser() {
        User userDTO = new User();
        userDTO.setUserId(1);
        userDTO.setUserEmail("test@gmail.com");
        userDTO.setUserName("test");
        userDTO.setUserPwd("test");
        when(userRepo.findById(userDTO.getUserId())).thenReturn(Optional.of(userDTO));
        UserDTO expected=userServiceImpl.findById(userDTO.getUserId());
        assertEquals(expected.getUserEmail(),userDTO.getUserEmail());
        verify(userRepo).findById(1);
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUserEmail("test@gmail.com");
        userDTO.setUserName("test");
        userDTO.setUserPwd("test");
        userDTO.setRoles(Set.of(new Role(1,"USER")));
        when(userRepo.findById(userDTO.getUserId())).thenReturn(Optional.of(convertUserDtoToUserEntity.apply(userDTO)));
        when(userRepo.save(any(User.class))).thenReturn(convertUserDtoToUserEntity.apply(userDTO));
        UserDTO expected=userServiceImpl.updateUser(userDTO);
        assertEquals(expected.getUserEmail(),userDTO.getUserEmail());
        verify(userRepo).findById(1);
    
    }
    
    @Test
     void testUserNotFound(){
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(CustomException.class,()->userServiceImpl.findById(90));
    }

    @Test
    void testDeleteUser() {   
        Integer userIdToDelete = 1;
        User userToDelete = User.builder().userId(userIdToDelete).userName("foo").build();
        when(userRepo.findById(userIdToDelete)).thenReturn(Optional.of(userToDelete));
        String result = userServiceImpl.deleteUserById(userIdToDelete);
        assertEquals(CommonConstants.USER_DELETED, result);
        verify(userRepo, times(1)).findById(userIdToDelete);
        verify(userRepo, times(1)).deleteById(userIdToDelete);
        
        when(userRepo.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> userServiceImpl.deleteUserById(2));
        verify(userRepo, times(1)).findById(2);
    }

    @Test
	void testGetAllUsers() {
		List<UserDTO> users = List.of(
				UserDTO.builder().userEmail("karun@gmail.com").userGender("MALE").userPhono("980745").build(),
				UserDTO.builder().userEmail("varun@gmail.com").userGender("MALE").userPhono("97880745").build());
		Mockito.when(userRepo.findAll())
				.thenReturn(users.stream().map(u -> convertUserDtoToUserEntity.apply(u)).collect(Collectors.toList()));
		List<UserDTO> expected = userServiceImpl.findAllUsers();
		assertEquals(users.size(), expected.size());
	}
    
    @Test
    void testSearchUser() {
    	User user = User.builder().userId(1).userName("test").userEmail("test@gmail.com").build();
		when(userRepo.getUsersByDynamicSearch(any(String.class))).thenReturn(List.of(user));
		List<UserDTO> expected = userServiceImpl.searchUser("test");
		assertEquals(1, expected.size());
    }
    
    @Test
	void testuploadFile() {
		User user = User.builder().userId(1).userName("test").userEmail("test@gmail.com").build();
		when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
		userServiceImpl.uploadFile(convertUserEntityIntoUserDTO.apply(user));
		verify(userRepo).findById(1);
	}

    Function<UserDTO,User> convertUserDtoToUserEntity = dto -> {
        User userVO=new User();
        userVO.setUserId(dto.getUserId());
        userVO.setStatus(dto.getStatus());
        userVO.setUserName(dto.getUserName());
        userVO.setUserEmail(dto.getUserEmail());
        userVO.setUserPhono(dto.getUserPhono());
        userVO.setUserPwd(dto.getUserPwd());
        userVO.setRoles(dto.getRoles());
        userVO.setUserGender(dto.getUserGender());
        userVO.setUserDob(dto.getUserDob());
        userVO.setCompany(dto.getCompany());
        userVO.setRoles(dto.getRoles());
        return userVO;
    };
    
    Function<User,UserDTO> convertUserEntityIntoUserDTO = userEntity -> {
		UserDTO userDTO=new UserDTO();
		userDTO.setUserId(userEntity.getUserId());
		userDTO.setStatus(userEntity.getStatus());
		userDTO.setUserName(userEntity.getUserName());
		userDTO.setUserEmail(userEntity.getUserEmail());
		userDTO.setUserPhono(userEntity.getUserPhono());
		userDTO.setRoles(userEntity.getRoles());
		userDTO.setUserGender(userEntity.getUserGender());
		userDTO.setUserDob(userEntity.getUserDob());
		userDTO.setCompany(userEntity.getCompany());
		userDTO.setUserProfile(userEntity.getUserProfile());
		return userDTO;
	};

    
}