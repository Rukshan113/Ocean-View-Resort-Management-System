<%@ page import="java.util.List, com.mycompany.oceanview.User" %>
<%
    List<User> users = (List<User>) request.getAttribute("userList");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="styles.css"/>
    </head>
    <body class="admin_page">
        <header class="admin_page_header">
            <h2>Admin Dashboard</h2>
        </header>
        
        <div class="container">
            <form method="post" action="admin">
                <input type="hidden" name="action" value="create">
                <input type="text" name="username" required placeholder="username">
                <input type="email" name="email" required placeholder="email">
                <input type="password" name="password" required placeholder="password">
                <select name="role">
                    <option value="">-- Select Role --</option>
                    <option value="admin">Admin</option>
                    <option value="manager">Manager</option>
                    <option value="receptionist">Receptionist</option>
                </select>
                <button type="submit"> Create User </button>
            </form>

            <div class="user_search">
                <form method="get" action="admin">
                    <input class="search_field" type="text" name="search">
                    <input class="search_btn" type="submit" value="Search">
                </form>

                <table>
                    <tr>
                        <th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Actions</th>
                    </tr>

                    <%
                        if(users != null){
                            for(User u : users){
                    %>
                    <tr>
                        <td><%=u.getId()%></td>
                        <td><%=u.getUsername()%></td>
                        <td><%=u.getEmail()%></td>
                        <td><%=u.getRole()%></td>
                        <td>
                            <a href="admin?action=edit&amp;id=<%=u.getId()%>">Edit</a>
                            
                            <form class="action_form" method="post" action="admin" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%=u.getId()%>">
                                <button type="submit">Delete</button>
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