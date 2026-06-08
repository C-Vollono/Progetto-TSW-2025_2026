<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/header.jsp" />

<section class="catalog-section">
	<div class="catalog-header">
		<h2>Il nostro Catalogo Premium</h2>
		<p> Esplora il mondo della musica Premium, prodotti di altissima qualità.</p>
	</div>
	
	<c:if test="${not empty messaggioErrore}">
		<div class="error-msg server-error catalog-error-container">
			${messaggioErrore}
		</div>
	</c:if>
	
	<div class="catalog-layout">
		
		<aside class="catalog-sidebar">
			<h3>Filtri</h3>
			<form action="${pageContext.request.contextPath}/Catalogo" method="GET">
				
				<div class="filter-group">
					<label for="categoria">Categoria:</label>
					<select name="categoria" id="categoria">
						<option value="All" ${empty selCategoria || selCategoria == 'All' ? 'selected' : ''}>Tutte</option>
						<option value="Chitarre" ${selCategoria == 'Chitarre' ? 'selected' : ''}>Chitarre</option>
						<option value="Pianoforti" ${selCategoria == 'Pianoforti' ? 'selected' : ''}>Pianoforti</option>
						<option value="Batterie" ${selCategoria == 'Batterie' ? 'selected' : ''}>Batterie</option>
					</select>
				</div>
				
				<div class="filter-group">
					<label for="marca">Marca:</label>
					<select name="marca" id="marca">
						<option value="All" ${empty selMarca || selMarca == 'All' ? 'selected' : ''}>Tutte</option>
						<option value="Yamaha" ${selMarca == 'Yamaha' ? 'selected' : ''}>Yamaha</option>
						<option value="Roland" ${selMarca == 'Roland' ? 'selected' : ''}>Roland</option>
						<option value="Fender" ${selMarca == 'Fender' ? 'selected' : ''}>Fender</option>
					</select>
				</div>
				
				<div class="filter-group">
					<label for="prezzo">Range di prezzo:</label>
					<select name="prezzo" id="prezzo">
						<option value="All" ${empty selPrezzo || selPrezzo == 'All' ? 'selected' : ''}>Tutti i prezzi</option>
						<option value="0-500" ${selPrezzo == '0-500' ? 'selected' : ''}>0€ - 500€</option>
						<option value="500-2000" ${selPrezzo == '500-2000' ? 'selected' : ''}>500€ - 2.000€</option>
						<option value="2000-10000" ${selPrezzo == '2000-10000' ? 'selected' : ''}>2.000€ - 10.000€</option>
					</select>
				</div>
				
				<button type="submit" class="btn-gold filter-btn">Applica filtri</button>
			</form>
		</aside>

		<div class="catalog-main">
		
		<form action="${pageContext.request.contextPath}/Catalogo" method="GET" class="form-topbar-flex">
				<input type="hidden" name="categoria" value="${empty selCategoria ? 'All' : selCategoria}">
				<input type="hidden" name="marca" value="${empty selMarca ? 'All' : selMarca}">
				<input type="hidden" name="prezzo" value="${empty selPrezzo ? 'All' : selPrezzo}">
			
			<div class="catalog-topbar">
				<input type="text" placeholder="Cerca prodotti..." class="catalog-search" name="searchQuery" value="${searchQuery}">
				
				<div id="search-suggestions" class="search-suggestions"></div>
				
				<div class="catalog-sort">
					<label for="ordina">Ordina per:</label>
					<select name="ordina" id="ordina" onchange="this.form.submit()">
						<option value="rilevanza" ${selOrdina == 'rilevanza' ? 'selected' : ''}>Rilevanza</option>
						<option value="prezzo_asc" ${selOrdina == 'prezzo_asc' ? 'selected' : ''}>Prezzo crescente</option>
						<option value="prezzo_desc" ${selOrdina == 'prezzo_desc' ? 'selected' : ''}>Prezzo decrescente</option>
					</select>
				</div>
			</div>
		</form>
			<div class="catalog-grid">
				<c:choose>
					<c:when test="${empty prodottiCatalogo}">
						<div class="empty-catalog-msg">
							<p>Nessun prodotto corrisponde ai criteri di ricerca.</p>
						</div>
					</c:when>
					
					<c:otherwise>
						<c:forEach var="prodotto" items="${prodottiCatalogo}">
							<div class="product-card">
								<div class="product-image">
									<img src="${pageContext.request.contextPath}/images/${prodotto.urlImmagine}" alt="${prodotto.modello}">
								</div>
								
								<div class="product-info">
									<h3>${prodotto.marca} ${prodotto.modello}</h3>
									<p class="product-brand">Categoria: ${prodotto.tipo}</p>
									<p class="product-price">€ ${prodotto.prezzo}</p>
									<p class="product-rating">★★★★★</p> 
									
									<div class="product-actions">
										<a href="${pageContext.request.contextPath}/Catalogo?id=${prodotto.idProdotto}" class="btn-details">Dettagli</a>
										
										<form action="${pageContext.request.contextPath}/Carrello" method="POST" class="form-add-cart-flex">
											<input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
											<button type="submit" class="btn-gold btn-add-cart">Carrello</button>
										</form>
									</div>
								</div>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>

			<c:if test="${not empty prodottiCatalogo}">
				<div class="load-more-container">
					<button class="btn-details load-more-btn">Vedi altro</button>
				</div>
			</c:if>
		</div>
	</div>
	<script>const contextPath = '${pageContext.request.contextPath}';</script>
	<script src="${pageContext.request.contextPath}/js/catalogo-search.js"></script>
</section>

<jsp:include page="/jsp/footer.jsp" />