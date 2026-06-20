<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_ordini.css">

<main class="admin-orders-container">
    <div class="orders-header">
        <h2>Dettagli Ordine #${ordine.idOrdine}</h2>
        <a href="${pageContext.request.contextPath}/Admin/GestioneOrdini" class="btn-action btn-secondary">Torna alla lista</a>
    </div>

    <div class="order-details-wrapper">
        <section class="order-info">
            <h3>Informazioni Spedizione</h3>
            <p><strong>Destinatario:</strong> ${ordine.spedizioneNomeCognome}</p>
            <p><strong>Indirizzo:</strong> ${ordine.spedizioneVia}, ${ordine.spedizioneNumeroCivico}</p>
            <p><strong>Città:</strong> ${ordine.spedizioneCitta} (${ordine.spedizioneProvincia}), ${ordine.spedizioneCap}</p>
            <p><strong>Telefono:</strong> ${ordine.spedizioneTelefono}</p>
        </section>

        <section class="order-products">
            <h3>Prodotti Acquistati</h3>
            <table class="orders-table">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Quantità</th>
                        <th>Prezzo Unitario</th>
                        <th>Subtotale</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaProdotti}" var="prodotto">
                        <tr>
                            <td>${prodotto.nome}</td>
                            <td>${prodotto.quantita}</td>
                            <td>€ <fmt:formatNumber value="${prodotto.prezzo}" pattern="#,##0.00" /></td>
                            <td>€ <fmt:formatNumber value="${prodotto.prezzo * prodotto.quantita}" pattern="#,##0.00" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" style="text-align: right;"><strong>Totale Ordine:</strong></td>
                        <td class="fw-bold text-gold">€ <fmt:formatNumber value="${ordine.totaleOrdine}" pattern="#,##0.00" /></td>
                    </tr>
                </tfoot>
            </table>
        </section>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />