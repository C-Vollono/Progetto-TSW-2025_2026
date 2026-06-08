<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="profile-section">
    <div class="profile-header">
        <h2>Area Personale</h2>
        <p>Benvenuto nel tuo angolino privato.</p>
    </div>

    <div class="profile-container">
        
        <div class="profile-card user-data-card">
            <h3>I Tuoi Dati</h3>
            
            <div class="profile-data-detail">
                <span class="data-label">Nome e Cognome:</span>
                <span class="data-value">Mario Rossi</span>
            </div>
            
            <div class="profile-data-detail">
                <span class="data-label">Username:</span>
                <span class="data-value">mario_rossi99</span>
            </div>
            
            <div class="profile-data-detail">
                <span class="data-label">Email registrata:</span>
                <span class="data-value">mariorossi@email.com</span>
            </div>
            
            <div class="profile-data-detail">
                <span class="data-label">Tipo di Account</span>
                <span class="data-value account-badge">Cliente Premium</span>
            </div>
        </div>

        <div class="profile-card orders-history-card">
            <h3>Storico Ordini</h3>
            
            <div class="table-responsive">
                <table class="profile-table">
                    <thead>
                        <tr>
                            <th>Numero Ordine</th>
                            <th>Data</th>
                            <th>Totale</th>
                            <th>Stato Spedizione</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>#TM-2026-0042</td>
                            <td>04/05/2026</td>
                            <td>€ 249,00</td>
                            <td><span class="status-badge status-delivered">Consegnato</span></td>
                        </tr>
                        <tr>
                            <td>#TM-2026-0011</td>
                            <td>12/02/2026</td>
                            <td>€ 49,00</td>
                            <td><span class="status-badge status-delivered">Consegnato</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</section>

<jsp:include page="/jsp/footer.jsp" />
