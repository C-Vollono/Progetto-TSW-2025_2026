<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="form-section">
	<div class="form-card">
		<h2>Ottieni il diritto di acquistare da noi!</h2>
		<p class="form-subtitle"> Inserisci le tue credenziali</p>
		
		<span class="error-msg server-error">${erroreLogin}</span> 
	
		<form id="formLogin" action="${pageContext.request.contextPath}/Login" method="POST">
	
			<div class="input-group">
				<label for="email">Email</label>
				<input type="email" id="email" name="email" placeholder="mariorossi@email.com" required autofocus>
			</div>
		
			<div class="input-group">
				<label for="password">Password</label>
				<input type="password" id="password" name="password" placeholder="Inserisci la tua password" required>
			</div>
		
			<button type="submit" class="form-btn">Accedi</button>
		</form>
	
		<div class="form-footer">
			<p>Non hai ancora un account? <a href="${pageContext.request.contextPath}/JSP/registrazione.jsp">Registrati</a></p>
		</div>
	</div>
</section>

<jsp:include page="/jsp/footer.jsp" />