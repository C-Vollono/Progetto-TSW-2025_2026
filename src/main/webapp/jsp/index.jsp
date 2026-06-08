<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/JSP/header.jsp" />

<section class="hero-section">
	<h1>Benvenuto su Tone<span>Market</span></h1>
		<p>Il negozio adatto per musicisti esigenti.<br>Esplora il mondo della musica e i suoi strumenti unici</p>
		
		<a href="${pageContext.request.contextPath}/JSP/catalogo.jsp" class="btn-gold">
			Esplora il nostro Catalogo
		</a>
</section>

<jsp:include page="/JSP/footer.jsp" />