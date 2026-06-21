<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_ordini.css">

<main class="admin-orders-container">
    
    <div class="orders-header">
        <div class="orders-header-title">
            <h2>Gestione Ordini <span class="premium-text">Premium</span></h2>
        </div>
    </div>

    <c:if test="${not empty sessionScope.messaggioSuccesso}">
        <div class="alert alert-success">${sessionScope.messaggioSuccesso}</div>
        <c:remove var="messaggioSuccesso" scope="session"/>
    </c:if>

    <div class="table-responsive">
        <table class="orders-table">
            <thead>
                <tr>
                    <th>ID Ordine</th>
                    <th>Cliente</th>
                    <th>Data</th>
                    <th>Totale</th>
                    <th>Stato</th>
                    <th class="text-center">Azioni</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty listaOrdiniTotali}">
                        <tr>
                            <td colspan="6" class="text-center empty-table">Nessun ordine trovato.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="ordine" items="${listaOrdiniTotali}">
                            <tr>
                                <td class="fw-bold">#${ordine.idOrdine}</td>
                                <td>${ordine.spedizioneNomeCognome}</td>
                                <td><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy HH:mm" /></td>
                                <td class="fw-bold text-gold">€ <fmt:formatNumber value="${ordine.totaleOrdine}" pattern="#,##0.00" /></td>
                               
                                <td>
								    <span class="status-badge 
								        <c:choose>
								           
								            <c:when test="${fn:toLowerCase(ordine.statoOrdine) == 'in elaborazione'}">status-elaborazione</c:when>
								            <c:when test="${fn:toLowerCase(ordine.statoOrdine) == 'spedito'}">status-spedito</c:when>
								            <c:when test="${fn:toLowerCase(ordine.statoOrdine) == 'consegnato'}">status-consegnato</c:when>
								            <c:when test="${fn:toLowerCase(ordine.statoOrdine) == 'annullato'}">status-annullato</c:when>
								            <c:when test="${fn:toLowerCase(ordine.statoOrdine) == 'in_attesa'}">status-elaborazione</c:when>
								            <c:otherwise>status-default</c:otherwise>
								        </c:choose>">
								        ${ordine.statoOrdine}
								    </span>
								</td>
                                
                                <td class="actions-cell">
                                    <a href="${pageContext.request.contextPath}/Admin/GestioneOrdini?action=dettagli&idOrdine=${ordine.idOrdine}" class="btn-action btn-dark">Dettagli</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />