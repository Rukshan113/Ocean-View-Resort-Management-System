package com.mycompany.oceanview.servlet;

import com.mycompany.oceanview.dao.UserDAO;
import com.mycompany.oceanview.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        try {
            UserDAO dao = new UserDAO();

            if("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                User user = dao.getUserById(id);
                request.setAttribute("user", user);
                request.getRequestDispatcher("update_user.jsp").forward(request, response);
            } 
            else {
                String search = request.getParameter("search");
                List<User> users = dao.getAllUsers(search);
                request.setAttribute("userList", users);
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
            }

        } catch(Exception e){
            request.setAttribute("message", "Database connection Error!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            UserDAO dao = new UserDAO();

            switch (action) {
                case "create" -> {
                    User user = new User();
                    user.setUsername(request.getParameter("username"));
                    user.setEmail(request.getParameter("email"));
                    user.setPassword(request.getParameter("password"));
                    user.setRole(request.getParameter("role"));
                    dao.createUser(user);
                    }
                case "delete" -> dao.deleteUser(Integer.parseInt(request.getParameter("id")));
                case "update" -> {
                    User user = new User();
                    user.setId(Integer.parseInt(request.getParameter("id")));
                    user.setUsername(request.getParameter("username"));
                    user.setEmail(request.getParameter("email"));
                    user.setRole(request.getParameter("role"));
                    dao.updateUser(user);
                    }
                default -> {
                    break;
                }
            }

            response.sendRedirect("admin");

        } catch(Exception e){
           request.setAttribute("message", "Database connection Error!");
           request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}