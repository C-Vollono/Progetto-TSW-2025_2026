<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/header.jsp" />

<main class="product-detail-section">
	
	<div class="product-top-container">
		
		<div class="product-gallery">
			<div class="main-image-container">
				<img src="${pageContext.request.contextPath}/images/${prodottoDettaglio.urlImmagine}" id="mainProductImage" alt="${prodottoDettaglio.modello}">
			</div>
			
			<div class="thumbnail-container">
				<img src="${pageContext.request.contextPath}/images/${prodottoDettaglio.urlImmagine}" class="thumbnail-img active-thumb" alt="Miniatura 1" onclick="changeMainImage(this)">
				<img src="${pageContext.request.contextPath}/images/${prodottoDettaglio.urlImmagine}" class="thumbnail-img" alt="Miniatura 2" onclick="changeMainImage(this)">
				<img src="${pageContext.request.contextPath}/images/${prodottoDettaglio.urlImmagine}" class="thumbnail-img" alt="Miniatura 3" onclick="changeMainImage(this)">
			</div>
		</div>

		<div class="product-info-detail">
			<h1>${prodottoDettaglio.marca} ${prodottoDettaglio.modello}</h1>
			
			<div class="product-meta">
				<p><strong>Marca:</strong> ${prodottoDettaglio.marca}</p>
				<p><strong>Modello:</strong> ${prodottoDettaglio.modello}</p>
				<p><strong>Categoria:</strong> ${prodottoDettaglio.tipo}</p>
				
				<c:choose>
					<c:when test="${prodottoDettaglio.quantita > 0}">
						<p class="stock-status in-stock">Disponibile (${prodottoDettaglio.quantita} pezzi)</p>
					</c:when>
					<c:otherwise>
						<p class="stock-status out-of-stock">Esaurito</p>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="product-price-large">
				€ ${prodottoDettaglio.prezzo}
			</div>

			<div class="product-description">
				<p>${prodottoDettaglio.descrizione}</p>
			</div>

			<div class="product-actions-block">
				
				<form action="${pageContext.request.contextPath}/Carrello" method="POST" class="action-form">
					<input type="hidden" name="azione" value="aggiungi">
					
					<input type="hidden" name="idProdotto" value="${prodottoDettaglio.idProdotto}">
					
					<div class="quantity-selector">
						<label for="quantita">Q.tà:</label>
						<input type="number" id="quantita" name="quantita" value="1" min="1" max="${prodottoDettaglio.quantita}" ${prodottoDettaglio.quantita == 0 ? 'disabled' : ''}>
					</div>
					
					<button type="submit" class="btn-gold btn-large" ${prodottoDettaglio.quantita == 0 ? 'disabled' : ''}>
						Aggiungi al Carrello
					</button>
				</form>

				<form action="${pageContext.request.contextPath}/Preferiti" method="POST" class="action-form">
					<input type="hidden" name="idProdotto" value="${prodottoDettaglio.idProdotto}">
					<button type="submit" class="btn-wishlist">
						&#10084; Aggiungi ai Preferiti
					</button>
				</form>
				
			</div>
		</div>
	</div>

	<div class="product-reviews-section">
		<h2>Recensioni dei Clienti</h2>
		<div class="reviews-container">
			<p class="no-reviews-msg">Ancora nessuna recensione per questo prodotto. Sii il primo a scriverne una!</p>
		</div>
	</div>

	<div class="related-products-section">
		<h2>Prodotti Correlati</h2>
		<div class="catalog-grid">
			<p class="no-reviews-msg grid-span-all">(I prodotti correlati appariranno qui)</p>
		</div>
	</div>

</main>

<jsp:include page="/jsp/footer.jsp" />