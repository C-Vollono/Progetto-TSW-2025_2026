
document.addEventListener("DOMContentLoaded", function() {

    const countdownElement = document.getElementById('countdown');
   
    if (!countdownElement) return;

    const contextPath = countdownElement.getAttribute('data-context');

    const homeUrl = contextPath + "/Home";


    let secondiRimanenti = 10;


    const timer = setInterval(function() {
        secondiRimanenti--;
        
        countdownElement.textContent = secondiRimanenti;
        
        if (secondiRimanenti <= 0) {
            clearInterval(timer);
            window.location.href = homeUrl;
        }
    }, 1000);
});