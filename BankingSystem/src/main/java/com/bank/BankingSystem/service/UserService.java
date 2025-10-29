package com.bank.BankingSystem.service;

import com.bank.BankingSystem.dao.UserDaoImpl;
import com.bank.BankingSystem.dto.GetUserdto;
import com.bank.BankingSystem.dto.Updatedto;
import com.bank.BankingSystem.entities.User;
import com.bank.BankingSystem.exceptions.BankingSystemException;
import com.bank.BankingSystem.exceptions.ErrorCode;
import com.bank.BankingSystem.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDaoImpl userDao;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerUser(User user) {
        try {
            Optional<User> userOptional = userDao.findByUsername(user.getUsername());
            if (userOptional.isPresent()) {
                throw new BankingSystemException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
            }
            userDao.save(user);
        } catch (Exception e) {
            log.error("Error registering user {}: {}", user.getUsername(), e.getMessage());
            throw e;
        }
    }

    public User getUser(GetUserdto getUserdto) {
        try {
            User user = userDao.findByUsername(getUserdto.getUsername())
                    .orElseThrow(() -> new BankingSystemException(ErrorCode.USER_NOT_FOUND));

            if (!user.getPassword().equals(getUserdto.getPassword())) {
                throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
            }

            return user;
        } catch (Exception e) {
            log.error("Error fetching user {}: {}", getUserdto.getUsername(), e.getMessage());
            throw e;
        }
    }

    public User deleteUser(GetUserdto getUserdto) {
        try {
            Optional<User> optionalUser = userDao.deleteUserByUsernameAndPassword(
                    getUserdto.getUsername(),
                    getUserdto.getPassword()
            );
            return optionalUser.orElseThrow(() -> new BankingSystemException(ErrorCode.WRONG_CREDENTIALS));
        } catch (Exception e) {
            log.error("Error deleting user {}: {}", getUserdto.getUsername(), e.getMessage());
            throw e;
        }
    }
    //generate token here
    public String generateLoginToken(String username, String password) {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isPresent()) {
            if(user.get().getPassword().equals(password)){

             return jwtUtil.generateToken(username);
            }else{
                throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
            }
        }else{
            throw new BankingSystemException(ErrorCode.USER_NOT_FOUND);
        }


    }

    public User updateUser(Updatedto updatedto, String username, String password) {
        try {
            User user = userDao.findByUsername(username)
                    .orElseThrow(() -> new BankingSystemException(ErrorCode.USER_NOT_FOUND));

            if (!user.getPassword().equals(password)) {
                throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
            }

            if (updatedto.getName() != null) {
                user.setName(updatedto.getName());
            }
            if (updatedto.getEmail() != null) {
                user.setEmail(updatedto.getEmail());
            }

            return userDao.save(user);
        } catch (Exception e) {
            log.error("Error updating user {}: {}", username, e.getMessage());
            throw e;
        }
    }
}
