<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="product.detail-section">
	<div class="product-container">
		
		<div class="product-image-large">
			<img src="https://images.unsplash.com/photo-1514649923863-ceaf75b770ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Chitarra Acustica Yamaha">
		</div>
		
		<div class="product-details">
			<h2>Chitarra Acustica Yamaha</h2>
			<p class="product-sku">Codice prodotto: YAM-AC-001</p>
			
			<p class="product-price-large">€ 249,00</p>
			
			<div class="product-description">
				<h3>Descrizione</h3>
				<p>bla bla bla</p>
			</div>
		
		<form id="addToCartForm" action="${pageContext.request.contextPath}/CarrelloServlet" method="POST">
			<input type="hidden" name="idProdotto" value="1">
			
			<div class="quantity-selector">
				<label for="quantita">Quantità:</label>
				<input type="number" id="quantita" name="quantita" value="1" min="1" max="10" required>
			</div>
			
			<button type="submit" class="btn-gold add-to-cart-large">Aggiungi al Carrello</button>
		</form>
	</div>
</div>
</section>