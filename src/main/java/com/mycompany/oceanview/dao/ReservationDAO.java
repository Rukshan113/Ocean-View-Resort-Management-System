package com.mycompany.oceanview.dao;

import com.mycompany.oceanview.model.Reservation;
import com.mycompany.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setReservationNo(rs.getInt("reservation_no"));
        r.setGuestName(rs.getString("guest_name"));
        r.setAddress(rs.getString("address"));
        r.setPhone(rs.getString("contact_number"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setPricePerNight(rs.getDouble("price_per_night"));
        r.setCheckIn(rs.getDate("check_in"));
        r.setCheckOut(rs.getDate("check_out"));
        return r;
    }

    public Reservation getReservation(int reservationNo) throws Exception {
        String sql = "SELECT r.*,  rm.room_number, rm.room_type, rm.price_per_night " +
                     "FROM reservations r JOIN rooms rm ON r.room_id = rm.room_id " +
                     "WHERE r.reservation_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationNo);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return mapResultSetToReservation(rs);
                }
            }
        }
        return null;
    }

    public List<Reservation> searchReservations(String search) throws Exception {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_number, rm.room_type, rm.price_per_night " +
                     "FROM reservations r JOIN rooms rm ON r.room_id = rm.room_id " +
                     "WHERE r.reservation_no=? OR r.guest_name LIKE ? OR r.contact_number LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            try {
                ps.setInt(1, Integer.parseInt(search));
            } catch(NumberFormatException e) {
                ps.setInt(1, -1);
            }
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    list.add(mapResultSetToReservation(rs));
                }
            }
        }
        return list;
    }

    public List<Reservation> getAllReservations() throws Exception {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.*, rm.room_number, rm.room_type, rm.price_per_night " +
                 "FROM reservations r JOIN rooms rm ON r.room_id = rm.room_id " +  
                 "ORDER BY r.reservation_no DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                list.add(mapResultSetToReservation(rs));
            }
        }
        return list;
    }

    public void addReservation(Reservation r, int roomId) throws Exception {
        String sql = "INSERT INTO reservations (guest_name,address,contact_number,room_id,check_in,check_out) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getAddress());
            ps.setString(3, r.getPhone());
            ps.setInt(4, roomId);
            ps.setDate(5, new java.sql.Date(r.getCheckIn().getTime()));
            ps.setDate(6, new java.sql.Date(r.getCheckOut().getTime()));

            ps.executeUpdate();
        }
    }
    
    
    //reports
    public int getTotalReservations() throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTotalRevenue() throws Exception {
        String sql = "SELECT SUM(DATEDIFF(r.check_out, r.check_in) * rm.price_per_night) " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.room_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    public int getCurrentMonthReservations() throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations " +
                     "WHERE MONTH(check_in) = MONTH(CURDATE()) " +
                     "AND YEAR(check_in) = YEAR(CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public double getCurrentMonthRevenue() throws Exception {
        String sql = "SELECT COALESCE(SUM(DATEDIFF(r.check_out, r.check_in) * rm.price_per_night),0) " +
                     "FROM reservations r " +
                     "JOIN rooms rm ON r.room_id = rm.room_id " +
                     "WHERE MONTH(r.check_in) = MONTH(CURDATE()) " +
                     "AND YEAR(r.check_in) = YEAR(CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
    
    public int deleteReservation(int reservationNo) throws Exception {
        int roomId = -1;
        String sqlSelect = "SELECT room_id FROM reservations WHERE reservation_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect)) {
            ps.setInt(1, reservationNo);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    roomId = rs.getInt("room_id");
                } else {
                    throw new Exception("Reservation not found");
                }
            }
        }
        String sqlDelete = "DELETE FROM reservations WHERE reservation_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlDelete)) {
            ps.setInt(1, reservationNo);
            ps.executeUpdate();
        }
        return roomId;
    }
}