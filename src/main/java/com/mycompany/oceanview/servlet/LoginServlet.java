package com.mycompany.oceanview.servlet;

import org.mindrot.jbcrypt.BCrypt;
import com.mycompany.oceanview.util.DBConnection;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM users WHERE email=?")) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password"); // hashed password from DB
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    boolean isFirstLogin = rs.getBoolean("is_first_login");
                    int userId = rs.getInt("user_id");

                    // Check password with BCrypt
                    if (BCrypt.checkpw(password, storedHash)) {
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("role", role);
                        session.setAttribute("userId", userId);

                        if (isFirstLogin) {
                            response.sendRedirect("change_password.jsp");
                        } else {
                            switch (role) {
                                case "admin" ->
                                    response.sendRedirect("admin");
                                case "receptionist" ->
                                    response.sendRedirect("receptionist");
                                default ->
                                    response.sendRedirect("index.html");
                            }
                        }
                    } else {
                        request.setAttribute("message", "Invalid email or password!");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("message", "Invalid email or password!");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            request.setAttribute("message", "Database Connection Error!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
