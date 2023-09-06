<%@ page import="shop.ipwebshopadmin.dto.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="shop.ipwebshopadmin.dto.Avatar" %><%--
  Created by IntelliJ IDEA.
  User: snaric
  Date: 20-Aug-23
  Time: 2:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="404.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="userBean" type="shop.ipwebshopadmin.beans.UserBean" scope="session"/>
<html>
<head>
    <title>Korisnici</title>
    <link href="styles/data-table.css" type="text/css" rel="stylesheet">
</head>
<body>
<%@include file="navbar.jsp"%>
<div class="table-container">
  <table style="width: 100%;text-align: center">
    <thead>
    <tr>
        <th colspan="6">Lista korisnika</th>
        <th colspan="3"><a href="?action=add-user" class="button-style">Dodaj korisnika</a></th>
    </tr>
    <tr>
      <th>ID</th>
      <th>Ime</th>
      <th>Prezime</th>
      <th>Korisničko ime</th>
      <th>Email</th>
      <th>Lokacija</th>
      <th>Nalog aktivan</th>
      <th>Avatar</th>
      <th>Opcije</th>
    </tr>
    </thead>
    <tbody>
    <%
      ArrayList<Avatar> avatars = userBean.getAvatars();
      for (User user : userBean.getAll()) {
    %>
    <tr>
      <td><%=user.getId()%></td>
      <td><%=user.getFirstName()%></td>
      <td><%=user.getLastName()%></td>
      <td><%=user.getUsername()%></td>
      <td><%=user.getEmail()%></td>
      <td><%=user.getLocation()%></td>
      <td><%=user.getAccountConfirmed()?"DA":"NE"%></td>
      <td>
          <% if (user.getAvatarId() != null) { %>
          <img src="<%=avatars.get(user.getAvatarId()-1).getUrl()%>" width="40" height="40" alt="Avatar"/>
          <%}%>
      </td>
        <td>
            <a href="?action=update-user&id=<%=user.getId()%>" class="button-style">Izmjeni</a>
            <a href="?action=deactivate-user&id=<%=user.getId()%>" class="button-style">Deaktiviraj</a>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>
</body>
</html>
