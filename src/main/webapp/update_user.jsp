<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.mycompany.oceanview.User" %>

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
</head>
<body>

<h2>Update User</h2>

<form method="post" action="admin">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="<%=user.getId()%>">

    Username:
    <input type="text" name="username"
           value="<%=user.getUsername()%>" required><br><br>

    Email:
    <input type="email" name="email"
           value="<%=user.getEmail()%>" required><br><br>

    Role:
    <select name="role">
        <option value="admin"
            <%= "admin".equals(user.getRole()) ? "selected" : "" %>>
            Admin
        </option>
        <option value="manager"
            <%= "manager".equals(user.getRole()) ? "selected" : "" %>>
            Manager
        </option>
        <option value="receptionist"
            <%= "receptionist".equals(user.getRole()) ? "selected" : "" %>>
            Receptionist
        </option>
    </select><br><br>

    <input type="submit" value="Update">
</form>

<br>
<a href="admin">Back</a>

</body>
</html>