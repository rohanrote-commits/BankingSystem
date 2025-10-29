package com.bank.BankingSystem.controller;

import com.bank.BankingSystem.dto.GetUserdto;
import com.bank.BankingSystem.dto.Updatedto;
import com.bank.BankingSystem.entities.User;
import com.bank.BankingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/banking/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param user user object is passed in the request body
     * @return returns thr response of User Registered
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.registerUser(user);

        return new ResponseEntity<>("User Registered", HttpStatus.CREATED);
    }

    /**
     *
     * @param getUserdto this object is passed in the request body
     * @return returns the user object
     */
    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestBody GetUserdto getUserdto){
        User user = userService.getUser(getUserdto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     *
     * @param getUserdto this object is passed in the request body
     * @return returns the user object
     */
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody GetUserdto getUserdto){
        User user = userService.deleteUser(getUserdto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     *
     * @param updatedto this object is passed in the request body
     * @param username usermalename is passed in the header
     * @param password password is passed in the header
     * @return returns the user object
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Updatedto updatedto,@RequestHeader("user-name") String username, @RequestHeader("password") String password){
        userService.updateUser(updatedto,username,password);
        return new ResponseEntity<>("User Updated", HttpStatus.OK);

    }

    @GetMapping("/login")
    public String login(@RequestHeader("user-name") String username, @RequestHeader("password") String password){
        return userService.generateLoginToken(username,password);
    }


}
