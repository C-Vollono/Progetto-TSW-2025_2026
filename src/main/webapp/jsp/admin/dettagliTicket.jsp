<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/jsp/admin/header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_ticket.css">

<main class="ticket-page-container">
    <h2 class="page-title">Gestione Ticket Assistenza</h2>

    <div class="wireframe-box">
        <div class="wireframe-header">
            <h2>Dettaglio Ticket #${ticket.idTicket}</h2>
            <a href="${pageContext.request.contextPath}/Admin/GestioneTicket?action=list" class="btn-back">Torna alla lista</a>
        </div>

        <div class="wireframe-grid">
            <div class="wireframe-left">
                
                <div class="wireframe-field">
                    <span class="wireframe-label">ID Utente:</span>
                    <span class="wireframe-value">${ticket.idUtente}</span>
                </div>

                <div class="wireframe-field">
                    <span class="wireframe-label">Descrizione Problema:</span>
                    <span class="wireframe-value value-pre-wrap">${ticket.descrizione}</span>
                </div>

                <div class="wireframe-field">
                    <span class="wireframe-label">Stato Attuale:</span>
                    <span class="status-badge 
                        ${ticket.stato == 'IN_ATTESA' ? 'status-elaborazione' : ''}
                        ${ticket.stato == 'ACCETTATO' ? 'status-spedito' : ''}
                        ${ticket.stato == 'COMPLETATO' ? 'status-consegnato' : ''}
                        ${ticket.stato == 'DECLINATO' ? 'status-annullato' : ''}">
                        ${ticket.stato}
                    </span>
                </div>

                <form action="${pageContext.request.contextPath}/Admin/GestioneTicket" method="post" class="wireframe-form">
                    <input type="hidden" name="action" value="updateStatus">
                    <input type="hidden" name="idTicket" value="${ticket.idTicket}">
                    
                    <label class="wireframe-label" for="stato">Modifica Stato Avanzamento:</label>
                    <select name="stato" id="stato" class="wireframe-select">
                        <option value="IN_ATTESA" ${ticket.stato == 'IN_ATTESA' ? 'selected' : ''}>IN ATTESA</option>
                        <option value="ACCETTATO" ${ticket.stato == 'ACCETTATO' ? 'selected' : ''}>ACCETTATO (In Corso)</option>
                        <option value="COMPLETATO" ${ticket.stato == 'COMPLETATO' ? 'selected' : ''}>COMPLETATO</option>
                        <option value="DECLINATO" ${ticket.stato == 'DECLINATO' ? 'selected' : ''}>DECLINATO</option>
                    </select>
                    
                    <button type="submit" class="btn-wireframe-submit">Aggiorna Stato</button>
                </form>

                <c:if test="${ticket.stato == 'COMPLETATO' and not empty pratica}">
                    <div class="pratica-section">
                        <h3>Dati Pratica Associata</h3>
                        
                        <div class="wireframe-field pratica-field">
                            <span class="wireframe-label">ID Pratica:</span>
                            <span class="wireframe-value">${pratica.idPratica}</span>
                        </div>
                        
                        <div class="wireframe-field pratica-field">
                            <span class="wireframe-label">Interventi Effettuati/Previsti:</span>
                            <span class="wireframe-value value-pre-wrap">${pratica.interventiPrevisti}</span>
                        </div>
                        
                        <div class="wireframe-field pratica-field">
                            <span class="wireframe-label">Costo Totale Riparazione:</span>
                            <span class="wireframe-value">€ ${pratica.costoRiparazione}</span>
                        </div>
                        
                        <div class="wireframe-field pratica-field">
                            <span class="wireframe-label">Data e Ora di Completamento:</span>
                            <span class="wireframe-value">${pratica.dataCompletamento}</span>
                        </div>
                    </div>
                </c:if>

            </div>
        </div>
    </div>
</main>

<jsp:include page="/jsp/footer.jsp" />