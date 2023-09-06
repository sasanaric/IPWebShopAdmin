<%--
  Created by IntelliJ IDEA.
  User: snaric
  Date: 19-Aug-23
  Time: 3:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="404.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="styles/form.css" rel="stylesheet">
    <title>Your Page Title</title>
</head>
<body>
<div class="container">
    <div class="login-frame">
        <div class="login-form">
            <form method="POST" action="?action=login">
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
                <button type="submit" value="Prijavi se" name="submit">Prijavi se</button>
                <h3><%=session.getAttribute("notification")!=null?session.getAttribute("notification").toString():""%></h3> <br />
            </form>
        </div>
    </div>
</div>
</body>
</html>
