package com.mycompany.oceanview;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ReceptionistServlet extends HttpServlet {

    private String jdbcURL = "jdbc:mysql://localhost:3306/ocean_view_resort_management_system?useSSL=false";
    private String dbUser = "root";
    private String dbPassword = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session == null || !"receptionist".equals(session.getAttribute("role"))){
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");

        try(Connection con = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {

            // Load available rooms for the dropdown
            PreparedStatement psRooms = con.prepareStatement(
                "SELECT room_id, room_number, room_type FROM rooms WHERE status='available' ORDER BY room_number"
            );
            ResultSet rsRooms = psRooms.executeQuery();
            List<Room> availableRooms = new ArrayList<>();
            while(rsRooms.next()){
                Room room = new Room();
                room.setRoomId(rsRooms.getInt("room_id"));
                room.setRoomNumber(rsRooms.getString("room_number"));
                room.setRoomType(rsRooms.getString("room_type"));
                availableRooms.add(room);
            }
            rsRooms.close();
            psRooms.close();
            request.setAttribute("availableRooms", availableRooms);

            
            List<Reservation> reservationList = new ArrayList<>();
            //calculate bill
            if("bill".equals(action)) {
                int resNo = Integer.parseInt(request.getParameter("resNo"));

                String sql = "SELECT r.*, rm.room_type, rm.price_per_night " +
                             "FROM reservations r JOIN rooms rm ON r.room_id = rm.room_id " +
                             "WHERE r.reservation_no = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, resNo);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    Reservation r = new Reservation();
                    r.setReservationNo(rs.getInt("reservation_no"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("contact_number"));
                    r.setRoomType(rs.getString("room_type"));
                    r.setPricePerNight(rs.getDouble("price_per_night"));

                    // Calculate nights stayed
                    java.sql.Date checkIn = rs.getDate("check_in");
                    java.sql.Date checkOut = rs.getDate("check_out");
                    int nights = (int)((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
                    double total = r.getPricePerNight() * nights;

                    request.setAttribute("reservation", r);
                    request.setAttribute("nights", nights);
                    request.setAttribute("total", total);
                } else {
                    request.setAttribute("error", "Reservation not found.");
                }

                rs.close();
                ps.close();
                request.getRequestDispatcher("bill.jsp").forward(request, response);
                return; // important to stop further code
            }
            // Load reservations for dashboard
            else if("search".equals(action) && request.getParameter("search") != null) {
                String search = request.getParameter("search").trim();
                String sql = "SELECT r.*, rm.room_number, rm.room_type, rm.price_per_night " +
                             "FROM reservations r " +
                             "JOIN rooms rm ON r.room_id = rm.room_id " +
                             "WHERE r.reservation_no=? OR r.guest_name LIKE ? OR r.contact_number LIKE ?";
                PreparedStatement ps = con.prepareStatement(sql);
                try {
                    ps.setInt(1, Integer.parseInt(search));
                } catch(NumberFormatException e) {
                    ps.setInt(1, -1);
                }
                ps.setString(2, "%" + search + "%");
                ps.setString(3, "%" + search + "%");
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Reservation r = new Reservation();
                    r.setReservationNo(rs.getInt("reservation_no"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("contact_number"));
                    r.setRoomNumber(rs.getString("room_number"));
                    r.setRoomType(rs.getString("room_type"));
                    r.setPricePerNight(rs.getDouble("price_per_night"));
                    reservationList.add(r);
                }
                rs.close();
                ps.close();
                if(reservationList.isEmpty()){
                    request.setAttribute("error", "Reservation Not Found!");
                }
            } else {
                // Load all reservations if no search
                String sql = "SELECT r.*, rm.room_number, rm.room_type, rm.price_per_night " +
                             "FROM reservations r " +
                             "JOIN rooms rm ON r.room_id = rm.room_id";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Reservation r = new Reservation();
                    r.setReservationNo(rs.getInt("reservation_no"));
                    r.setGuestName(rs.getString("guest_name"));
                    r.setPhone(rs.getString("contact_number"));
                    r.setRoomNumber(rs.getString("room_number"));
                    r.setRoomType(rs.getString("room_type"));
                    r.setPricePerNight(rs.getDouble("price_per_night"));
                    reservationList.add(r);
                }
                rs.close();
                ps.close();
            }

            request.setAttribute("reservationList", reservationList);
            request.getRequestDispatcher("receptionist_dashboard.jsp").forward(request, response);

        } catch(Exception e){
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("receptionist_dashboard.jsp").forward(request, response);
        }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session == null || !"receptionist".equals(session.getAttribute("role"))){
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");

        try(Connection con = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {

            if("add".equals(action)){
                String guestName = request.getParameter("guestName");
                String address = request.getParameter("address");
                String contact = request.getParameter("contact");
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                String checkIn = request.getParameter("checkIn");
                String checkOut = request.getParameter("checkOut");

                // Insert reservation
                String sql = "INSERT INTO reservations (guest_name,address,contact_number,room_id,check_in,check_out) VALUES (?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, guestName);
                ps.setString(2, address);
                ps.setString(3, contact);
                ps.setInt(4, roomId);
                ps.setDate(5, java.sql.Date.valueOf(checkIn));
                ps.setDate(6, java.sql.Date.valueOf(checkOut));
                ps.executeUpdate();

                // Update room status
                String updateRoom = "UPDATE rooms SET status='booked' WHERE room_id=?";
                PreparedStatement ps2 = con.prepareStatement(updateRoom);
                ps2.setInt(1, roomId);
                ps2.executeUpdate();

                response.sendRedirect("receptionist?action=dashboard");
            }

        } catch(Exception e){
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error occurred while processing reservation!</h3>");
        }
    }
}