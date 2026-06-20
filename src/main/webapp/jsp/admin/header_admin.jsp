<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>ToneMarket - Pannello Amministrazione</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_header.css">
</head>
<body>

<header class="main-admin-header">
    <div class="admin-nav-container">
        
      <div class="admin-brand">
    <a href="${pageContext.request.contextPath}/Home">
        <img src="${pageContext.request.contextPath}/images/chiaveViolinoheader.png" alt="Icona" class="admin-logo-img">
        <!-- Racchiudiamo tutto il testo in uno span principale -->
        <span class="brand-text">Tone<span>Market</span></span>
    </a>
</div>
        
        <ul class="admin-menu-links">
            <li><a href="${pageContext.request.contextPath}/jsp/admin/admin.jsp">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/Admin/GestioneProdotti">Catalogo Manager</a></li>
            <li><a href="${pageContext.request.contextPath}/Admin/GestioneOrdini">Ordini Manager</a></li>
            <li><a href="${pageContext.request.contextPath}/jsp/admin/ordiniAdmin.jsp">Ticket Assistenza</a></li>
        </ul>

        <div class="admin-user-actions">
            <a href="${pageContext.request.contextPath}/Logout" class="btn-logout-admin">Logout</a>
        </div>

    </div>
</header>