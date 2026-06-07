<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/JSP/header.jsp" />

<section class="catalog-section">
	<div class="catalog-header">
		<h2>Il nostro Catalogo Premium</h2>
		<p> Esplora il mondo della musica Premium, prodotti di altissima qualità.</p>
	</div>
	
	<div class="catalog-grid">
		
		<div class="product-card">
			<div class="product-image">
				<img src="https://images.unsplash.com/photo-1514649923863-ceaf75b770ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80" alt="Chitarra Acustica">
			</div>
		<div class="product-info">
			<h3>Chitarra Acustica Yamaha</h3>
			<p class="product-desc">Suono caldo e bla bla bla</p>
			<p class="product-price">€ 249,00</p>
			
			<div class="product-action">
				<a href="${pageContext.request.contextPath}/JSP/prodotto.jsp?id=1" class="btn-details">Dettagli</a>
				<button class="btn-gold add-to-cart">Aggiungi al carrello</button>
			</div>
		</div>
	</div>
		<div class="product-card">
			<div class="product-image">
				<img src="https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80" alt="Pianoforte a Coda">
			</div>
			<div class="product-info">
				<h3>Pianoforte a Coda Steinway</h3>
				<p class="product-desc">Eccellenza ecc.</p>
				<p class="product-price">€ 15.500,00</p>
				
				<div class="product-actions">
					<a href="${pageContext.request.contextPath}/JSP/prodotto.jsp?id=2" class="btn-details"></a>
					<button class="btn-gold add-to-cart">Aggiungi al carrello</button>
				</div>
			</div>
		</div>
		
			<div class="product-card">
            	<div class="product-image">
                	<img src="https://images.unsplash.com/photo-1514649923863-ceaf75b770ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80" alt="Batteria Elettronica">
            </div>
            <div class="product-info">
                <h3>Batteria Roland TD-1K</h3>
                <p class="product-desc">Suona ecc.</p>
                <p class="product-price">€ 499,00</p>
                
                <div class="product-actions">
                    <a href="${pageContext.request.contextPath}/JSP/prodotto.jsp?id=3" class="btn-details">Dettagli</a>
                    <button class="btn-gold add-to-cart">Aggiungi al carrello</button>
                </div>
            </div>
        </div>
	</div>
</section>

<jsp:include page="/JSP/footer.jsp" />