<%@page import="com.mycompany.oceanview.model.Room"%>
<%@page import="com.mycompany.oceanview.model.Reservation"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List" %>
<%
    List<Reservation> reservationList = (List<Reservation>) request.getAttribute("reservationList");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Receptionist Dashboard</title>
        <link rel="stylesheet" href="styles.css"/>
    </head>
    <body class="receptionist-page">
        <header class="receptionist-header">
            <div>
                <h2>Receptionist Dashboard</h2>
                <h3>Welcome, <%= session.getAttribute("username") %></h3>
            </div>
            <a class="logout-btn" href="logout">Logout</a>
        </header>
        
        <div class="container">
            <form class="add-reservation-form" action="receptionist" method="post">
                <input type="hidden" name="action" value="add">
                <label for="guestname">Guest Name</label>
                <input type="text" name="guestName" id="guestname" placeholder="Name" required>
                <label for="username">Username</label>
                <input type="text" name="address" id="username" placeholder="Address">
                <label for="username">Contact Number</label>
                <input type="text" name="contact" required placeholder="Contact Number">

                <label for="username">Available Rooms</label>
                <select name="roomId">
                <%
                    List<Room> rooms = (List<Room>) request.getAttribute("availableRooms");
                    for(Room r : rooms){
                %>
                    <option value="<%= r.getRoomId() %>">
                        <%= r.getRoomNumber() %> - <%= r.getRoomType() %>
                    </option>
                <%
                    }
                %>
                </select>
                
                <label for="username">Check-in Date</label>
                <input type="date" name="checkIn" required>
                <label for="username">Check-out Date</label>
                <input type="date" name="checkOut" required>

                <button type="submit"> Add Reservation</button>
            </form>

            <div class="search">
                <form method="get" action="receptionist">
                    <input type="hidden" name="action" value="search">
                    <input class="search-field" type="text" name="search">
                    <input class="search-btn" type="submit" value="Search">
                </form>

               <table>
                    <tr>
                        <th>Reservation No</th>
                        <th>Guest Name</th>
                        <th>Phone</th>
                        <th>Room Number</th>
                        <th>Room Type</th>
                        <th>Price Per Night</th>
                        <th>Actions</th>
                    </tr>

                    <%
                        if(reservationList != null){
                            for(Reservation r : reservationList){
                    %>
                    <tr>
                        <td><%= r.getReservationNo() %></td>
                        <td><%= r.getGuestName() %></td>
                        <td><%= r.getPhone() %></td>
                        <td><%= r.getRoomNumber() %></td>
                        <td><%= r.getRoomType() %></td>
                        <td><%= r.getPricePerNight() %></td>
                        <td>
                            <a href="receptionist?action=bill&amp;resNo=<%= r.getReservationNo() %>">Calculate Bill</a>
                            <form class="cancel-form" method="post" action="receptionist" style="display:inline;">
                                <input type="hidden" name="action" value="cancel">
                                <input type="hidden" name="resNo" value="<%= r.getReservationNo() %>">
                                <button class="cancel-btn" type="submit">Cancel</button>
                            </form>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </table>
            </div>
        </div>
    </body>
</html>