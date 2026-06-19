<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/profilo.css">

<jsp:include page="/jsp/header.jsp" />

<main class="dashboard-container">
    <aside class="dashboard-sidebar">
        <h3>Il Mio Account</h3>
        <ul class="dashboard-menu">
            <li><a href="#" class="menu-link active" data-target="panoramica">Panoramica Profilo</a></li>
            <li><a href="#" class="menu-link" data-target="ordini">I Miei Ordini</a></li>
            <li><a href="#" class="menu-link" data-target="preferiti">I Miei Preferiti</a></li>
            <li><a href="#" class="menu-link" data-target="recensioni">Le Mie Recensioni</a></li>
            <li><a href="#" class="menu-link" data-target="assistenza">Assistenza Clienti</a></li>
            <li><a href="#" class="menu-link" data-target="impostazioni">Impostazioni Account</a></li>
            <li><a href="${pageContext.request.contextPath}/Logout" class="menu-link text-danger">Esci</a></li>
        </ul>
    </aside>

    <section class="dashboard-content">
        
        <div id="panoramica" class="dashboard-section active">
            <h2>Panoramica Profilo</h2>
            <p class="section-subtitle">Gestisci le tue informazioni personali e la sicurezza dell'account.</p>
            
            <div class="overview-cards">
                <div class="overview-card">
                    <div class="card-header">
                        <h3>Dati Personali</h3>
                        <button class="btn-edit" aria-label="Modifica dati" onclick="document.querySelector('[data-target=\'impostazioni\']').click()">✎</button>
                    </div>
                    <div class="card-body">
                        <p><strong>Nome:</strong> ${sessionScope.utenteLoggato.nome}</p>
                        <p><strong>Cognome:</strong> ${sessionScope.utenteLoggato.cognome}</p>
                        <p><strong>Email:</strong> ${sessionScope.utenteLoggato.email}</p>
                        <p><strong>Username:</strong> ${sessionScope.utenteLoggato.username}</p>
                        <p><strong>Data di Nascita:</strong> 
                            <c:choose>
                                <c:when test="${not empty sessionScope.utenteLoggato.dataDiNascita}">
                                    <fmt:formatDate value="${sessionScope.utenteLoggato.dataDiNascita}" pattern="dd/MM/yyyy" />
                                </c:when>
                                <c:otherwise>
                                    <span class="text-empty">Non specificata</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-header">
                        <h3>Indirizzi</h3>
                        <button class="btn-edit" aria-label="Modifica indirizzi" onclick="document.querySelector('[data-target=\'impostazioni\']').click()">✎</button>
                    </div>
                    <div class="card-body">
                        <p class="text-empty">Nessun indirizzo di spedizione salvato.</p>
                    </div>
                </div>

                <div class="overview-card">
                    <div class="card-header">
                        <h3>Ordini Recenti</h3>
                        <button class="btn-link-small" onclick="document.querySelector('[data-target=\'ordini\']').click()">Vedi tutti</button>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty ordiniRecenti}">
                                <p class="text-empty">Nessun ordine effettuato di recente.</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="recent-list">
                                    <c:forEach var="ordine" items="${ordiniRecenti}">
                                        <li>
                                            <div class="recent-info">
                                                <span class="recent-title">Ordine #${ordine.idOrdine}</span>
                                                <span class="recent-date"><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd MMM yyyy" /></span>
                                            </div>
                                            <span class="status-badge 
                                                ${ordine.statoOrdine == 'Spedito' || ordine.statoOrdine == 'Consegnato' ? 'status-success' : 
                                                  (ordine.statoOrdine == 'Annullato' ? 'status-danger' : 'status-warning')}">
                                                ${ordine.statoOrdine}
                                            </span>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div> <div class="overview-card">
                    <div class="card-header">
                        <h3>Ultimi Ticket</h3>
                        <button class="btn-link-small" onclick="document.querySelector('[data-target=\'assistenza\']').click()">Vedi tutti</button>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty ticketRecenti}">
                                <p class="text-empty">Nessun ticket di assistenza recente.</p>
                            </c:when>
                            <c:otherwise>
                                <ul class="recent-list">
                                    <c:forEach var="ticket" items="${ticketRecenti}">
                                        <li>
                                            <div class="recent-info">
                                                <span class="recent-title">Ticket #${ticket.idTicket}</span>
                                                <span class="recent-date"><fmt:formatDate value="${ticket.dataApertura}" pattern="dd MMM yyyy" /></span>
                                            </div>
                                            <span class="status-badge 
                                                ${ticket.stato == 'Risolto' ? 'status-success' : 
                                                  (ticket.stato == 'Chiuso' ? 'status-danger' : 'status-warning')}">
                                                ${ticket.stato}
                                            </span>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
		</div>
		
        <div id="ordini" class="dashboard-section">
            <h2>I Miei Ordini</h2>
            <p>Caricamento ordini in corso...</p>
        </div>

        <div id="preferiti" class="dashboard-section"><h2>I Miei Preferiti</h2></div>
        <div id="recensioni" class="dashboard-section"><h2>Le Mie Recensioni</h2></div>
        <div id="assistenza" class="dashboard-section"><h2>Assistenza Clienti</h2></div>
        <div id="impostazioni" class="dashboard-section">
            <h2>Impostazioni Account</h2>
            <p class="section-subtitle">Aggiorna le tue informazioni personali o modifica la tua password di accesso.</p>

            <div class="settings-grid">
                <div class="settings-card">
                    <h3>Dati Personali</h3>
                    <form id="formAggiornaDati" action="${pageContext.request.contextPath}/AggiornaProfilo" method="POST">
                        <div class="input-group-settings">
                            <label for="nomeEdit">Nome</label>
                            <input type="text" id="nomeEdit" name="nome" value="${sessionScope.utenteLoggato.nome}" required>
                        </div>
                        <div class="input-group-settings">
                            <label for="cognomeEdit">Cognome</label>
                            <input type="text" id="cognomeEdit" name="cognome" value="${sessionScope.utenteLoggato.cognome}" required>
                        </div>
                        <div class="input-group-settings">
                            <label for="dataNascitaEdit">Data di Nascita</label>
                            <input type="date" id="dataNascitaEdit" name="dataNascita" value="${sessionScope.utenteLoggato.dataDiNascita}" required>
                        </div>
                        
                        <div class="input-group-settings disabled-group">
                            <label>Email (Non modificabile)</label>
                            <input type="email" value="${sessionScope.utenteLoggato.email}" disabled>
                        </div>
                        <div class="input-group-settings disabled-group">
                            <label>Username (Non modificabile)</label>
                            <input type="text" value="${sessionScope.utenteLoggato.username}" disabled>
                        </div>

                        <div id="msgDati"></div>

                        <button type="submit" class="btn-gold form-btn">Salva Modifiche</button>
                    </form>
                </div>

                <div class="settings-card">
                    <h3>Cambia Password</h3>
                    <form id="formCambiaPassword" action="${pageContext.request.contextPath}/CambiaPassword" method="POST">
                        <div class="input-group-settings">
                            <label for="oldPassword">Password Attuale</label>
                            <input type="password" id="oldPassword" name="oldPassword" placeholder="Inserisci password attuale" required>
                        </div>
                        <div class="input-group-settings">
                            <label for="newPassword">Nuova Password</label>
                            <input type="password" id="newPassword" name="newPassword" placeholder="Min. 8 caratteri, 1 lettera, 1 numero" required>
                        </div>
                        <div class="input-group-settings">
                            <label for="confirmNewPassword">Conferma Nuova Password</label>
                            <input type="password" id="confirmNewPassword" name="confirmNewPassword" placeholder="Ripeti la nuova password" required>
                        </div>

                        <div id="msgPassword"></div>

                        <button type="submit" class="btn-gold form-btn">Aggiorna Password</button>
                    </form>
                </div>
            </div>
        </div>

    </section>
</main>

<script defer src="${pageContext.request.contextPath}/js/profilo.js"></script>

<jsp:include page="/jsp/footer.jsp" />