<%@ page import="com.mycompany.oceanview.Reservation" %>
<%
    Reservation r = (Reservation) request.getAttribute("reservation");
    Integer nights = (Integer) request.getAttribute("nights");
    Double total = (Double) request.getAttribute("total");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bill</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body class="bill-page">
    <header>
        <h2>Reservation Bill</h2>
    </header>
    <% if(error != null) { %>
        <p style="color:red;"><%= error %></p>
        <a href="receptionist?action=dashboard">Back to Dashboard</a>
    <% } else if(r != null) { %>

    <table border="1" cellpadding="5" cellspacing="0">
        <tr>
            <td>Reservation No:</td>
            <td><%= r.getReservationNo() %></td>
        </tr>
        <tr>
            <td>Guest Name:</td>
            <td><%= r.getGuestName() %></td>
        </tr>
        <tr>
            <td>Phone:</td>
            <td><%= r.getPhone() %></td>
        </tr>
        <tr>
            <td>Room Type:</td>
            <td><%= r.getRoomType() %></td>
        </tr>
        <tr>
            <td>Price per Night:</td>
            <td><%= r.getPricePerNight() %></td>
        </tr>
        <tr>
            <td>Nights Stayed:</td>
            <td><%= nights %></td>
        </tr>
        <tr>
            <td><strong>Total Amount:</strong>
            </td><td><strong><%= total %></strong></td>
        </tr>
    </table>

    <br>
    <button onclick="window.print()">Print Bill</button>
    <a href="receptionist?action=dashboard">Go Back</a>

    <% } %>
</body>
</html>