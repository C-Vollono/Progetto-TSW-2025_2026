document.addEventListener("DOMContentLoaded", function() {
    const menuLinks = document.querySelectorAll(".menu-link[data-target]");
    const sections = document.querySelectorAll(".dashboard-section");

    menuLinks.forEach(link => {
        link.addEventListener("click", function(e) {
            e.preventDefault();

            //Rimuove lo stato attivo da tutti i link e nasconde tutte le sezioni
            menuLinks.forEach(l => l.classList.remove("active"));
            sections.forEach(sec => sec.classList.remove("active"));

            //Aggiunge lo stato attivo al bottone cliccato
            this.classList.add("active");

            //Mostra la sezione collegata tramite il "data-target"
            const targetId = this.getAttribute("data-target");
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.classList.add("active");
            }
        });
    });
	
	//GESTIONE FORM IMPOSTAZIONI VIA AJAX
	    
	    const formAggiornaDati = document.getElementById("formAggiornaDati");
	    if (formAggiornaDati) {
	        formAggiornaDati.addEventListener("submit", function(e) {
	            e.preventDefault(); 
	            const msgDati = document.getElementById("msgDati");
	            msgDati.textContent = "Salvataggio in corso..."; 
	            msgDati.className = "";

	            const params = new URLSearchParams(new FormData(this));
	            params.append('action', 'aggiornaDati');

	            fetch("Profilo", { method: 'POST', body: params })
	            .then(response => response.json())
	            .then(data => {
	                msgDati.textContent = data.message;
	                if (data.success) {
	                    msgDati.className = "success-msg-js";
	                    
	                    document.querySelector("#panoramica p:nth-child(1) strong").nextSibling.textContent = " " + document.getElementById("nomeEdit").value;
	                    document.querySelector("#panoramica p:nth-child(2) strong").nextSibling.textContent = " " + document.getElementById("cognomeEdit").value;
	                } else {
	                    msgDati.className = "error-msg-js";
	                }
	            })
	            .catch(() => {
	                msgDati.textContent = "Errore di connessione al server.";
	                msgDati.className = "error-msg-js";
	            });
	        });
	    }

	    const formCambiaPassword = document.getElementById("formCambiaPassword");
	    if (formCambiaPassword) {
	        formCambiaPassword.addEventListener("submit", function(e) {
	            e.preventDefault();
	            const msgPassword = document.getElementById("msgPassword");
	            msgPassword.textContent = ""; 
	            msgPassword.className = "";
	            
	            const newPwd = document.getElementById("newPassword").value;
	            const confPwd = document.getElementById("confirmNewPassword").value;
	            
	            // Validazione Regex Javascript
	            const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
	            if (!regex.test(newPwd)) {
	                msgPassword.textContent = "La password deve avere min. 8 caratteri, una lettera e un numero.";
	                msgPassword.className = "error-msg-js";
	                return;
	            }
	            if (newPwd !== confPwd) {
	                msgPassword.textContent = "Le nuove password non coincidono.";
	                msgPassword.className = "error-msg-js";
	                return;
	            }

	            msgPassword.textContent = "Aggiornamento in corso...";
	            msgPassword.className = "";

	            const params = new URLSearchParams(new FormData(this));
	            params.append('action', 'cambiaPassword');

	            fetch("Profilo", { method: 'POST', body: params })
	            .then(response => response.json())
	            .then(data => {
	                msgPassword.textContent = data.message;
	                if (data.success) {
	                    msgPassword.className = "success-msg-js";
	                    formCambiaPassword.reset(); 
	                } else {
	                    msgPassword.className = "error-msg-js";
	                }
	            })
	            .catch(() => {
	                msgPassword.textContent = "Errore di connessione al server.";
	                msgPassword.className = "error-msg-js";
	            });
	        });
	    }
		
		//VALIDAZIONE ON BLUR PER IL CAMBIO PASSWORD
		    
		    const newPwdInput = document.getElementById("newPassword");
		    const confPwdInput = document.getElementById("confirmNewPassword");
		    const msgPasswordBlur = document.getElementById("msgPassword");
		    const regexPwd = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

		    if (newPwdInput && confPwdInput) {
		        
		        newPwdInput.addEventListener("blur", function() {
		            if (this.value !== "") {
		                if (!regexPwd.test(this.value)) {
		                    msgPasswordBlur.textContent = "La password deve avere min. 8 caratteri, una lettera e un numero.";
		                    msgPasswordBlur.className = "error-msg-js";
		                } else if (confPwdInput.value !== "" && this.value !== confPwdInput.value) {
		                    msgPasswordBlur.textContent = "Le nuove password non coincidono.";
		                    msgPasswordBlur.className = "error-msg-js";
		                } else {
		                    msgPasswordBlur.textContent = ""; 
		                    msgPasswordBlur.className = "";
		                }
		            }
		        });

		        
		        confPwdInput.addEventListener("blur", function() {
		            if (this.value !== "") {
		                if (this.value !== newPwdInput.value) {
		                    msgPasswordBlur.textContent = "Le nuove password non coincidono.";
		                    msgPasswordBlur.className = "error-msg-js";
		                } else if (!regexPwd.test(newPwdInput.value)) {
		                    msgPasswordBlur.textContent = "La password principale non rispetta i requisiti.";
		                    msgPasswordBlur.className = "error-msg-js";
		                } else {
		                    msgPasswordBlur.textContent = ""; 
		                    msgPasswordBlur.className = "";
		                }
		            }
		        });
				
		        newPwdInput.addEventListener("input", () => { msgPasswordBlur.textContent = ""; });
		        confPwdInput.addEventListener("input", () => { msgPasswordBlur.textContent = ""; });
		    }
});