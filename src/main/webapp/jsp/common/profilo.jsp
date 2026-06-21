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
    					<c:choose>
        					<c:when test="${empty datiSpedizione}">
            					<p class="text-empty">Nessun indirizzo di spedizione salvato.</p>
        					</c:when>
        					<c:otherwise>
            					<c:forEach var="ind" items="${datiSpedizione}" end="0">
                					<p><strong>${ind.via}, ${ind.numeroCivico}</strong></p>
                					<p>${ind.citta} (${ind.provincia}) &mdash; ${ind.cap}</p>
            					</c:forEach>
        					</c:otherwise>
    					</c:choose>
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
            <p class="section-subtitle">Visualizza lo storico dei tuoi acquisti e i relativi dettagli.</p>
            
            <c:choose>
                <c:when test="${empty tuttiOrdini}">
                    <p class="text-empty">Non hai ancora effettuato ordini.</p>
                </c:when>
                <c:otherwise>
                    <div class="orders-container">
                        <c:forEach var="ordine" items="${tuttiOrdini}">
                            <div class="order-card">
                                <div class="order-header">
                                    <div class="order-info-group">
                                        <span class="order-label">Data Ordine</span>
                                        <span class="order-value"><fmt:formatDate value="${ordine.dataOrdine}" pattern="dd MMM yyyy" /></span>
                                    </div>
                                    <div class="order-info-group">
                                        <span class="order-label">Totale</span>
                                        <span class="order-value">&euro; ${ordine.totaleOrdine}</span>
                                    </div>
                                    <div class="order-info-group">
                                        <span class="order-label">Spedito a</span>
                                        <span class="order-value">${ordine.spedizioneNomeCognome}</span>
                                    </div>
                                    <div class="order-info-group order-number-group">
                                        <span class="order-label">Ordine # ${ordine.idOrdine}</span>
                                        <button class="btn-link-small btn-dettagli" data-id="${ordine.idOrdine}">Vedi Dettagli</button>
                                    </div>
                                </div>
                                <div class="order-body">
                                    <span class="status-badge 
                                        ${ordine.statoOrdine == 'Spedito' || ordine.statoOrdine == 'Consegnato' ? 'status-success' : 
                                          (ordine.statoOrdine == 'Annullato' ? 'status-danger' : 'status-warning')}">
                                        ${ordine.statoOrdine}
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div id="modalDettagli" class="modal-overlay">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Dettagli Ordine <span id="modalOrdineTitle"></span></h3>
                    <button class="btn-close-modal" id="closeModalBtn">&times;</button>
                </div>
                <div class="modal-body">
                    <ul id="listaDettagliProdotti" class="dettagli-list">
                        </ul>
                </div>
            </div>
        </div>

        <div id="preferiti" class="dashboard-section">
            <h2>I Miei Preferiti</h2>
            <p class="section-subtitle">I prodotti che hai salvato per i tuoi futuri acquisti.</p>
            
            <div id="msgPreferiti" class="form-message"></div>

            <c:choose>
                <c:when test="${empty preferiti}">
                    <p class="text-empty" id="emptyPreferitiText">Non hai ancora aggiunto prodotti ai preferiti.</p>
                </c:when>
                <c:otherwise>
                    <div class="preferiti-grid" id="preferitiContainer">
                        <c:forEach var="prodotto" items="${preferiti}">
                            <div class="preferiti-card" data-card-id="${prodotto.idProdotto}">
                                <img src="${pageContext.request.contextPath}/images/${prodotto.immagine}" alt="Immagine Prodotto" class="preferiti-img">
                                <div class="preferiti-nome">${prodotto.nome}</div>
                                <div class="preferiti-prezzo">&euro; ${prodotto.prezzo}</div>
                                
                                <div class="preferiti-actions">
                                    <form action="${pageContext.request.contextPath}/Carrello" method="POST" class="form-add-cart">
                                        <input type="hidden" name="action" value="aggiungi">
                                        <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                                        <button type="submit" class="btn-gold form-btn">Al Carrello</button>
                                    </form>
                                    
                                    <button class="btn-danger-small btn-rimuovi-preferito" data-id="${prodotto.idProdotto}">Rimuovi</button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div id="recensioni" class="dashboard-section">
            <h2>Le Mie Recensioni</h2>
            <p class="section-subtitle">Lo storico dei pareri che hai condiviso sui nostri prodotti.</p>
            
            <div id="msgRecensioni"></div>

            <c:choose>
                <c:when test="${empty mieRecensioni}">
                    <p class="text-empty" id="emptyRecensioniText">Non hai ancora scritto nessuna recensione.</p>
                </c:when>
                <c:otherwise>
                    <div class="recensioni-container" id="recensioniContainer">
                        <c:forEach var="recensione" items="${mieRecensioni}">
                            <div class="recensione-card" data-card-id="${recensione.idRecensione}">
                                <div class="recensione-header">
                                    <div class="recensione-info">
                                        <span class="recensione-titolo">${recensione.titolo}</span>
                                        <span class="recensione-data"><fmt:formatDate value="${recensione.dataRecensione}" pattern="dd MMM yyyy HH:mm" /></span>
                                    </div>
                                    <div class="recensione-stelle">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= recensione.valutazione}">
                                                    <span class="stella-piena">★</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="stella-vuota">☆</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="recensione-body">
                                    <p class="recensione-testo">"${recensione.corpo}"</p>
                                </div>
                                <div class="recensione-footer">
                                    <button class="btn-danger-small btn-elimina-recensione" data-id="${recensione.idRecensione}">Elimina</button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div id="assistenza" class="dashboard-section">
            <h2>Assistenza Clienti</h2>
            <p class="section-subtitle">Hai un problema con un ordine o uno strumento? Apri un ticket e ti aiuteremo al più presto.</p>

            <div class="support-grid">
                <div class="settings-card">
                    <h3>Apri un Nuovo Ticket</h3>
                    <form id="formApriTicket" action="${pageContext.request.contextPath}/Profilo" method="POST">
                        <div class="input-group-settings">
                            <label for="oggettoTicket">Oggetto della richiesta</label>
                            <input type="text" id="oggettoTicket" name="oggetto" placeholder="Es. Problema con spedizione ordine #123" required>
                        </div>
                        
                        <div class="input-group-settings">
                            <label for="messaggioTicket">Dettagli del problema</label>
                            <textarea id="messaggioTicket" name="messaggio" rows="5" placeholder="Descrivi il tuo problema in modo dettagliato..." required></textarea>
                        </div>
                        
                        <div class="input-group-settings">
                            <label for="allegatoTicket">Allega una foto (Opzionale)</label>
                            <input type="file" id="allegatoTicket" name="allegato" accept="image/*" class="file-input-custom">
                        </div>
                        
                        <div id="msgTicket" class="form-message-margin"></div>
                        
                        <button type="submit" class="btn-gold form-btn">Invia Richiesta</button>
                    </form>
                </div>

                <div class="settings-card">
                    <h3>I Tuoi Ticket</h3>
                    <c:choose>
                        <c:when test="${empty tuttiTicket}">
                            <p class="text-empty" id="emptyTicketText">Non hai aperto nessun ticket di assistenza.</p>
                            <ul class="ticket-list" id="ticketListContainer"></ul>
                        </c:when>
                        <c:otherwise>
                            <ul class="ticket-list" id="ticketListContainer">
                                <c:forEach var="ticket" items="${tuttiTicket}">
                                    <li class="ticket-item">
                                        <div class="ticket-info">
                                            <span class="ticket-id">Ticket #${ticket.idTicket} - ${ticket.oggetto}</span>
                                            <span class="ticket-date"><fmt:formatDate value="${ticket.dataApertura}" pattern="dd MMM yyyy" /></span>
                                        </div>
                                        
                                        <div class="ticket-actions">
                                            <span class="status-badge 
                                                ${ticket.stato == 'Risolto' ? 'status-success' : 
                                                  (ticket.stato == 'Chiuso' ? 'status-danger' : 'status-warning')}">
                                                ${ticket.stato}
                                            </span>
                                            <button class="btn-link-small btn-leggi-ticket" data-id="${ticket.idTicket}">Leggi</button>
                                        </div>
                                        
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div> <div id="modalTicket" class="modal-overlay">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Dettagli Ticket <span id="modalTicketTitle"></span></h3>
                    <button class="btn-close-modal" id="closeModalTicketBtn">&times;</button>
                </div>
                <div class="modal-body" id="corpoModalTicket">
                    </div>
            </div>
        </div>
        
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
                
                <div class="settings-card settings-card--full">
    				<h3>Indirizzi di Spedizione</h3>

    					<c:choose>
        					<c:when test="${empty datiSpedizione}">
            					<p class="text-empty">Nessun indirizzo di spedizione salvato.</p>
        					</c:when>
        					<c:otherwise>
            					<ul class="indirizzi-list">
                					<c:forEach var="ind" items="${datiSpedizione}">
                    					<li class="indirizzo-item">
                        					<div class="indirizzo-info">
                            					<span class="indirizzo-via">${ind.via}, ${ind.numeroCivico}</span>
                            					<span class="indirizzo-citta">${ind.citta} (${ind.provincia}) &mdash; ${ind.cap}</span>
                            						<c:if test="${not empty ind.telefono}">
                                						<span class="indirizzo-telefono">Tel: ${ind.telefono}</span>
                            						</c:if>
                        					</div>
                        					<div class="indirizzo-actions">
                            					<form action="${pageContext.request.contextPath}/Profilo" method="POST">
                                					<input type="hidden" name="action" value="eliminaIndirizzo">
                                					<input type="hidden" name="idSpedizione" value="${ind.idSpedizione}">
                                					<button type="submit" class="btn-danger-small" onclick="return confirm('Eliminare questo indirizzo?')">Elimina</button>
                            					</form>
                        					</div>
                    					</li>
                					</c:forEach>
            					</ul>
        					</c:otherwise>
    					</c:choose>

    					<h4 class="indirizzi-subtitle">Aggiungi un nuovo indirizzo</h4>
    					<form id="formAggiungiIndirizzo" action="${pageContext.request.contextPath}/Profilo" method="POST">
        				<input type="hidden" name="action" value="aggiungiIndirizzo">
        					<div class="indirizzi-form-grid">
            					<div class="input-group-settings">
                					<label for="via">Via / Piazza</label>
                					<input type="text" id="via" name="via" placeholder="Es. Via Roma" required>
            					</div>
            					<div class="input-group-settings">
                					<label for="numeroCivico">Numero civico</label>
                					<input type="text" id="numeroCivico" name="numeroCivico" placeholder="Es. 12" required>
            					</div>
            					<div class="input-group-settings">
                					<label for="citta">Città</label>
                					<input type="text" id="citta" name="citta" placeholder="Es. Napoli" required>
            					</div>
            					<div class="input-group-settings">
                					<label for="provincia">Provincia</label>
                					<input type="text" id="provincia" name="provincia" placeholder="Es. NA" maxlength="2" required>
            					</div>
            					<div class="input-group-settings">
                					<label for="cap">CAP</label>
                					<input type="text" id="cap" name="cap" placeholder="Es. 80053" pattern="\d{5}" required>
            					</div>
            					<div class="input-group-settings">
                					<label for="telefono">Telefono</label>
                					<input type="tel" id="telefono" name="telefono" placeholder="Es. 3331234567">
            					</div>
        					</div>
        					<div id="msgIndirizzo"></div>
        					<button type="submit" class="btn-gold form-btn">Aggiungi Indirizzo</button>
    					</form>
				</div>
				
				<div class="settings-card settings-card--full">
    				<h3>Metodi di Pagamento</h3>

    				<c:choose>
        				<c:when test="${empty datiPagamento}">
            				<p class="text-empty">Nessun metodo di pagamento salvato.</p>
        				</c:when>
        				<c:otherwise>
            				<ul class="indirizzi-list">
                				<c:forEach var="carta" items="${datiPagamento}">
                    				<li class="indirizzo-item">
                        				<div class="indirizzo-info">
                            				<span class="indirizzo-via">${carta.circuitoCarta} &mdash; ${carta.numeroCartaOscurato}</span>
                            				<span class="indirizzo-citta">Intestatario: ${carta.intestatario}</span>
                            				<span class="indirizzo-telefono">Scadenza: <fmt:formatDate value="${carta.scadenzaCarta}" pattern="MM/yyyy" /></span>
                        				</div>
                        				<div class="indirizzo-actions">
                            				<form action="${pageContext.request.contextPath}/Profilo" method="POST">
                                				<input type="hidden" name="action" value="eliminaPagamento">
                                				<input type="hidden" name="idPagamento" value="${carta.idPagamento}">
                                				<button type="submit" class="btn-danger-small"onclick="return confirm('Rimuovere questo metodo di pagamento?')">Rimuovi</button>
                            				</form>
                        				</div>
                    				</li>
                				</c:forEach>
            				</ul>
        				</c:otherwise>
    				</c:choose>

    				<h4 class="indirizzi-subtitle">Aggiungi una carta</h4>
    					<form id="formAggiungiPagamento" action="${pageContext.request.contextPath}/Profilo" method="POST">
        					<input type="hidden" name="action" value="aggiungiPagamento">
        						<div class="indirizzi-form-grid">
            						<div class="input-group-settings">
                						<label for="circuitoCarta">Circuito</label>
                							<select id="circuitoCarta" name="circuitoCarta" required>
                    							<option value="" disabled selected>Seleziona...</option>
                    							<option value="Visa">Visa</option>
                    							<option value="Mastercard">Mastercard</option>
                    							<option value="American Express">American Express</option>
                    							<option value="PayPal">PayPal</option>
                							</select>
            						</div>
            						<div class="input-group-settings">
                						<label for="numeroCarta">Numero carta</label>
                							<input type="text" id="numeroCarta" name="numeroCarta" placeholder="Es. 1234 5678 9012 3456" pattern="\d{4}\s?\d{4}\s?\d{4}\s?\d{4}" maxlength="19" required>
            						</div>
            						<div class="input-group-settings">
                						<label for="intestatario">Intestatario</label>
                							<input type="text" id="intestatario" name="intestatario" placeholder="Es. Mario Rossi" required>
            						</div>
            							<div class="input-group-settings">
                							<label for="scadenzaCarta">Scadenza</label>
                							<input type="month" id="scadenzaCarta" name="scadenzaCarta" required>
            							</div>
        							</div>
        							<div id="msgPagamento"></div>
        								<button type="submit" class="btn-gold form-btn">Aggiungi Carta</button>
    					</form>
				</div>	                
            </div>
        </div>

    </section>
</main>

<script defer src="${pageContext.request.contextPath}/js/profilo.js"></script>

<jsp:include page="/jsp/footer.jsp" />