package org.WalletHomeWork.service;

import org.WalletHomeWork.Exception.BusinessException;
import org.WalletHomeWork.entity.User;
import org.WalletHomeWork.repositiry.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;


public class UserService {
    static UserRepository userRepository;


    public UserService() {
        userRepository = new UserRepository();
    }

    public Long insertNewUser(User user) {
        try {
            if (!userRepository.checkExistedUsername(user)) {
                String password = hashPassword(user.getPassword());
                user.setPassword(password);
                Long id = userRepository.insert(user);
                System.out.println("User signed up successfully");
                return id;
            }
        } catch (BusinessException e) {
            throw new BusinessException("already existed user");

        }
        return null;
    }

    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private boolean checkPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, hashedPassword);
    }

    public User login(String userName, String password) {
        User user = userRepository.login(userName);
        if (user == null) {
            throw new BusinessException("User not found");
        }
        String hashedPassword = user.getPassword();
        if (checkPassword(password, hashedPassword)) {
            return user;
        } else {
            throw new BusinessException("Invalid password");
        }
    }

    public void updateWalletId(Long walletId, Long userID) {
        userRepository.updateWalletId(walletId, userID);
    }


}

