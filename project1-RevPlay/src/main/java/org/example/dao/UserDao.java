package org.example.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.DBConnection;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final Logger logger = LogManager.getLogger(UserDao.class);

    //
    public void deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
        }
    }

    // ---------- REGISTER -----------
    public boolean registerUser(User user){
        String insert_user = "INSERT INTO users (email, password, role, security_question, security_answer, status)" + "VALUES (?, ?, ?, ?, ?, ?)";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(insert_user);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getSecurityQuestion());
            ps.setString(5, user.getSecurityAnswer());
            ps.setString(6, user.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;

        }catch (SQLException se){
            logger.error("Error while registering user", se);
            return false;
        }
    }

    // --- USERID BY MAIL ---
    public int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getInstance();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            logger.error("Error fetching user id by email", e);
        }
        return -1;
    }

    // --------- LOGIN --------
    public User login(String email, String password){
        String get_user_by_email = "SELECT * FROM users WHERE email = ? AND status = 'ACTIVE'";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(get_user_by_email);
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    if (password.equals(rs.getString("password"))){
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setEmail(rs.getString("email"));
                        user.setRole(rs.getString("role"));
                        user.setStatus(rs.getString("status"));
                        return user;
                    }
                }
            }
        }catch (SQLException se){
            logger.error("Error during login", se);
        }
        return null;
    }

    //---- USER EXISTENCE ----
    public boolean userExists(String email){
        String check_user_exists = "SELECT user_id FROM users WHERE email = ?";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(check_user_exists);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch (SQLException se){
            logger.error("Error checking user existence", se);
            return false;
        }
    }

    // --- USER BY EMAIL ---
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            Connection con = DBConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password")); // HASHED
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setSecurityQuestion(rs.getString("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                return user;
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by email", e);
        }
        return null;
    }

    // --- CHANGE PASSWORD ---
    public boolean verifyOldPassword(String email, String oldPassword){
        String get_password = "SELECT password FROM users WHERE email = ?";
        try {
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(get_password);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return oldPassword.equals(rs.getString("password"));
            }
        } catch (SQLException se){
            logger.error("Error verifying old password", se);
        }
        return false;
    }

    public String getPasswordByEmail(String email) {
        String get_password = "SELECT password FROM users WHERE email = ?";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(get_password);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }

        } catch (SQLException se) {
            logger.error("Error fetching password", se);
        }
        return null;
    }

    // ----- FORGOT PASSWORD ----
    public String getSecurityQuestion(String email){
        String get_security_QA = "SELECT security_question, security_answer FROM users WHERE email = ?";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(get_security_QA);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("security_question");
            }
        }catch (SQLException se){
            logger.error("Error fetching security question", se);
        }
        return null;
    }

    public boolean verifySecurityAnswer(String email, String answer){
        String get_security_QA = "SELECT security_question, security_answer FROM users WHERE email = ?";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(get_security_QA);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return answer.equals(rs.getString("security_answer"));
            }
        }catch (SQLException se){
            logger.error("Error fetching security answer", se);
        }
        return false;
    }

    // ------ UPDATE PASSWORD -------
    public boolean updatePassword(String email, String newPassword){
        String update_password = "UPDATE users SET password = ? WHERE email = ?";
        try{
            Connection conn = DBConnection.getInstance();
            PreparedStatement ps = conn.prepareStatement(update_password);
            ps.setString(1, newPassword);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        }catch (SQLException se){
            logger.error("Error updating password", se);
            return false;
        }
    }
}
