package com.mycompany.oceanview.dao;

import com.mycompany.oceanview.model.User;
import com.mycompany.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (username,email,password,role) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
        }
    }

    public void deleteUser(int user_id) throws Exception {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();             
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.executeUpdate();
        }
    }

    public void updateUser(User user) throws Exception {
        String sql = "UPDATE users SET username=?, email=?, role=? WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        }
    }

    public List<User> getAllUsers(String search) throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        if (search != null && !search.isEmpty()) {
            sql += " WHERE username LIKE ?";
        }

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            if (search != null && !search.isEmpty()) {
                ps.setString(1, "%" + search + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role")
                    ));
                }
            }
        }

        return list;
    }

    public User getUserById(int user_id) throws Exception {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, user_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }
        }

        return user;
    }
    
    public void updatePassword(int userId, String newPassword, boolean isFirstLogin) throws Exception {
        String sql = "UPDATE users SET password=?, is_first_login=? WHERE user_id=?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setBoolean(2, isFirstLogin);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }
}