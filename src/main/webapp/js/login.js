document.addEventListener("DOMContentLoaded", function() {
    const formLogin = document.getElementById("formLogin");

    if (formLogin) {
        formLogin.addEventListener("submit", function(e) {
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
                    showToast(data.message, 'success');
                    setTimeout(() => { window.location.href = data.redirect; }, 1500);
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