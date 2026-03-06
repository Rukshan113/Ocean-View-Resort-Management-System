<%
    String role = (String) session.getAttribute("role");
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="styles.css"/>
    </head>
    <body class="help-page">
        <h1>Ocean View Resort Reservation System - Help</h1>

        <p>This page provides guidance for hotel staff on how to use the reservation management system effectively.</p>

        <h3>1. Login</h3>
        <p>
            Enter email and password on the login page.
            If it is your first login, the system will require you to change your password for security.
        </p>

        <h3>2. User Management (Admin Only)</h3>
        <ul>
            <li>Add new system users.</li>
            <li>Search existing users.</li>
            <li>Update user information.</li>
            <li>Delete users when necessary.</li>
        </ul>

        <h3>3. Reservation Management</h3>
        <ul>
            <li>Add new reservations by entering guest details.</li>
            <li>Select the room type and enter check-in and check-out dates.</li>
        </ul>

        <h3>4. Search Reservation</h3>
        <p>
            Staff members can search reservations using the reservation number or guest name.
            This allows quick retrieval of reservation information.
        </p>

        <h3>5. Billing</h3>
        <p>
            The system automatically calculates the bill based on the room type and the number of nights stayed.
            Staff can review and print the generated bill for the guest.
        </p>

        <h3>6. Logout</h3>
        <p>
            Always logout from the system after completing your work to ensure system security.
        </p>

        <% if("admin".equals(role)) { %>
            <a href="admin" class="back">Go Back</a>
        <% } else { %>
            <a href="receptionist" class="back">Go Back</a>
        <% } %>
    </body>
    </html>
