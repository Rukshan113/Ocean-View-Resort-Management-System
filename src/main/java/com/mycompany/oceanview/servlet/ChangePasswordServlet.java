package com.mycompany.oceanview.servlet;

import com.mycompany.oceanview.dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("index.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("message", "Passwords do not match!");
            request.setAttribute("redirect", "change_password.jsp");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            // Hash the new password using BCrypt
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            UserDAO dao = new UserDAO();
            dao.updatePassword(userId, hashedPassword, false); // pass hashed password

            // Redirect based on role
            String role = (String) session.getAttribute("role");
            if ("admin".equals(role)) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("receptionist");
            }

        } catch (Exception e) {
            request.setAttribute("message", "Database Connection Error!");
            request.setAttribute("redirect", "change_password.jsp");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}