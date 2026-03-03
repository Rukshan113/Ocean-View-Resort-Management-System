<%@page import="com.mycompany.oceanview.model.User"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    User user = (User) request.getAttribute("user");

    if(user == null){
        response.sendRedirect("admin");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update User</title>
        <link rel="stylesheet" href="styles.css"/>
    </head>
    
    <body class="update-user-page">
        <h2>Update User</h2>

        <form method="post" action="admin">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%=user.getId()%>">

            <label for="username">username</label>
            <input type="text" name="username" value="<%=user.getUsername()%>" id="username" required>

             <label for="email">email</label>
             <input type="email" name="email" value="<%=user.getEmail()%>" id="email" required>

            <label for="role">role</label>
            <select name="role" id="role">
                <option value="admin"<%= "admin".equals(user.getRole()) ? "selected" : "" %>>
                    Admin
                </option>
                <option value="receptionist"<%= "receptionist".equals(user.getRole()) ? "selected" : "" %>>
                    Receptionist
                </option>
            </select>
            <button type="submit"> Update</button>
            <a href="admin">Go Back</a>
        </form>
    </body>
</html>