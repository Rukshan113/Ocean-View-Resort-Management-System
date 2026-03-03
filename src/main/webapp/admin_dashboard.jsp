<%@ page import="java.util.List, com.mycompany.oceanview.User" %>
<%
    List<User> users = (List<User>) request.getAttribute("userList");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="styles.css"/>
    </head>
    <body class="admin_page">
        <header class="admin_page_header">
            <div>
                <h2>Admin Dashboard</h2>
                <h3>Welcome, <%= session.getAttribute("username") %></h3>
            </div>
            <a class="logout-btn" href="logout">Logout</a>
        </header>
        
        <div class="container">
            <form method="post" action="admin">
                <input type="hidden" name="action" value="create">
                
                <label for="username">username</label>
                <input type="text" name="username" id="username" placeholder="Enter username here" required>
                
                <label for="email">email</label>
                <input type="email" name="email" id="email" placeholder="Enter email here" required>
                
                <label for="password">password</label>
                <input type="password" name="password" id="password" placeholder="Enter password here" required>
                
                <label for="role">role</label>
                <select name="role" id="role">
                    <option value="receptionist">Receptionist</option>
                    <option value="admin">Admin</option>
                </select>
                
                <button type="submit"> Create User </button>
            </form>

            <div class="search">
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
                            
                            <form class="delete-form" method="post" action="admin">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%=u.getId()%>">
                                <button class="delete-btn" type="submit" class="delete-btn" onclick="return confirm('Are you sure you want to delete this user?');">
                                    Delete
                                </button>
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