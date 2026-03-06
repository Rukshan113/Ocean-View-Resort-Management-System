package com.mycompany.oceanview.servlet;

import com.mycompany.oceanview.dao.ReservationDAO;
import com.mycompany.oceanview.dao.RoomDAO;
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
                
                ReservationDAO rdao = new ReservationDAO();
                RoomDAO roomDao = new RoomDAO();
                request.setAttribute("totalReservations", rdao.getTotalReservations());
                request.setAttribute("totalRevenue", rdao.getTotalRevenue());
                request.setAttribute("availableRooms", roomDao.getAvailableRoomCount());
                request.setAttribute("totalMonthReservation", rdao.getCurrentMonthReservations());
                request.setAttribute("totalMonthRevenue", rdao.getCurrentMonthRevenue());
                
               
                
                request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
            }

        } catch(Exception e){
            request.setAttribute("message", "Database connection Error!");
            request.setAttribute("redirect", "admin");
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

                    // Hash the password before storing
                    String plainPassword = request.getParameter("password");
                    String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
                    user.setPassword(hashedPassword);

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
           request.setAttribute("redirect", "admin");
           request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}