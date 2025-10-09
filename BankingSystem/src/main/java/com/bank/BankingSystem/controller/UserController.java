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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.registerUser(user);

        return new ResponseEntity<>("User Registered", HttpStatus.CREATED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestBody GetUserdto getUserdto){
        User user = userService.getUser(getUserdto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody GetUserdto getUserdto){
        User user = userService.deleteUser(getUserdto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Updatedto updatedto,@RequestHeader("user-name") String username, @RequestHeader("password") String password){
        userService.updateUser(updatedto,username,password);
        return new ResponseEntity<>("User Updated", HttpStatus.OK);

    }


}
