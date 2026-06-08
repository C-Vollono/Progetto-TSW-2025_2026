<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/header.jsp" />

<section class="form-section">
	<div class="form-card">
		<h2>Registra il tuo Account</h2>
		<p class="form-subtitle">ToneMarket è qui per te</p>
		
		<c:if test="${not empty messaggioErrore}">
			<div class="error-msg server-error">
				${messaggioErrore}
			</div>
		</c:if>
		
		<form id="formRegistrazione" action="${pageContext.request.contextPath}/Registrazione" method="POST">
			<div class="input-group">
				<label for="nome">Nome</label>
				<input type="text" id="nome" name="nome" placeholder="Es. Mario" required autofocus>
				<span id="nomeError" class="error-msg-js"></span>
				</div>
				
			<div class="input-group">
				<label for="cognome">Cognome</label>
				<input type="text" id="cognome" name="cognome" placeholder="Es. Rossi" required>
				<span id="cognomeError" class="error-msg-js"></span>
				</div>
				
			<div class="input-group">
				<label for="username">Username</label>
				<input type="text" id="username" name="username" placeholder="Il tuo username" required>
				<span id="usernameError" class="error-msg-js"></span>
			</div>
			
			<div class="input-group">
				<label for="email">Email</label>
				<input type="email" id="email" name="email" placeholder="mariorossi@email.com" required>
				<span id="emailError" class="error-msg-js"></span>
			</div>
			
			<div class="input-group">
				<label for="dataNascita">Data di Nascita</label>
				<input type="date" id="dataNascita" name="dataNascita" required>
				<span id="dataNascitaError" class="error-msg-js"></span>
			</div>
			
			<div class="input-group">
				<label for="password">Password</label>
				<input type="password" id="password" name="password" placeholder="Min. 8 caratteri, 1 lettera e 1 numero" required>
				<span id="passwordError" class="error-msg-js"></span>
			</div>
				
			<div class="input-group">
				<label for="password">Conferma Password</label>
				<input type="password" id="confermaPassword" name="confermaPassword" placeholder="Ripeti Password" required>
				<span id="confermaPasswordError" class="error-msg-js"></span>
			</div>
			
			<button type="submit" id="btnRegistrati" class="form-btn" disabled>Registrati</button>
		</form>
		
		<div class="form-footer">
			<p>Hai già un account? <a href="${pageContext.request.contextPath}/jsp/login.jsp">Accedi qui</a></p>
			</div>
		</div>
	</section>
	
<jsp:include page="/jsp/footer.jsp" />
	
<script src="${pageContext.request.contextPath}/js/validazione.js"></script>	