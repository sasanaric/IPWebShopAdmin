<%@ page import="shop.ipwebshopadmin.dto.Avatar" %>
<%--
  Created by IntelliJ IDEA.
  User: snaric
  Date: 20-Aug-23
  Time: 11:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="404.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="userBean" type="shop.ipwebshopadmin.beans.UserBean" scope="session"/>
<html>
<head>
    <title>Dodaj korisnika</title>
    <link href="styles/form.css" rel="stylesheet">
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="container">
    <div class="login-frame">
        <div class="login-form">
            <form method="POST" action="?action=add-user">
                <div class="form-row">
                    Ime <br />
                    <label for="firstName">
                        <input type="text" name="firstName" id="firstName" /><br />
                    </label>
                </div>
                <div class="form-row">
                    Prezime <br />
                    <label for="lastName">
                        <input type="text" name="lastName" id="lastName" /><br />
                    </label>
                </div>
                <div class="form-row">
                    Korisničko ime<br />
                    <label for="username">
                        <input type="text" name="username" id="username" /><br />
                    </label>
                </div>
                <div class="form-row">
                    Lozinka <br />
                    <label for="password">
                        <input type="password" name="password" id="password" /><br />
                    </label>
                </div>
                <div class="form-row">
                    Email <br />
                    <label for="email">
                        <input type="text" name="email" id="email" /><br />
                    </label>
                </div>
                <div class="form-row">
                    Lokacija <br />
                    <label for="location">
                        <input type="text" name="location" id="location" /><br />
                    </label>
                </div>
                <div class="form-row">Avatar</div>
                <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px;">
                    <% for(Avatar avatar : userBean.getAvatars()) { %>
                    <input class="avatar-radio" type="radio" name="avatarId" id="avatar<%=avatar.getId()%>" value="<%=avatar.getId()%>" style="display: none;">
                    <label for="avatar<%=avatar.getId()%>">
                        <img src="<%=avatar.getUrl()%>" alt="Avatar <%=avatar.getId()%>" style="width: 50px;" class="avatar-img">
                    </label>
                    <% } %>
                </div>

                <button type="submit" value="Registruj nalog" name="submit">Registruj se</button>
                <h3><%=session.getAttribute("notification").toString() %></h3> <br />
            </form>
        </div>
    </div>
</div>
</body>
</html>
