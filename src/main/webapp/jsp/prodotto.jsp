<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/prodotto.css">

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

				<form action="${pageContext.request.contextPath}/Profilo" method="POST" class="action-form">
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
    
    		<c:choose>
        		<c:when test="${not empty sessionScope.utenteLoggato}">
            		<div class="review-form-container">
                		<h3>Scrivi una recensione</h3>
                		<form action="${pageContext.request.contextPath}/Recensione" method="POST" id="form-recensione">
                    		<input type="hidden" name="azione" value="inserisci">
                    		<input type="hidden" name="idProdotto" value="${prodottoDettaglio.idProdotto}">
                    
                    	<div class="form-group-review">
                        	<label for="titoloRecensione">Titolo</label>
                        	<input type="text" id="titoloRecensione" name="titolo" required placeholder="Riassumi la tua esperienza" maxlength="100">
                    	</div>

                    	<div class="form-group-review">
                        	<label for="valutazione">Valutazione</label>
                        	<select name="valutazione" id="valutazione" required>
                            	<option value="5">5 Stelle (Eccellente)</option>
                            	<option value="4">4 Stelle (Molto Buono)</option>
                            	<option value="3">3 Stelle (Nella media)</option>
                            	<option value="2">2 Stelle (Scarso)</option>
                            	<option value="1">1 Stella (Pessimo)</option>
                        	</select>
                    	</div>

                    	<div class="form-group-review">
                        	<label for="corpoRecensione">Recensione</label>
                        	<textarea id="corpoRecensione" name="corpo" rows="4" required placeholder="Condividi i dettagli..."></textarea>
                    	</div>

                    	<button type="submit" class="btn-gold">Pubblica Recensione</button>
                	</form>
            	</div>
        	</c:when>
        	<c:otherwise>
            	<div class="review-login-prompt">
                	<p><a href="${pageContext.request.contextPath}/Login" class="btn-login-review">Accedi</a> per scrivere una recensione.</p>
            	</div>
        	</c:otherwise>
    	</c:choose>

    	<div class="reviews-list">
        	<c:choose>
            	<c:when test="${empty recensioniProdotto}">
                	<p class="no-reviews-msg">Ancora nessuna recensione. Sii il primo a scriverne una!</p>
            	</c:when>
            	<c:otherwise>
                	<c:forEach var="rec" items="${recensioniProdotto}">
                    	<div class="review-item">
                        	<div class="review-rating">
                            	<c:forEach begin="1" end="5" var="i">
                                	<span class="star ${i <= rec.valutazione ? 'filled' : 'empty'}">★</span>
                            	</c:forEach>
                        	</div>
                        	<h4 class="review-title">${rec.titolo}</h4>
                        	<p class="review-body">${rec.corpo}</p>
                    	</div>
                	</c:forEach>
            	</c:otherwise>
       		</c:choose>
    	</div>
	</div>

	<div class="related-products-section">
		<h2>Prodotti Correlati</h2>
		<div class="catalog-grid">
			<p class="no-reviews-msg grid-span-all">(I prodotti correlati appariranno qui)</p>
		</div>
	</div>

</main>

<script src="${pageContext.request.contextPath}/js/prodotto.js"></script>

<jsp:include page="/jsp/footer.jsp" />