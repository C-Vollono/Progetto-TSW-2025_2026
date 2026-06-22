// src/main/webapp/js/errore.js

document.addEventListener("DOMContentLoaded", function() {
    // 1. Recuperiamo lo span del conto alla rovescia
    const countdownElement = document.getElementById('countdown');
    
    // Sicurezza: se l'elemento non è nella pagina interrompiamo lo script per non causare eccezioni
    if (!countdownElement) return;

    // 2. Leggiamo il Context Path (es: /ToneMarket) che la JSP ha scritto nell'attributo data-context
    const contextPath = countdownElement.getAttribute('data-context');
    
    // 3. Costruiamo la rotta di destinazione finale
    const homeUrl = contextPath + "/Home";

    // 4. Inizializziamo il tempo del countdown
    let secondiRimanenti = 10;

    // 5. Avviamo il decremento del timer ogni secondo (1000ms)
    const timer = setInterval(function() {
        secondiRimanenti--;
        
        // Aggiorna la cifra a schermo
        countdownElement.textContent = secondiRimanenti;
        
        // 6. Al termine del tempo, ferma il thread del timer e cambia pagina
        if (secondiRimanenti <= 0) {
            clearInterval(timer);
            window.location.href = homeUrl;
        }
    }, 1000);
});