<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="admin-section">
	<div class="admin-header">
		<h2>Pannello Admin</h2>
		<p>Gestione e-commerce ToneMarket.</p>
	</div>
	
	<div class="admin-container">
	
		<div class="admin-card form-container">
			<h3>Aggiungi Nuovo Prodotto</h3>
			<form action="${pageContext.request.contextPath}/AggiungiProdottoServlet" method="POST">
			
				<div class="input-group">
					<label for="nomeProdotto">Nome Strumento</label>
					<input type="text" id="nomeProdotto" name="nome" placeholder="Es. Fender Stratocaster" required>
				</div>
				
				<div class="input-group">
					<label for="prezzo">Prezzo (€)</label>
					</div></form></div></div></section>