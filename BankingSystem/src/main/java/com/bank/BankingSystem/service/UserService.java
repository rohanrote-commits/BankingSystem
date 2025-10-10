package com.bank.BankingSystem.service;

import com.bank.BankingSystem.dao.UserDao;
import com.bank.BankingSystem.dao.UserDaoImpl;
import com.bank.BankingSystem.dto.GetUserdto;
import com.bank.BankingSystem.dto.Updatedto;
import com.bank.BankingSystem.entities.User;
import com.bank.BankingSystem.exceptions.BankingSystemException;
import com.bank.BankingSystem.exceptions.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserDaoImpl userDao;

    public void registerUser(User user){
        Optional<User> userOptional = userDao.findByUsername(user.getUsername());
         if(userOptional.isPresent()){
             throw new BankingSystemException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
         }
         userDao.save(user);

    }

    public User getUser(GetUserdto getUserdto) {
        User user = userDao.findByUsername(getUserdto.getUsername())
                .orElseThrow(() -> new BankingSystemException(ErrorCode.USER_NOT_FOUND));

        if (!java.util.Objects.equals(user.getPassword(), getUserdto.getPassword())) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }

        return user;
    }

    public User deleteUser(GetUserdto getUserdto){
     Optional<User> optionalUser = userDao.deleteUserByUsernameAndPassword(getUserdto.getUsername(),getUserdto.getPassword());
             optionalUser.orElseThrow(() -> new BankingSystemException(ErrorCode.WRONG_CREDENTIALS));
     return optionalUser.get();
    }

    public User updateUser(Updatedto updatedto,String username,String password){

        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new BankingSystemException(ErrorCode.USER_NOT_FOUND));

        if (!java.util.Objects.equals(user.getPassword(), password)) {
            throw new BankingSystemException(ErrorCode.WRONG_CREDENTIALS);
        }

        if(updatedto.getName()!=null){
            user.setName(updatedto.getName());
        }
        if(updatedto.email!=null){
            user.setEmail(updatedto.email);
        }
       return userDao.save(user);

    }

}
