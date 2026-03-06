package com.mycompany.oceanview.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class SetupAdmin {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            
            PreparedStatement stmtCheck = con.prepareStatement("SELECT * FROM users WHERE role='admin'");
            ResultSet rs = stmtCheck.executeQuery();

            if (!rs.next()) { // only create if no admin exists
                String plainPassword = "123"; // initial password
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                PreparedStatement stmtInsert = con.prepareStatement(
                    "INSERT INTO users (username, email, password, role, is_first_login) VALUES (?, ?, ?, ?, ?)"
                );
                stmtInsert.setString(1, "Admin");
                stmtInsert.setString(2, "admin@gmail.com");
                stmtInsert.setString(3, hashedPassword);
                stmtInsert.setString(4, "admin");
                stmtInsert.setBoolean(5, true);
                stmtInsert.executeUpdate();

                System.out.println("First admin created successfully!");
            } else {
                System.out.println("Admin already exists, skipping creation.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}