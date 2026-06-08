<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/header.jsp" />

<section class="hero-carousel" id="heroCarousel">
    <div class="carousel-slide slide-corda active">
        <h2>Corda</h2>
        <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?categoria=corda" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-slide slide-fiato">
        <h2>Fiato</h2>
        <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?categoria=fiato" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-slide slide-percussioni">
        <h2>Percussioni</h2>
        <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?categoria=percussione" class="btn-gold">Scopri la collezione</a>
    </div>
    
    <div class="carousel-indicators">
        <span class="dot active" data-slide="0"></span>
        <span class="dot" data-slide="1"></span>
        <span class="dot" data-slide="2"></span>
    </div>
</section>

<section class="categories-flex">
    <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?cat=fiato" class="cat-card">Strumenti a Fiato</a>
    <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?categoria=corda" class="cat-card">Strumenti a Corda</a>
    <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?cat=percussione" class="cat-card">Strumenti a Percussioni</a>
    <a href="${pageContext.request.contextPath}/jsp/catalogo.jsp?cat=accessori" class="cat-card">Accessori</a>
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
                            <img src="${prodotto.immagine}" alt="${prodotto.nome}">
                        </div>
                        <div class="product-info">
                            <h3>${prodotto.nome}</h3>
                            <p class="product-desc">${prodotto.descrizione}</p>
                            <p class="product-price">€ ${prodotto.prezzo}</p>
                            
                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/jsp/prodotto.jsp?id=${prodotto.id}" class="btn-details">Dettagli</a>
                                
                                <form action="${pageContext.request.contextPath}/CarrelloServlet" method="POST" class="form-add-cart">
                                    <input type="hidden" name="idProdotto" value="${prodotto.id}">
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