package com.mycompany.oceanview;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author shanr
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String jdbcURL = "jdbc:mysql://localhost:3306/ocean_view_resort_management_system?useSSL=false";
        String dbUser = "root";
        String dbPassword = "";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User exists
                String username = rs.getString("username");
                String role = rs.getString("role");

                // Store session info
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", role);

                if (role.equals("admin")) {
                    response.sendRedirect("admin_dashboard.jsp");
                } else if (role.equals("manager")) {
                    response.sendRedirect("manager_dashboard.jsp");
                }
                else{
                    response.sendRedirect("receptionist_dashboard.jsp");
                }
            }
            else{
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<h3 style='color:red;'>Invalid email or password!</h3>");
                response.getWriter().println("<a href='index.html'>Go back to login</a>");
            }
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Database connection error!</h3>");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.html");
    }
}   