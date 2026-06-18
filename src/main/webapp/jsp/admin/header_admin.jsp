<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>ToneMarket - Pannello Amministrazione</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-style.css">
</head>
<body>

<header class="main-header">
    <div class="logo">
			<a href="${pageContext.request.contextPath}/Home">
				<img src="${pageContext.request.contextPath}/images/chiaveViolinoheader.png" alt="Icona ToneMarket" class="logo-img">
				<div>Tone<span>Market</span></div>
			</a>
		</div>
        </div>
        <ul class="admin-menu-links">
            <li><a href="${pageContext.request.contextPath}/jsp/admin/admin.jsp">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/Admin/GestioneProdotti">Catalogo Manager</a></li>
            <li><a href="${pageContext.request.contextPath}/GestioneOrdini">Ordini Manager</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/admin/ordiniAdmin.jsp">Ticket Assistenza</a></li>
            <li><a href="${pageContext.request.contextPath}/Logout" class="admin-logout-link">Esci</a></li>
        </ul>
    </div>
</header>