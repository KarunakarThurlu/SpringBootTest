package com.app.serviceimpl;

import com.app.iservice.IUserService;
import com.app.mailingservice.SendMail;
import com.app.model.Role;
import com.app.model.User;
import com.app.repo.RoleRepository;
import com.app.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private UserRepo repo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SendMail sendMail;

    @Override
    public String sendMail() {
        sendMail.sendMailWithAttachment();
        return "mail sended";
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null) {
                return null;
            }
            byte[] fileName = file.getOriginalFilename().toString().getBytes();
            Path path = Paths.get("/home/karunakar/tony/" + LocalDate.now() + "_" + (new String(fileName, "UTF-8")));
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
            return "fileUploaded";
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "usercache", allEntries = true)})
    public User saveUser(User u) {
        log.info("Enter into save user method ");
        Set<Role> rolesFromUI = u.getRoles();
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
        String pwd = u.getUserPwd();
        u.setUserPwd(bcryptPasswordEncoder.encode(pwd));
        u.setRoles(roles);
        return repo.save(u);
    }

    @Override
    @Cacheable(cacheNames = "usercache", key = "#email")
    public User findByUserEmail(String email) {
        return repo.findByUserEmail(email);
    }

    @Override
    @Cacheable(cacheNames = "usercache")
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @Override
    @Cacheable(cacheNames = "usercache", key = "#id")
    public Optional<User> findById(Integer id) {
        Optional<User> user = repo.findById(id);
        return user;
    }

    @Override
    @CacheEvict(value = "usercache", key = "#userId")
    public void deleteUserById(Integer userId) {
        repo.deleteById(userId);
    }

    @Override
    @CacheEvict(value = "usercache")
    public User updateUser(User updatedUser) {
        return repo.save(updatedUser);
    }

    @Override
    public List<User> searchUser(String searchKey) {
        return repo.getUsersByDynamicSearch(searchKey);
    }

}	
