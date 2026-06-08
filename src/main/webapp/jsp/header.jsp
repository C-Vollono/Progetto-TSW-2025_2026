<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="it">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>ToneMarket - Il miglior negozio di strumenti musicali sul mercato</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
	<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/chiaveViolinotrans.png" sizes="96x96">
	
	<script defer src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
	
	<header class="main-header">
		<div class="logo">
			<a href="${pageContext.request.contextPath}/jsp/index.jsp">
				<img src="${pageContext.request.contextPath}/images/chiaveViolinoheader.png" alt="Icona ToneMarket" class="logo-img">
				<div>Tone<span>Market</span></div>
			</a>
		</div>
		
		<div class="search-bar">
			<input type="text" id="searchInput" placeholder="Cerca ciò che desideri..." autocomplete="off">
			<button type="button" id="searchBtn">Cerca</button>
			<div id="searchResults" class="search-results-dropdown"></div>
		</div>
		
		<div class="user-action">
			<a href="${pageContext.request.contextPath}/jsp/catalogo.jsp" class="btn-nav">Catalogo</a>
			
			<c:choose>
				<c:when test="${empty sessionScope.utenteLoggato}">
					<a href="${pageContext.request.contextPath}/jsp/login.jsp"  class="btn-login">Accedi</a>
					<a href="${pageContext.request.contextPath}/jsp/registrazione.jsp" class="btn-register">Registrati</a>
				</c:when>
				
				<c:otherwise>
					<div class="user-dropdown-container">
						<button class="btn-login dropdown-toggle btn-transparent">
						Benvenuto, ${sessionScope.utenteLoggato.username} &#9662;
						</button>
						
						<div class="user-dropdown-menu">
							<a href="${pageContext.request.contextPath}/jsp/common/profilo.jsp">Il mio Profilo</a>
							
							<c:if test="${sessionScope.ruolo == 'admin'}">
								<a href="${pageContext.request.contextPath}/jsp/admin/admin.jsp">Pannello Admin</a>
							</c:if>
							
							<a href="${pageContext.request.contextPath}/Logout" class="text-danger">Esci</a>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			
			<a href="${pageContext.request.contextPath}/jsp/carrello.jsp" class="btn-cart">Carrello</a>
		</div>
		
		<script>
  			const contextPath = '${pageContext.request.contextPath}';
		</script>
		<script src="${pageContext.request.contextPath}/js/header.js"></script>
	</header>
	
	<main>