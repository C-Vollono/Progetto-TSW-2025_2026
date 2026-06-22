//GESTIONE GALLERIA IMMAGINI
function changeMainImage(thumbnail) {
    const mainImage = document.getElementById("mainProductImage");
    mainImage.src = thumbnail.src;
    
    const allThumbnails = document.querySelectorAll(".thumbnail-img");
    allThumbnails.forEach(img => img.classList.remove("active-thumb"));
    thumbnail.classList.add("active-thumb");
}

//GESTIONE AGGIUNTA PREFERITI
document.addEventListener("DOMContentLoaded", function() {
    
    const btnAggiungiPreferito = document.getElementById("btnAggiungiPreferito");
    const msgPreferito = document.getElementById("msgPreferitoProdotto");

    if (btnAggiungiPreferito) {
        btnAggiungiPreferito.addEventListener("click", function(e) {
            e.preventDefault();

            const idProdotto = this.getAttribute("data-id");
            
            msgPreferito.textContent = "Salvataggio in corso...";
            msgPreferito.className = "msg-preferito"; 

            const params = new URLSearchParams();
            params.append('action', 'aggiungiPreferito');
            params.append('idProdotto', idProdotto);

            fetch("Profilo", { 
                method: 'POST', 
                body: params 
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                    return null;
                }
                return response.json();
            })
            .then(data => {
                if (!data) return; 
                
                if (data.success) {
                    msgPreferito.textContent = data.message;
                    msgPreferito.className = "msg-preferito success-msg-js"; 
                    
                    btnAggiungiPreferito.classList.add("wishlist-added");
                } else {
                    msgPreferito.textContent = data.message || "Impossibile aggiungere il prodotto.";
                    msgPreferito.className = "msg-preferito error-msg-js"; 
                }
            })
            .catch(() => {
                msgPreferito.textContent = "Errore di connessione o utente non autenticato.";
                msgPreferito.className = "msg-preferito error-msg-js";
            });
        });
    }
});