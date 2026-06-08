<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>ToneMarket - Il miglior negozio di strumenti musicali sul mercato</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<header>
	<div class="header-container">
		<div class="logo">
			<a href="${pageContext.request.contextPath}/jsp/index.jsp">
				<h1>Tone<span>Market</span></h1>
			</a>
		</div>
		
		<nav>
			<ul>
				<li><a href="${pageContext.request.contextPath}/jsp/index.jsp">Home</a></li>
				<li><a href="${pageContext.request.contextPath}/jsp/catalogo.jsp">Catalogo</a></li>
				
				<li class="search-item">
					<input type="text" id="searchBar" placeholder="Cerca il tuo strumento">
					<div id="searchSuggestions" class="suggestions-box"></div>
					</li>
					
					<li><a href="${pageContext.request.contextPath}/jsp/login.jsp">Accedi</a></li>
					<li><a href="${pageContext.request.contextPath}/jsp/registrazione.jsp">Registrati</a></li>
					<li><a href="${pageContext.request.contextPath}/jsp/carrello.jsp">Carrello</a></li>
					</ul>
				</nav>
			</div>
		</header>
<main>