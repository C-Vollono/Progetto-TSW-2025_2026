document.addEventListener("DOMContentLoaded", function() {

    const formsCarrello = document.querySelectorAll('form[action*="/Carrello"]');

    formsCarrello.forEach(form => {
        form.addEventListener('submit', function(e) {
            const azioneInput = form.querySelector('input[name="azione"]');
            
            // Controlla se il form è di "aggiunta"
            if (azioneInput && azioneInput.value === 'aggiungi') {
                e.preventDefault(); 

                const formData = new FormData(this);
                const params = new URLSearchParams(formData);
                params.append('isAjax', 'true'); 

                fetch(this.action, {
                    method: 'POST',
                    body: params 
                })
                .then(response => {
                    if (!response.ok) throw new Error("Errore dal server");
                    return response.json(); 
                })
                .then(data => {
                    if (data.success) {
                        const badge = document.getElementById('cart-badge-count');
                        if (badge) {
                            badge.textContent = data.totaleElementi;
                            badge.style.display = 'flex'; 
                        }
                        
                        showToast("Prodotto aggiunto al carrello \u2714");
                    }
                })
                .catch(error => console.error("Si è verificato un errore:", error));
            }
        });
    });
});