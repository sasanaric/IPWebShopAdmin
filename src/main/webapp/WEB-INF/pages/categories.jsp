<%@ page import="shop.ipwebshopadmin.dto.Category" %>
<%@ page import="shop.ipwebshopadmin.dto.Attribute" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: snaric
  Date: 19-Aug-23
  Time: 3:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page errorPage="404.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="categoryBean" type="shop.ipwebshopadmin.beans.CategoryBean" scope="session"/>
<html>
<head>
    <title>Kategorije</title>
    <link href="styles/data-table.css" type="text/css" rel="stylesheet">
</head>
<body>
<%@include file="navbar.jsp"%>
<div class="table-container">
    <table style="text-align: center;font-size: 20px;width: 60%">
        <thead>
        <tr>
            <th colspan="6">Lista kategorija</th>
            <th colspan="3"><a href="?action=add-category" class="button-style">Dodaj kategoriju</a></th>
        </tr>
        <tr>
            <th colspan="1">ID</th>
            <th colspan="4" >Naziv</th>
            <th colspan="4">Opcije</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Category category : categoryBean.getAll()) {
        %>
        <tr class="category-row">
            <td colspan="1"><%=category.getId()%></td>
            <td colspan="4"><%=category.getName()%></td>
            <td colspan="4">
                <a href="?action=update-category&id=<%=category.getId()%>" class="button-style">Izmjieni</a>
                <a href="?action=delete-category&id=<%=category.getId()%>" class="button-style">Obriši</a>
                <a href="?action=add-attribute&id=<%=category.getId()%>" class="button-style">Dodaj atribut</a>
            </td>
        </tr>
        <tr>
            <td colspan="9">
                <div class="attributes-grid">
                    <%
                        List<Attribute> attributes = categoryBean.getAttributesByCategory(category.getId());
                        for (Attribute attribute : attributes) {
                    %>
                    <div class="grid-item-name"><%=attribute.getName()%></div>
                    <div class="grid-item-actions">
                        <a href="?action=update-attribute&id=<%=attribute.getId()%>" class="button-style-attribute">Izmijeni</a>
                        <a href="?action=delete-attribute&id=<%=attribute.getId()%>" class="button-style-attribute">Obriši</a>
                    </div>
                    <%
                        }
                    %>
                </div>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
