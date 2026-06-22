<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/checkout.css">

<jsp:include page="/jsp/header.jsp" />

<main class="checkout-wrapper">
    <div class="checkout-header">
        <h2>Checkout Sicuro</h2>
        <p>Completa il tuo ordine PREMIUM in pochi passi.</p>
    </div>

    <form action="${pageContext.request.contextPath}/ConfermaOrdine" method="POST" id="formCheckout" class="checkout-container" novalidate>
        
        <div class="checkout-left">
            
            <div class="checkout-card">
                <h3>1. Indirizzo di Spedizione</h3>
                
                <div class="radio-grid">
                    <c:forEach var="ind" items="${listaSpedizioni}" varStatus="loop">
                        <label class="radio-card">
                            <input type="radio" name="idSpedizione" value="${ind.idSpedizione}" class="radio-input" ${loop.first ? 'checked' : ''} required>
                            <div class="radio-content">
                                <span class="radio-title">${ind.via}, ${ind.numeroCivico}</span>
                                <span class="radio-desc">${ind.citta} (${ind.provincia}) - ${ind.cap}</span>
                            </div>
                        </label>
                    </c:forEach>
                    
                    <label class="radio-card">
                        <input type="radio" name="idSpedizione" value="nuovo" class="radio-input" ${empty listaSpedizioni ? 'checked' : ''} required>
                        <div class="radio-content">
                            <span class="radio-title">+ Aggiungi un nuovo indirizzo</span>
                        </div>
                    </label>
                </div>

				<div id="boxNuovaSpedizione" class="checkout-new-form">
                    <div class="indirizzi-form-grid">
                        <div class="input-group-settings">
                            <label for="nuovaVia">Via / Piazza</label>
                            <input type="text" id="nuovaVia" name="nuovaVia" placeholder="Es. Via Roma">
                            <span id="errNuovaVia" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovoCivico">Numero civico</label>
                            <input type="text" id="nuovoCivico" name="nuovoCivico" placeholder="Es. 12">
                            <span id="errNuovoCivico" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovaCitta">Città</label>
                            <input type="text" id="nuovaCitta" name="nuovaCitta" placeholder="Es. Napoli">
                            <span id="errNuovaCitta" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovaProvincia">Provincia</label>
                            <input type="text" id="nuovaProvincia" name="nuovaProvincia" placeholder="Es. NA" maxlength="2">
                            <span id="errNuovaProvincia" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovoCap">CAP</label>
                            <input type="text" id="nuovoCap" name="nuovoCap" placeholder="Es. 80053" maxlength="5">
                            <span id="errNuovoCap" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovoTelefono">Telefono (Opzionale)</label>
                            <input type="tel" id="nuovoTelefono" name="nuovoTelefono" placeholder="Es. 3331234567" maxlength="10">
                            <span id="errNuovoTelefono" class="error-msg-js"></span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="checkout-card">
                <h3>2. Metodo di Pagamento</h3>
                
                <div class="radio-grid">
                    <c:forEach var="carta" items="${listaPagamenti}" varStatus="loop">
                        <label class="radio-card">
                            <input type="radio" name="idPagamento" value="${carta.idPagamento}" class="radio-input" ${loop.first ? 'checked' : ''} required>
                            <div class="radio-content">
                                <span class="radio-title">${carta.circuitoCarta} terminante in ${carta.numeroCartaOscurato.substring(carta.numeroCartaOscurato.length() - 4)}</span>
                                <span class="radio-desc">Intestatario: ${carta.intestatario}</span>
                            </div>
                        </label>
                    </c:forEach>

                    <label class="radio-card">
                        <input type="radio" name="idPagamento" value="nuovo" class="radio-input" ${empty listaPagamenti ? 'checked' : ''} required>
                        <div class="radio-content">
                            <span class="radio-title">+ Aggiungi una nuova carta</span>
                        </div>
                    </label>
                </div>

                <div id="boxNuovoPagamento" class="checkout-new-form">
                    <div class="indirizzi-form-grid">
                        <div class="input-group-settings">
                            <label for="nuovoCircuito">Circuito</label>
                            <select id="nuovoCircuito" name="nuovoCircuito">
                                <option value="" disabled selected>Seleziona...</option>
                                <option value="Visa">Visa</option>
                                <option value="Mastercard">Mastercard</option>
                                <option value="American Express">American Express</option>
                                <option value="PayPal">PayPal</option>
                            </select>
                            <span id="errNuovoCircuito" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovoNumeroCarta">Numero carta</label>
                            <input type="text" id="nuovoNumeroCarta" name="nuovoNumeroCarta" placeholder="Es. 1234 5678 9012 3456" maxlength="19">
                            <span id="errNuovoNumeroCarta" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovoIntestatario">Intestatario</label>
                            <input type="text" id="nuovoIntestatario" name="nuovoIntestatario" placeholder="Es. Mario Rossi">
                            <span id="errNuovoIntestatario" class="error-msg-js"></span>
                        </div>
                        <div class="input-group-settings">
                            <label for="nuovaScadenza">Scadenza</label>
                            <input type="month" id="nuovaScadenza" name="nuovaScadenza">
                            <span id="errNuovaScadenza" class="error-msg-js"></span>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>

        <div class="checkout-right">
            <div class="checkout-summary-card">
                <h3>Riepilogo Ordine</h3>
                
                <div class="summary-items">
                    <c:forEach var="item" items="${sessionScope.carrello.elementi}">
                        <div class="summary-item">
                            <div class="summary-item-info">
                                <span class="summary-item-name">${item.key.marca} ${item.key.modello}</span>
                                <span class="summary-item-qty">Qtà: ${item.value}</span>
                            </div>
                            <span class="summary-item-price">&euro; <fmt:formatNumber value="${item.key.prezzo * item.value}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-totals">
                    <div class="summary-row">
                        <span>Subtotale (${sessionScope.carrello.totalePezzi} articoli)</span>
                        <span>&euro; <fmt:formatNumber value="${sessionScope.carrello.prezzoTotale}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                    <div class="summary-row">
                        <span>Spedizione</span>
                        <span class="text-success">Gratis</span>
                    </div>
                    <div class="summary-row summary-grand-total">
                        <span>Totale da Pagare</span>
                        <span>&euro; <fmt:formatNumber value="${sessionScope.carrello.prezzoTotale}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                </div>

                <button type="submit" class="btn-gold form-btn">Conferma e Paga</button>
                
                <p class="summary-secure-note">🔒 Pagamento sicuro e crittografato.</p>
            </div>
        </div>
    </form>
</main>

<script defer src="${pageContext.request.contextPath}/js/checkout.js"></script>

<jsp:include page="/jsp/footer.jsp" />
