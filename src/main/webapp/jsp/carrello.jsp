<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/carrello.css">

<jsp:include page="/jsp/header.jsp" />

<section class="cart-section">
    <div class="cart-header">
        <h2>Il tuo Carrello</h2>
        <p>Controlla i tuoi articoli prima di procedere al pagamento.</p>
    </div>

    <c:if test="${not empty sessionScope.messaggioErrore}">
        <div class="error-msg server-error catalog-error-container cart-msg">
            ${sessionScope.messaggioErrore}
        </div>
        <c:remove var="messaggioErrore" scope="session" />
    </c:if>
    
    <c:if test="${not empty sessionScope.messaggioSuccesso}">
        <div class="success-msg cart-msg">
            ${sessionScope.messaggioSuccesso}
        </div>
        <c:remove var="messaggioSuccesso" scope="session" />
    </c:if>

    <div class="cart-container">
        <div class="cart-items-list">
            <c:choose>
                <c:when test="${empty sessionScope.carrello || empty sessionScope.carrello.elementi}">
                    <div class="empty-cart-message">
                        <h3>Il tuo carrello è vuoto</h3>
                        <p>Non hai ancora aggiunto nessuno strumento musicale.</p>
                        <a href="${pageContext.request.contextPath}/Catalogo" class="btn-gold">Torna al Catalogo</a>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="item" items="${sessionScope.carrello.elementi}">
                        <div class="cart-item">
                            
                            <img src="${pageContext.request.contextPath}/${item.key.urlImmagine}" alt="${item.key.modello}" class="cart-item-img">
                            <div class="cart-item-details">
                                <h3>${item.key.marca} ${item.key.modello}</h3>
                                <p class="item-cat">Categoria: ${item.key.tipo}</p>
                                <p class="cart-item-price">€ <fmt:formatNumber value="${item.key.prezzo}" pattern="#,##0.00"/></p>
                            </div>
                            
                            <div class="cart-item-actions">
                                <form action="${pageContext.request.contextPath}/Carrello" method="POST" class="qty-form">
                                    <input type="hidden" name="azione" value="modifica">
                                    <input type="hidden" name="idProdotto" value="${item.key.idProdotto}">
                                    
                                    <button type="submit" name="quantita" value="${item.value - 1}" class="btn-qty" ${item.value <= 1 ? 'disabled' : ''}>-</button>
                                    <input type="text" value="${item.value}" readonly class="input-qty">
                                    <button type="submit" name="quantita" value="${item.value + 1}" class="btn-qty">+</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/Carrello" method="POST" class="remove-form">
                                    <input type="hidden" name="azione" value="rimuovi">
                                    <input type="hidden" name="idProdotto" value="${item.key.idProdotto}">
                                    <button type="submit" class="btn-remove">Rimuovi</button>
                                </form>
                            </div>
						</div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not empty sessionScope.carrello && not empty sessionScope.carrello.elementi}">
            <div class="cart-summary">
                <h3>Sommario ordine</h3>
                
                <div class="summary-row">
                    <span>Totale articoli:</span>
                    <span>€ <fmt:formatNumber value="${sessionScope.carrello.prezzoTotale}" pattern="#,##0.00"/></span>
                </div>
                
                <div class="summary-row">
                    <span>Costi di spedizione:</span>
                    <span>Gratuita</span>
                </div>
                
                <hr class="summary-divider">
                
                <div class="summary-row total-row">
                    <span>Totale ordine:</span>
                    <span>€ <fmt:formatNumber value="${sessionScope.carrello.prezzoTotale}" pattern="#,##0.00"/></span>
                </div>
                
                <c:choose>
                    <c:when test="${not empty sessionScope.utente}">
                        <form action="${pageContext.request.contextPath}/Checkout" method="POST">
                            <button type="submit" class="btn-gold btn-checkout">Procedi al checkout</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="checkout-login-warning">
                            <p>Devi effettuare il login per procedere.</p>
                            <a href="${pageContext.request.contextPath}/Login" class="btn-gold login-btn-cart">Accedi per il Checkout</a>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </c:if>

    </div>
</section>

<jsp:include page="/jsp/footer.jsp" />