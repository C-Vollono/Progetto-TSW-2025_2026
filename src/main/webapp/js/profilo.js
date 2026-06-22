document.addEventListener("DOMContentLoaded", function() {
    const menuLinks = document.querySelectorAll(".menu-link[data-target]");
    const sections = document.querySelectorAll(".dashboard-section");

    menuLinks.forEach(link => {
        link.addEventListener("click", function(e) {
            e.preventDefault();
            
            menuLinks.forEach(l => l.classList.remove("active"));
            sections.forEach(sec => sec.classList.remove("active"));
            
            this.classList.add("active");
            
            const targetId = this.getAttribute("data-target");
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.classList.add("active");
            }
        });
    });
	
	//GESTIONE FORM IMPOSTAZIONI AJAX    
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
			
	//GESTIONE DETTAGLI ORDINI
	const modale = document.getElementById("modalDettagli");
	const closeModalBtn = document.getElementById("closeModalBtn");
	const listaDettagli = document.getElementById("listaDettagliProdotti");
	const modalTitle = document.getElementById("modalOrdineTitle");
			    
	function svuotaLista() {
		while (listaDettagli.firstChild) {
			listaDettagli.removeChild(listaDettagli.firstChild);
		}
	}
			   
	function mostraMessaggioLista(testo, classeCss) {
		svuotaLista();
		const li = document.createElement("li");
		const p = document.createElement("p");
		p.className = classeCss;
		p.textContent = testo;
		li.appendChild(p);
		listaDettagli.appendChild(li);
	}

	document.querySelectorAll(".btn-dettagli").forEach(btn => {
		btn.addEventListener("click", function() {
			const idOrdine = this.getAttribute("data-id");
			modalTitle.textContent = "#" + idOrdine;
			           
			mostraMessaggioLista("Caricamento prodotti...", "text-empty");
			modale.classList.add("active");

			const params = new URLSearchParams();
			params.append('action', 'dettagliOrdine');
			params.append('idOrdine', idOrdine);

			fetch("Profilo", { method: 'POST', body: params })
				.then(response => response.json())
			    .then(data => {
			    	svuotaLista();
		                if (data.success && data.dettagli.length > 0) {
		                    data.dettagli.forEach(item => {			                        
		                        const li = document.createElement("li");
		                        li.className = "dettaglio-item";
			                        
		                        const divLeft = document.createElement("div");
			                        
		                        const divNome = document.createElement("div");
		                        divNome.className = "dettaglio-nome";
		                        divNome.textContent = item.nome;
			                        
		                        const divQuantita = document.createElement("div");
		                        divQuantita.className = "dettaglio-quantita";
		                        divQuantita.textContent = "Quantità: " + item.quantita;
			                        
		                        divLeft.appendChild(divNome);
		                        divLeft.appendChild(divQuantita);
			                        
		                        const divPrezzo = document.createElement("div");
		                        divPrezzo.className = "dettaglio-prezzo";
		                        divPrezzo.textContent = "€ " + item.prezzo.toFixed(2);
			                        
		                        li.appendChild(divLeft);
		                        li.appendChild(divPrezzo);
			                        
		                        listaDettagli.appendChild(li);
		                    });
			            } else {
			            	mostraMessaggioLista("Impossibile caricare i dettagli.", "error-msg-js");
			            }
					})
			        .catch(() => {
			        mostraMessaggioLista("Errore di connessione.", "error-msg-js");
			        });
		});
	});

	if (closeModalBtn) {
		closeModalBtn.addEventListener("click", () => modale.classList.remove("active"));
	}

	window.addEventListener("click", function(e) {
		if (e.target === modale) {
			modale.classList.remove("active");
		}
	});
				
	//GESTIONE RIMOZIONE PREFERITI
	const msgPreferiti = document.getElementById("msgPreferiti");
	const preferitiContainer = document.getElementById("preferitiContainer");

    document.querySelectorAll(".btn-rimuovi-preferito").forEach(btn => {
	    btn.addEventListener("click", function() {
		    const idProdotto = this.getAttribute("data-id");
		    const cardDaRimuovere = document.querySelector(`.preferiti-card[data-card-id="${idProdotto}"]`);				            
				            
		    msgPreferiti.textContent = "Rimozione in corso...";
		    msgPreferiti.className = "form-message";

		    const params = new URLSearchParams();
		    params.append('action', 'rimuoviPreferito');
		    params.append('idProdotto', idProdotto);

	        fetch("Profilo", { method: 'POST', body: params })
	        .then(response => response.json())
	        .then(data => {
		        if (data.success) {
			        msgPreferiti.textContent = "";				                    
			               
				    if (cardDaRimuovere) {
					    preferitiContainer.removeChild(cardDaRimuovere);
				    }
				                    
				    if (preferitiContainer.children.length === 0) {
				    	preferitiContainer.remove();
				                        
				        const emptyP = document.createElement("p");
				        emptyP.className = "text-empty";
				        emptyP.id = "emptyPreferitiText";
				        emptyP.textContent = "Non hai più prodotti salvati nei preferiti.";				                        
				                        
				        msgPreferiti.parentNode.insertBefore(emptyP, msgPreferiti.nextSibling);
				    }
				}else {
					msgPreferiti.textContent = data.message || "Impossibile rimuovere il prodotto.";
				    msgPreferiti.className = "error-msg-js";
				}
			})
			.catch(() => {
				msgPreferiti.textContent = "Errore di connessione al server.";
				msgPreferiti.className = "error-msg-js";
			});
		});
	});
					
    //GESTIONE RIMOZIONE RECENSIONE
	const btnEliminaRecensioni = document.querySelectorAll('.btn-elimina-recensione');
	const msgRecensioni = document.getElementById('msgRecensioni');
	
	const modalConfermaRec = document.getElementById('modalConfermaRecensione');
	const btnAnnullaRec = document.getElementById('btnAnnullaRecensione');
	const btnConfermaRec = document.getElementById('btnConfermaRecensione');

	let idDaEliminare = null;
	let cardDaEliminare = null;

	btnEliminaRecensioni.forEach(btn => {
		btn.addEventListener('click', function(e) {
			e.preventDefault();
			idDaEliminare = this.getAttribute('data-id');
			cardDaEliminare = this.closest('.recensione-card');
			
			modalConfermaRec.classList.add('active');
		});
	});
	if (btnAnnullaRec) {
		btnAnnullaRec.addEventListener('click', function() {
			modalConfermaRec.classList.remove('active');
			idDaEliminare = null;
			cardDaEliminare = null;
		});
	}
	if (btnConfermaRec) {
		btnConfermaRec.addEventListener('click', function() {
			modalConfermaRec.classList.remove('active');

			if (!idDaEliminare || !cardDaEliminare) return;

			if(msgRecensioni) {
				msgRecensioni.textContent = "Eliminazione in corso...";
				msgRecensioni.className = "form-message-margin";
			}

			const params = new URLSearchParams();
			params.append('action', 'eliminaRecensione');
			params.append('idRecensione', idDaEliminare);

			fetch("Profilo", { method: 'POST', body: params })
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					if(msgRecensioni) {
						msgRecensioni.textContent = data.message;
						msgRecensioni.className = "success-msg-js form-message-margin";
					}
					
					cardDaEliminare.style.transition = "opacity 0.3s ease";
					cardDaEliminare.style.opacity = "0";
					setTimeout(() => cardDaEliminare.remove(), 300);
					
				} else {
					if(msgRecensioni) {
						msgRecensioni.textContent = data.message || "Errore durante l'eliminazione.";
						msgRecensioni.className = "error-msg-js form-message-margin";
					}
				}
			})
			.catch(() => {
				if(msgRecensioni) {
					msgRecensioni.textContent = "Errore di connessione al server.";
					msgRecensioni.className = "error-msg-js form-message-margin";
				}
			});
		});
	}

	window.addEventListener("click", function(e) {
		if (e.target === modalConfermaRec) {
			modalConfermaRec.classList.remove("active");
			idDaEliminare = null;
			cardDaEliminare = null;
		}
	});
							
	//GESTIONE APERTURA TICKET
	const formApriTicket = document.getElementById("formApriTicket");
	const msgTicket = document.getElementById("msgTicket");
	const ticketListContainer = document.getElementById("ticketListContainer");
	const emptyTicketText = document.getElementById("emptyTicketText");

	if (formApriTicket) {
		formApriTicket.addEventListener("submit", function(e) {
			e.preventDefault();
					            
			msgTicket.textContent = "Invio richiesta in corso...";
			msgTicket.className = "form-message-margin";

			const formDataObj = new FormData(this);
			formDataObj.append('action', 'apriTicket');
						            
			const oggettoInserito = document.getElementById("oggettoTicket").value;

			fetch("Profilo", { method: 'POST', body: formDataObj })
			.then(response => response.json())
			.then(data => {
				if (data.success) {
					msgTicket.textContent = data.message;
					msgTicket.className = "success-msg-js form-message-margin";
						                    
					if (emptyTicketText) {
						emptyTicketText.style.display = "none";
					}
					
					const li = document.createElement("li");
					li.className = "ticket-item";

					const divInfo = document.createElement("div");
					divInfo.className = "ticket-info";

					const spanId = document.createElement("span");
					spanId.className = "ticket-id";
					spanId.textContent = "Ticket #Nuovo - " + oggettoInserito;

					const spanDate = document.createElement("span");
					spanDate.className = "ticket-date";
					spanDate.textContent = "Appena aperto";

					divInfo.appendChild(spanId);
					divInfo.appendChild(spanDate);

                    const divActions = document.createElement("div");
                    divActions.className = "ticket-actions";

					const spanStatus = document.createElement("span");
					spanStatus.className = "status-badge status-warning";
					spanStatus.textContent = "Aperto";

                    const btnLeggi = document.createElement("button");
                    btnLeggi.className = "btn-link-small btn-leggi-ticket";
                    btnLeggi.setAttribute("data-id", "Nuovo"); 
                    btnLeggi.textContent = "Leggi";

                    divActions.appendChild(spanStatus);
                    divActions.appendChild(btnLeggi);

					li.appendChild(divInfo);
					li.appendChild(divActions);

                    if (ticketListContainer) {
                        ticketListContainer.prepend(li);
                    }

                    formApriTicket.reset();
               } else {
				msgTicket.textContent = data.message || "Impossibile aprire il ticket.";
				msgTicket.className = "error-msg-js form-message-margin";
			   }
			})
		    .catch(() => {
				msgTicket.textContent = "Errore di connessione al server.";
				msgTicket.className = "error-msg-js form-message-margin";
			});
		});
	}
							
	//GESTIONE LETTURA TICKET
    const modalTicket = document.getElementById("modalTicket");
    const closeModalTicketBtn = document.getElementById("closeModalTicketBtn");
    const corpoModalTicket = document.getElementById("corpoModalTicket");
    const modalTicketTitle = document.getElementById("modalTicketTitle");

    if (ticketListContainer) {
        ticketListContainer.addEventListener("click", function(e) {
            if (e.target.classList.contains("btn-leggi-ticket")) {
                const idTicket = e.target.getAttribute("data-id");
                modalTicketTitle.textContent = "#" + idTicket;
                
                while (corpoModalTicket.firstChild) {
                    corpoModalTicket.removeChild(corpoModalTicket.firstChild);
                }
                const pLoad = document.createElement("p");
                pLoad.className = "text-empty";
                pLoad.textContent = "Caricamento dettagli...";
                corpoModalTicket.appendChild(pLoad);

                modalTicket.classList.add("active");

                const params = new URLSearchParams();
                params.append('action', 'dettagliTicket');
                params.append('idTicket', idTicket);

                fetch("Profilo", { method: 'POST', body: params })
                .then(response => response.json())
                .then(data => {
                    while (corpoModalTicket.firstChild) {
                        corpoModalTicket.removeChild(corpoModalTicket.firstChild);
                    }

                    if (data.success) {
                        const boxUtente = document.createElement("div");
                        boxUtente.className = "ticket-msg-box";
                        
                        const titleUtente = document.createElement("div");
                        titleUtente.className = "ticket-msg-title";
                        titleUtente.textContent = "Il tuo messaggio:";
                        
                        const textUtente = document.createElement("div");
                        textUtente.className = "ticket-msg-text";
                        textUtente.textContent = data.messaggio;

                        boxUtente.appendChild(titleUtente);
                        boxUtente.appendChild(textUtente);
                        
                        if (data.allegato && data.allegato.trim() !== "") {
                            const imgAllegato = document.createElement("img");
                            imgAllegato.src = "images/tickets/" + data.allegato; 
                            imgAllegato.className = "ticket-msg-img";
                            imgAllegato.alt = "Foto Allegata al Ticket";
                            
                            boxUtente.appendChild(imgAllegato);
                        }
						
						corpoModalTicket.appendChild(boxUtente);

                        if (data.risposta && data.risposta.trim() !== "") {
                            const boxAdmin = document.createElement("div");
                            boxAdmin.className = "ticket-msg-box admin-response";
                            
                            const titleAdmin = document.createElement("div");
                            titleAdmin.className = "ticket-msg-title";
                            titleAdmin.textContent = "Risposta Assistenza:";
                            
                            const textAdmin = document.createElement("div");
                            textAdmin.className = "ticket-msg-text";
                            textAdmin.textContent = data.risposta;

                            boxAdmin.appendChild(titleAdmin);
                            boxAdmin.appendChild(textAdmin);
                            corpoModalTicket.appendChild(boxAdmin);
                        } else {
                            const pAttesa = document.createElement("p");
                            pAttesa.className = "text-empty";
                            pAttesa.textContent = "In attesa di risposta da un operatore...";
                            corpoModalTicket.appendChild(pAttesa);
                        }
                    } else {
                        const pError = document.createElement("p");
                        pError.className = "error-msg-js";
                        pError.textContent = data.message || "Errore nel caricamento.";
                        corpoModalTicket.appendChild(pError);
                    }
                })
                .catch(() => {
                    while (corpoModalTicket.firstChild) {
                        corpoModalTicket.removeChild(corpoModalTicket.firstChild);
                    }
                    const pError = document.createElement("p");
                    pError.className = "error-msg-js";
                    pError.textContent = "Errore di connessione al server.";
                    corpoModalTicket.appendChild(pError);
                });
            }
        });
    }

    if (closeModalTicketBtn) {
        closeModalTicketBtn.addEventListener("click", () => modalTicket.classList.remove("active"));
    }
    window.addEventListener("click", function(e) {
        if (e.target === modalTicket) {
            modalTicket.classList.remove("active");
        }
    });
});