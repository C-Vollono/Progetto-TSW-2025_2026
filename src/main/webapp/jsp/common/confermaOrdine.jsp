<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/confermaOrdine.css">

<div class="no-print">
    <jsp:include page="/jsp/header.jsp" />
</div>

<main class="confirmation-wrapper">
    <div class="confirmation-card">
        
        <div class="success-icon">✓</div>
        <h2>Ordine Confermato!</h2>
        
        <c:choose>
            <c:when test="${not empty sessionScope.messaggioSuccesso}">
                <p class="confirmation-message">${sessionScope.messaggioSuccesso}</p>
                <c:remove var="messaggioSuccesso" scope="session" />
            </c:when>
            <c:otherwise>
                <p class="confirmation-message">Il tuo ordine è stato registrato con successo.</p>
            </c:otherwise>
        </c:choose>

        <div class="confirmation-details">
            <p>Ti abbiamo inviato un'email con il riepilogo dell'ordine.</p>
            <p>Puoi seguire lo stato della spedizione e visualizzare i dettagli dei prodotti direttamente dal tuo profilo.</p>
        </div>
        
        <c:if test="${not empty sessionScope.ricevuta_idOrdine}">
            <div class="receipt-box">
                <div class="receipt-header">
                    <h3>Ricevuta Ordine #${sessionScope.ricevuta_idOrdine}</h3>
                </div>
                <div class="receipt-info">
                    <p><strong>Intestatario:</strong> ${sessionScope.ricevuta_nome}</p>
                    <p><strong>Spedizione a:</strong> ${sessionScope.ricevuta_indirizzo}</p>
                    <p><strong>Pagamento:</strong> ${sessionScope.ricevuta_metodo}</p>
                </div>
                
                <table class="receipt-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th class="text-center">Qtà</th>
                            <th class="text-right">Prezzo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${sessionScope.ricevuta_elementi}">
                            <tr>
                                <td>${item.key.marca} ${item.key.modello}</td>
                                <td class="text-center">${item.value}</td>
                                <td class="text-right">&euro; <fmt:formatNumber value="${item.key.prezzo * item.value}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="2" class="text-right total-label">Totale Pagato:</td>
                            <td class="text-right total-amount">&euro; <fmt:formatNumber value="${sessionScope.ricevuta_totale}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <c:remove var="ricevuta_idOrdine" scope="session"/>
            <c:remove var="ricevuta_nome" scope="session"/>
            <c:remove var="ricevuta_indirizzo" scope="session"/>
            <c:remove var="ricevuta_metodo" scope="session"/>
            <c:remove var="ricevuta_elementi" scope="session"/>
            <c:remove var="ricevuta_totale" scope="session"/>
        </c:if>

        <div class="confirmation-actions no-print">
            <a href="${pageContext.request.contextPath}/Home" class="btn-gold">Torna agli Acquisti</a>
            <a href="${pageContext.request.contextPath}/Profilo#ordini" class="btn-secondary">Vai ai Miei Ordini</a>
            <button id="btnPrint" class="btn-secondary">Stampa Ricevuta</button>
        </div>
        
        <div class="print-only-footer">
            <p><strong>ToneMarket Premium Instruments</strong></p>
            <p>Via della Musica 123, Napoli (NA)</p>
            <p><em>Documento valido come ricevuta d'acquisto non fiscale. Conservare per la garanzia.</em></p>
        </div>
        
    </div>
</main>

<div class="no-print">
    <jsp:include page="/jsp/footer.jsp" />
</div>

<script defer src="${pageContext.request.contextPath}/js/confermaOrdine.js"></script>