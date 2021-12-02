package com.app.serviceimpl;

import com.app.SpringBootTestTests;
import com.app.exceptions.CustomException;
import com.app.model.Role;
import com.app.model.User;
import com.app.pojos.UserDTO;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest extends SpringBootTestTests {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    public void testSaveUser() {
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
    public void testGetUser() {
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
     void testUserNotFound(){
        when(userRepo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
        assertThrows(CustomException.class,()->userServiceImpl.findById(90));
    }

    @Test
    public void testDeleteUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserEmail("test@gmail.com");
        userDTO.setUserName("test");
        userDTO.setUserPwd("test");
        userServiceImpl.deleteUserById(1);
        atLeastOnce();
    }

    @Test
    public void testGetAllUsers() {
       List<UserDTO> users= List.of( UserDTO.builder().userEmail("karun@gmail.com").userGender("MALE").userPhono("980745").build(), UserDTO.builder().userEmail("varun@gmail.com").userGender("MALE").userPhono("97880745").build());
        Mockito.when(userRepo.findAll()).thenReturn(users.stream().map(u->convertUserDtoToUserEntity.apply(u)).collect(Collectors.toList()));
        List<UserDTO> expected=userServiceImpl.findAllUsers();
        assertTrue(users.size()==expected.size());
    }

    Function<UserDTO,User> convertUserDtoToUserEntity = dto -> {
        User userVO=new User();
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