<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/header.jsp" />

<section class="hero-carousel" id="heroCarousel">
    <div class="carousel-slide slide-corda active">
        <h2>Corda</h2>
        <a href="${pageContext.request.contextPath}/Catalogo?categoria=1" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-slide slide-fiato">
        <h2>Fiato</h2>
        <a href="${pageContext.request.contextPath}/Catalogo?categoria=2" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-slide slide-percussioni">
        <h2>Percussioni</h2>
        <a href="${pageContext.request.contextPath}/Catalogo?categoria=3" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-slide slide-accessori">
        <h2>Accessori</h2>
        <a href="${pageContext.request.contextPath}/Catalogo?categoria=4" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-indicators">
        <span class="dot active" data-slide="0"></span>
        <span class="dot" data-slide="1"></span>
        <span class="dot" data-slide="2"></span>
        <span class="dot" data-slide="3"></span>
    </div>
</section>

<section class="categories-flex">
    <a href="${pageContext.request.contextPath}/Catalogo?categoria=1" class="cat-card">Strumenti a Corda</a>
    <a href="${pageContext.request.contextPath}/Catalogo?categoria=2" class="cat-card">Strumenti a Fiato</a>
    <a href="${pageContext.request.contextPath}/Catalogo?categoria=3" class="cat-card">Strumenti a Percussioni</a>
    <a href="${pageContext.request.contextPath}/Catalogo?categoria=4" class="cat-card">Accessori</a>
</section>

<section class="catalog-section">
    <div class="catalog-header">
        <h2>Prodotti in Evidenza</h2>
    </div>
    
    <div class="catalog-grid">
        <c:choose>
            <c:when test="${not empty prodottiInEvidenza}">
                <c:forEach var="prodotto" items="${prodottiInEvidenza}">
                    <div class="product-card">
                        <div class="product-image">
                            <img src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" alt="${prodotto.modello}">
                        </div>
                        <div class="product-info">
                            <h3>${prodotto.marca} ${prodotto.modello}</h3>
                            <p class="product-desc">${prodotto.descrizione}</p>
                            <p class="product-price">€ ${prodotto.prezzo}</p>
                            
                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/Catalogo?id=${prodotto.idProdotto}" class="btn-details">Dettagli</a>
                                
                                <form action="${pageContext.request.contextPath}/Carrello" method="POST" class="form-add-cart">
                                    <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                                    <input type="hidden" name="quantita" value="1">
                                    <button type="submit" class="btn-gold btn-add-cart">Aggiungi</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            
            <c:otherwise>
                <div class="empty-catalog-msg">
                    <p>I prodotti in evidenza sono in aggiornamento. Visita il catalogo per scoprire le novità!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<jsp:include page="/jsp/footer.jsp" />