<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="cart-section">
    <div class="cart-header">
        <h2>Il tuo Carrello</h2>
        <p>Controlla i tuoi articoli prima di procedere al pagamento.</p>
    </div>

    <div class="cart-container">
        
        <div class="cart-items-list">
            
            <div class="cart-item">
                <img src="https://images.unsplash.com/photo-1514649923863-ceaf75b770ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80" alt="Chitarra Acustica" class="cart-item-img">
                <div class="cart-item-details">
                    <h3>Chitarra Acustica Yamaha</h3>
                    <p class="cart-item-price">€ 249,00</p>
                </div>
                <div class="cart-item-actions">
                    <input type="number" value="1" min="1" class="cart-qty-input">
                    <form action="${pageContext.request.contextPath}/RemoveCartServlet" method="POST" class="remove-form">
                        <input type="hidden" name="idProdotto" value="1">
                        <button type="submit" class="btn-remove">Rimuovi</button>
                    </form>
                </div>
            </div>

            <div class="cart-item">
                <img src="https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80" alt="Pianoforte" class="cart-item-img">
                <div class="cart-item-details">
                    <h3>Pianoforte a Coda Steinway</h3>
                    <p class="cart-item-price">€ 15.500,00</p>
                </div>
                <div class="cart-item-actions">
                    <input type="number" value="1" min="1" class="cart-qty-input">
                    <form action="${pageContext.request.contextPath}/RemoveCartServlet" method="POST" class="remove-form">
                        <input type="hidden" name="idProdotto" value="2">
                        <button type="submit" class="btn-remove">Rimuovi</button>
                    </form>
                </div>
            </div>

        </div>

        <div class="cart-summary">
            <h3>Riepilogo Ordine</h3>
            <div class="summary-row">
                <span>Subtotale:</span>
                <span>€ 15.749,00</span>
            </div>
            <div class="summary-row">
                <span>Spedizione:</span>
                <span>Gratuita</span>
            </div>
            <hr class="summary-divider">
            <div class="summary-row total-row">
                <span>Totale:</span>
                <span>€ 15.749,00</span>
            </div>
            
            <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST">
                <button type="submit" class="btn-gold btn-checkout">Procedi al Pagamento</button>
            </form>
            <a href="${pageContext.request.contextPath}/JSP/catalogo.jsp" class="continue-shopping">Continua con gli acquisti.</a>
        </div>

    </div>
</section>

<jsp:include page="/jsp/footer.jsp" />