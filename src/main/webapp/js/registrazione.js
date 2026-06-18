document.addEventListener("DOMContentLoaded", function() {
    const formRegistrazione = document.getElementById("formRegistrazione");
    const btnRegistrati = document.getElementById("btnRegistrati");

    if (formRegistrazione) {
        formRegistrazione.addEventListener("submit", function(e) {
            e.preventDefault();
            
            if (btnRegistrati.disabled) {
                showToast("Compila tutti i campi correttamente prima di procedere.", "error");
                return; 
            }

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
                    showToast(data.message, 'success');
                    setTimeout(() => { window.location.href = data.redirect; }, 2000);
                } else {
                    showToast(data.message, 'error');
                }
            })
            .catch(error => {
                console.error("Dettaglio errore:", error);
                showToast("Errore di connessione con il server.", "error");
            });
        });
    }
});