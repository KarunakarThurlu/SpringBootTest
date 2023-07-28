package com.app.serviceimpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.constants.CommonConstants;
import com.app.exceptions.CustomException;
import com.app.iservice.IUserService;
import com.app.mailingservice.SendMail;
import com.app.model.Role;
import com.app.model.User;
import com.app.pojos.UserDTO;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements IUserService {

	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private PasswordEncoder bcryptPasswordEncoder;

	@Autowired
	private UserRepo repo;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SendMail sendMail;

	
	@Override
	@CacheEvict(value = "usercache", key = "#userEmail")
	public User getUserForAuthCheck(String userEmail) {
		return repo.findByUserEmail(userEmail);
	}

	
	@Override
	public String sendMail() {
		sendMail.sendMailWithAttachment();
		return "mail sended";
	}

	@Override
	public String uploadFile(MultipartFile file) {
		try {
			User user=new User();
			user.setUserProfile(file.getBytes());
			return "ProfileUploaded Successfully";
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	
	@Override
	@Caching(evict = {@CacheEvict(value = "usercache", allEntries = true)})
	public UserDTO saveUser(UserDTO u) {
		log.info("Enter into save user method ");
		String pwd = u.getUserPwd();
		u.setUserPwd(bcryptPasswordEncoder.encode(pwd));
		u.setRoles(saveUserRoles.apply(u.getRoles()));
		u.setUserId(null);
		User userEntity = convertUserDtoToUserEntity.apply(u);
		return convertUserEntityIntoUserDTO.apply(repo.save(userEntity));
	
	}

	
	@Override
	@Cacheable(cacheNames = "usercache", key = "#email")
	public User findByUserEmail(String email) {
		return repo.findByUserEmail(email);
	}

	@Override
	@Cacheable(cacheNames = "usercache")
	public List<UserDTO> findAllUsers() {
		List<User> users= repo.findAll();
		return users
				.stream()
				.map(u->convertUserEntityIntoUserDTO.apply(u))
				.toList();
	}

	@Override
	@Cacheable(cacheNames = "usercache", key = "#id")
	public UserDTO findById(Integer id) {
		User user = repo.findById(id).orElseThrow(()->new CustomException(CommonConstants.NOT_FOUND,CommonConstants.USER_NOT_FOUND));
		return convertUserEntityIntoUserDTO.apply(user);
	}

	@Override
	@CacheEvict(value = "usercache", key = "#userId")
	public String deleteUserById(Integer userId) {
		User user = repo.findById(userId).orElseThrow(()->new CustomException(CommonConstants.NOT_FOUND,CommonConstants.USER_NOT_FOUND));
		if(user!=null)
			repo.deleteById(user.getUserId());
		return CommonConstants.USER_DELETED;
	}

	@Override
	@CacheEvict(value = "usercache")
	@Transactional
	public UserDTO updateUser(UserDTO userDto) {
		User userVO = repo.findById(userDto.getUserId()).orElseThrow(()->new CustomException(CommonConstants.NOT_FOUND,CommonConstants.USER_NOT_FOUND));
		userVO.setUserName(userDto.getUserName()!=null?userDto.getUserName():userVO.getUserName());
		userVO.setUserPhono(userDto.getUserPhono()!=null?userDto.getUserPhono():userVO.getUserPhono());
		userVO.setUserGender(userDto.getUserGender()!=null?userDto.getUserGender():userVO.getUserGender());
		userVO.setUserDob(userDto.getUserDob()!=null?userDto.getUserDob():userVO.getUserDob());
		
		userVO.setRoles(saveUserRoles.apply(userDto.getRoles()));
		User savedUserentity = repo.save(userVO);
		return convertUserEntityIntoUserDTO.apply(savedUserentity);
	}

	@Override
	public List<UserDTO> searchUser(String searchKey) {
		List<User> users = repo.getUsersByDynamicSearch(searchKey);
		return users
				.stream()
				.map(u->convertUserEntityIntoUserDTO.apply(u))
				.toList();
	}
	@Override
	public String uploadFile(UserDTO userDTO) {
		User user = repo.findById(userDTO.getUserId()).orElseThrow(()->new CustomException(CommonConstants.NOT_FOUND,CommonConstants.USER_NOT_FOUND));
		user.setUserId(userDTO.getUserId());
		user.setUserProfile(userDTO.getUserProfile());
		repo.save(user);
		return "Profile Uploaded Successfully";
	}
	
	UnaryOperator<Set<Role>> saveUserRoles = rolesFromUI -> {
		Set<Role> roles = new HashSet<>();
		for (Role role : rolesFromUI) {
			Optional<Role> savedRole = roleRepository.findByRoleName(role.getRoleName());
			if (savedRole.isPresent()) {
				roles.add(savedRole.get());
			} else {
				Role newRole = roleRepository.save(role);
				roles.add(newRole);
			}
		}
		return roles;
	};
	
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
