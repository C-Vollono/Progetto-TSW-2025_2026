<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_ordini.css">

<main class="admin-orders-container">
    
    <div class="wireframe-box">
        <div class="wireframe-header">
            <h2>Dettagli Ordine (Ordine selezionato)</h2>
            <a href="${pageContext.request.contextPath}/Admin/GestioneOrdini" class="btn-back">Torna indietro</a>
        </div>
        
        <div class="wireframe-grid">
            <div class="wireframe-left">
                <div class="wireframe-field">
                    <span class="label">ID Ordine:</span>
                    <span class="value">#${ordine.idOrdine}</span>
                </div>
                
                <div class="wireframe-field">
                    <span class="label">Nome Cliente:</span>
                    <span class="value">${ordine.spedizioneNomeCognome}</span>
                </div>
                
                <div class="wireframe-field">
                    <span class="label">Indirizzo di Consegna:</span>
                    <span class="value">${ordine.spedizioneVia}, ${ordine.spedizioneNumeroCivico}, ${ordine.spedizioneCap} ${ordine.spedizioneCitta} (${ordine.spedizioneProvincia})</span>
                </div>
                
                <div class="wireframe-field">
                    <span class="label">Lista oggetti:</span>
                    <ul class="wireframe-list">
                        <c:forEach items="${articoliOrdine}" var="prodotto">
                            <li>${prodotto.idProdotto} (x${prodotto.quantita}) - € <fmt:formatNumber value="${prodotto.prezzoUnitarioStorico * prodotto.quantita}" pattern="#,##0.00" /></li>
                        </c:forEach>
                    </ul>
                </div>
                
                <div class="wireframe-field">
                    <span class="label">Totale ordine:</span>
                    <span class="value total">€ <fmt:formatNumber value="${ordine.totaleOrdine}" pattern="#,##0.00" /></span>
                </div>
            </div>

            <div class="wireframe-right">
                <div class="wireframe-field">
                    <span class="label">Stato Attuale:</span>
                    <span class="value">${ordine.statoOrdine}</span>
                </div>
                
                <div class="wireframe-form">
                    <span class="label">Aggiorna Stato:</span>
                    <form action="${pageContext.request.contextPath}/Admin/GestioneOrdini" method="POST">
                        <input type="hidden" name="action" value="cambiaStato">
                        <input type="hidden" name="idOrdine" value="${ordine.idOrdine}">
                        
                        <select name="stato" class="wireframe-select">
                            <option value="In elaborazione" ${ordine.statoOrdine == 'In elaborazione' ? 'selected' : ''}>In elaborazione</option>
                            <option value="Spedito" ${ordine.statoOrdine == 'Spedito' ? 'selected' : ''}>Spedito</option>
                            <option value="Consegnato" ${ordine.statoOrdine == 'Consegnato' ? 'selected' : ''}>Consegnato</option>
                            <option value="Annullato" ${ordine.statoOrdine == 'Annullato' ? 'selected' : ''}>Annullato</option>
                        </select>
                        
                        <button type="submit" class="btn-wireframe-submit">Aggiorna Stato dell'Ordine</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
</main>

<jsp:include page="/jsp/footer.jsp" />