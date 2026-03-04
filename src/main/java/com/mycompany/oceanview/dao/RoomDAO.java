package com.mycompany.oceanview.dao;

import com.mycompany.oceanview.model.Room;
import com.mycompany.oceanview.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAvailableRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_id, room_number, room_type FROM rooms WHERE status='available' ORDER BY room_number";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setRoomType(rs.getString("room_type"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    public void updateRoomStatus(int roomId, String status) throws Exception {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, roomId);
            ps.executeUpdate();
        }
    }
    
    public int getAvailableRoomCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM rooms WHERE status='available'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}