package com.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.iservice.IUserService;
import com.app.model.ResoureNotFoundException;
import com.app.model.Response;
import com.app.model.User;
import com.app.securityconfig.JwtUtil;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;


    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody JSONObject user) {
        String message = "message";
        Map<String, String> m = new HashMap<>();
        try {
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.get("email"), user.get("password")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            m.put("AuthToken", token);
            m.put(message, "Login Success");
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        } catch (BadCredentialsException e) {
            m.put(message, "UserName or Password Incurrect");
            return new ResponseEntity<>(m, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            m.put(message, e.getMessage());
            return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/signup")
    public Response<User> saveUser(@RequestBody User user) {
        User userExists = userService.findByUserEmail(user.getUserEmail());
        if (userExists != null) {
            return new Response<>(user, "User Already exists with this " + user.getUserEmail() + " email", HttpStatus.ACCEPTED);
        } else {
            user.setUserId(null);
            user.setStatus(User.STATUS.ACTIVE.name());
            User savedUser = userService.saveUser(user);
            return new Response<>(savedUser, "User Saved Successfully", HttpStatus.ACCEPTED);
        }
    }


    @GetMapping("/getuser/{id}")
    public Response<User> getUserById(@PathVariable("id") Integer userId) {
        User user = userService.findById(userId).orElseThrow(()->new ResoureNotFoundException("User Not Found"));
        if (user!=null)
            return new Response<>(user, "user", HttpStatus.OK);
        else
            return new Response<>(null, "User Not Found", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getallusers")
    public Response<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new Response<>(users, "OK", HttpStatus.OK);
    }

    @DeleteMapping("/deleteuser/{id}")
    public Response<User> deleteUserById(@PathVariable("id") Integer userId) {
        User user = userService.findById(userId).orElseThrow(()->new ResoureNotFoundException("User Not Found"));;
        if (user!=null) {
            userService.deleteUserById(userId);
            return new Response<>(null, "User Deleted Successfully", HttpStatus.OK);
        }
        return null;
    }

    @PutMapping("/updateuser")
    public Response<User> updateUser(@RequestBody User user) {
        logger.info("udating user");
        if (user.getUserId() != null) {
            Integer userID = user.getUserId();
            Optional<User> userFromDB = userService.findById(userID);
            if (userFromDB.isPresent()) {
                User updatedUser = foo.apply(userFromDB.get(), user);
                return new Response<>(updatedUser, "User Details Updated Successfully", HttpStatus.OK);
            } else {
                return new Response<>(null, "User Not Found", HttpStatus.BAD_REQUEST);
            }
        }
        return new Response<>(null, "UserId is required for updating user.", HttpStatus.BAD_REQUEST);
    }

    BinaryOperator<User> foo = (userFromDB, userFromUI) -> {
        userFromDB.setUserDob(userFromUI.getUserDob());
        userFromDB.setUserGender(userFromUI.getUserGender());
        userFromDB.setUserName(userFromUI.getUserName());
        userFromDB.setUserPhono(userFromUI.getUserPhono());
        userFromDB.setStatus(User.STATUS.ACTIVE.name());
        return userService.updateUser(userFromDB);

    };


    @GetMapping("/searchuser")
    public Response<List<User>> searchUser(@RequestParam String searchKey) {
        List<User> users = userService.searchUser(searchKey);
        if (users.isEmpty())
            return new Response<>(null, "Users Not Found", HttpStatus.BAD_REQUEST);
        else
            return new Response<>(users, "OK", HttpStatus.OK);
    }

    @PostMapping("/uploadingfile")
    public Response<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String result = userService.uploadFile(file);
            return new Response<>(null, result, HttpStatus.OK);
        } else {
            return new Response<>(null, "file dos't exist", HttpStatus.BAD_REQUEST);
        }

    }

}
