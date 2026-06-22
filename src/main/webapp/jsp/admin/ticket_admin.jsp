<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_ticket.css">

<main class="ticket-page-container">
    <h2 class="page-title">Gestione Ticket Assistenza</h2>

    <div class="ticket-main-box">
        <h3 class="box-title">Ticket Ricevuti</h3>

        <c:choose>
            <c:when test="${empty listaTicket}">
                <p style="padding: 10px;">Nessun ticket presente al momento.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="ticket" items="${listaTicket}">
                    
                    <div class="ticket-card">
                        <div class="ticket-info">
                            <div class="ticket-id"><strong>Ticket #${ticket.idTicket}</strong></div>
                            <div class="ticket-detail"><strong>Utente ID:</strong> ${ticket.idUtente}</div>
                            <div class="ticket-detail"><strong>Problema:</strong> ${ticket.descrizione}</div> 
						    <div class="ticket-detail"><strong>Stato attuale:</strong>   
						    <span class="status-badge 
						        ${ticket.stato == 'IN_ATTESA' ? 'status-elaborazione' : ''}
						        ${ticket.stato == 'ACCETTATO' ? 'status-spedito' : ''}
						        ${ticket.stato == 'COMPLETATO' ? 'status-consegnato' : ''}
						        ${ticket.stato == 'DECLINATO' ? 'status-annullato' : ''}">
						        ${ticket.stato}
						    </span>
						</div>
                        </div>
                        
                        <div class="ticket-actions">
                            <a href="${pageContext.request.contextPath}/Admin/GestioneTicket?action=view&idTicket=${ticket.idTicket}" class="btn-wireframe">Vedi dettagli</a>
                        </div>
                    </div>
                    
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />