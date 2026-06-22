<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>ToneMarket - Si è verificato un errore</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/errore.css">
</head>
<body>

    <div class="error-box">
        <div class="brand-error">Tone<span>Market</span></div>
        
        <div class="error-icon">⚠️</div>
        
        <c:choose>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                <h2>Errore 404 - Nota Calante! 🎸</h2>
                <p>Ops! La pagina che stai cercando è andata fuori tempo o non è mai stata scritta sul nostro spartito. Forse l'URL ha preso una steccata clamorosa!</p>
            </c:when>
            
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                <h2>Errore 403 - Solo membri della Band! 🎟️</h2>
                <p>Alt! Questa zona è riservata al backstage o al soundcheck privato. Non hai il pass VIP laminato o l'autorizzazione necessaria per fare questo assolo!</p>
            </c:when>
            

            <c:when test="${requestScope['javax.servlet.error.status_code'] == 500}">
                <h2>Errore 500 - Amplificatore Bruciato! 🔌</h2>
                <p>Ci scusiamo per il fischio... Si deve essere spezzata una corda sul più bello o il server è andato in distorsione totale! I nostri tecnici sul palco stanno già riaccordando gli strumenti.</p>
            </c:when>
            
            <c:otherwise>
                <h2>Il ritmo si è interrotto! 🥁</h2>
                <p>Qualcosa ha fatto saltare la testina del giradischi (Codice Stato: ${requestScope['javax.servlet.error.status_code']}). Rimettiamo a tempo la traccia!</p>
            </c:otherwise>
        </c:choose>
        
        <p>Verrai reindirizzato automaticamente alla Home tra 
            <span id="countdown" data-context="${pageContext.request.contextPath}">10</span> secondi.
        </p>
        
        <div class="actions-bar">
            <a href="#" onclick="window.history.back(); return false;" class="btn-link">← Torna indietro</a>
            <a href="${pageContext.request.contextPath}/Home" class="btn-action">Vai alla Home</a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/errore.js"></script>

</body>
</html>