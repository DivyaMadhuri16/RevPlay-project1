package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.PasswordUtil;

public class UserService {
    private UserDao userDao = new UserDao();

    public boolean register(User user){
        if (user.getEmail() == null || user.getPassword() == null){
            return false;
        }
        if(userDao.userExists(user.getEmail())){
            return false;
        }
        user.setStatus("ACTIVE");
        String hashedPassword =
                PasswordUtil.hashPassword(user.getPassword());

        user.setPassword(hashedPassword);
        return userDao.registerUser(user);
    }

    public User login(String email, String password){
        User user = userDao.getUserByEmail(email);
        if (user != null &&
                PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            return false;
        }
        if (PasswordUtil.verifyPassword(newPassword, user.getPassword())) {
            return false;
        }
        String newHashed = PasswordUtil.hashPassword(newPassword);
        return userDao.updatePassword(email, newHashed);
    }

    public boolean recoverPassword(String email, String answer, String newPassword) {
        if (!userDao.verifySecurityAnswer(email, answer)) {
            return false;
        }

        User user = userDao.getUserByEmail(email);
        if (user == null) {
            return false;
        }
        if (PasswordUtil.verifyPassword(newPassword, user.getPassword())) {
            return false;
        }
        String newHashed = PasswordUtil.hashPassword(newPassword);
        return userDao.updatePassword(email, newHashed);
    }


    public String getSecurityQuestion(String email) {
        return userDao.getSecurityQuestion(email);
    }
}
