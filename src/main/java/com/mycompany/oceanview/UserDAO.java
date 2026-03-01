package com.mycompany.oceanview;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/ocean_view_resort_management_system?useSSL=false";
    private String dbUser = "root";
    private String dbPassword = "";

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (username,email,password,role) VALUES (?,?,?,?)";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getRole());
        ps.executeUpdate();
        con.close();
    }

    public void deleteUser(int user_id) throws Exception {
        String sql = "DELETE FROM users WHERE user_id=?";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, user_id);
        ps.executeUpdate();
        con.close();
    }

    public void updateUser(User user) throws Exception {
        String sql = "UPDATE users SET username=?, email=?, role=? WHERE user_id=?";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getRole());
        ps.setInt(4, user.getId());
        ps.executeUpdate();
        con.close();
    }

    public List<User> getAllUsers(String search) throws Exception {
        List<User> list = new ArrayList<>();
        Connection con = getConnection();

        String sql = "SELECT * FROM users";
        PreparedStatement ps;

        if(search != null && !search.isEmpty()) {
            sql += " WHERE username LIKE ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
        } else {
            ps = con.prepareStatement(sql);
        }

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            list.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role")
            ));
        }

        con.close();
        return list;
    }

    public User getUserById(int user_id) throws Exception {
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE user_id=?");
        ps.setInt(1, user_id);
        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()) {
            user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role")
            );
        }

        con.close();
        return user;
    }
}