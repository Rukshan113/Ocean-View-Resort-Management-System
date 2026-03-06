package com.mycompany.oceanview.servlet;

import com.mycompany.oceanview.dao.ReservationDAO;
import com.mycompany.oceanview.dao.RoomDAO;
import com.mycompany.oceanview.model.Reservation;
import com.mycompany.oceanview.model.Room;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ReceptionistServlet extends HttpServlet {

    private final RoomDAO roomDAO = new RoomDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if(session == null || !"receptionist".equals(session.getAttribute("role"))){
            response.sendRedirect("index.html");
            return;
        }

        String action = request.getParameter("action");

        try {
            List<Room> availableRooms = roomDAO.getAvailableRooms();
            request.setAttribute("availableRooms", availableRooms);

            if("bill".equals(action)){
                int resNo = Integer.parseInt(request.getParameter("resNo"));
                Reservation r = reservationDAO.getReservation(resNo);
                if(r != null){
                    int nights = (int)((r.getCheckOut().getTime() - r.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));
                    double total = r.getPricePerNight() * nights;
                    request.setAttribute("reservation", r);
                    request.setAttribute("nights", nights);
                    request.setAttribute("total", total);
                } else {
                    request.setAttribute("error", "Reservation not found.");
                }
                request.getRequestDispatcher("bill.jsp").forward(request, response);
                return;
            }

            List<Reservation> reservationList;
            if("search".equals(action) && request.getParameter("search") != null){
                reservationList = reservationDAO.searchReservations(request.getParameter("search").trim());
                if(reservationList.isEmpty()){
                    request.setAttribute("message", "Reservation Not Found!");
                    request.setAttribute("redirect", "receptionist");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            } else {
                reservationList = reservationDAO.getAllReservations();
            }

            request.setAttribute("reservationList", reservationList);
            request.getRequestDispatcher("receptionist_dashboard.jsp").forward(request, response);

        } catch(Exception e){
            e.printStackTrace();
            request.setAttribute("message", "Database connection Error in Reservation Process!");
            request.setAttribute("redirect", "receptionist");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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

        try {
            if("add".equals(action)){
                Reservation r = new Reservation();
                r.setGuestName(request.getParameter("guestName"));
                r.setAddress(request.getParameter("address"));
                r.setPhone(request.getParameter("contact"));
                r.setCheckIn(java.sql.Date.valueOf(request.getParameter("checkIn")));
                r.setCheckOut(java.sql.Date.valueOf(request.getParameter("checkOut")));
                int roomId = Integer.parseInt(request.getParameter("roomId"));

                reservationDAO.addReservation(r, roomId);
                roomDAO.updateRoomStatus(roomId, "booked");

                response.sendRedirect("receptionist?action=dashboard");
            }

        } catch(Exception e){
            request.setAttribute("message", "Database connection Error!");
            request.setAttribute("redirect", "receptionist");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}