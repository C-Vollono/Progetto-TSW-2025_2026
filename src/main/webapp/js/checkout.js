document.addEventListener("DOMContentLoaded", function() {
    const radioCards = document.querySelectorAll('.radio-card');

    radioCards.forEach(card => {
        const radioInput = card.querySelector('.radio-input');
        
        if (radioInput && radioInput.checked) {
            card.classList.add('card-selezionata');
        }
        
        card.addEventListener('mouseenter', function() {
            this.classList.add('card-hover');
        });

        card.addEventListener('mouseleave', function() {
            this.classList.remove('card-hover');
        });
        
        if (radioInput) {
            radioInput.addEventListener('change', function() {
                const groupName = this.name;                
                
                document.querySelectorAll(`input[name="${groupName}"]`).forEach(otherRadio => {
                    otherRadio.closest('.radio-card').classList.remove('card-selezionata');
                });                
                
                if (this.checked) {
                    card.classList.add('card-selezionata');
                }
            });
        }
    });
	
	//GESTIONE SPEDIZIONE
    const radioSpedizioni = document.querySelectorAll('input[name="idSpedizione"]');
    const boxNuovaSpedizione = document.getElementById('boxNuovaSpedizione');
    
    function toggleSpedizioneForm() {
        const radioSelezionato = document.querySelector('input[name="idSpedizione"]:checked');
        if (!radioSelezionato || !boxNuovaSpedizione) return;

        const isNuovo = radioSelezionato.value === 'nuovo';
        const inputs = boxNuovaSpedizione.querySelectorAll('input, select');

        if (isNuovo) {
            boxNuovaSpedizione.classList.add('active');
            inputs.forEach(input => {
                if(input.name !== 'nuovoTelefono') {
                    input.setAttribute('required', 'true');
                }
            });
        } else {
            boxNuovaSpedizione.classList.remove('active');
            inputs.forEach(input => {
                input.removeAttribute('required');
                input.value = '';
            });
        }
    }

    //GESTIONE PAGAMENTO
    const radioPagamenti = document.querySelectorAll('input[name="idPagamento"]');
    const boxNuovoPagamento = document.getElementById('boxNuovoPagamento');

    function togglePagamentoForm() {
        const radioSelezionato = document.querySelector('input[name="idPagamento"]:checked');
        if (!radioSelezionato || !boxNuovoPagamento) return;

        const isNuovo = radioSelezionato.value === 'nuovo';
        const inputs = boxNuovoPagamento.querySelectorAll('input, select');

        if (isNuovo) {
            boxNuovoPagamento.classList.add('active');
            inputs.forEach(input => input.setAttribute('required', 'true'));
        } else {
            boxNuovoPagamento.classList.remove('active');
            inputs.forEach(input => {
                input.removeAttribute('required');
                input.value = '';
            });
        }
    }

    radioSpedizioni.forEach(radio => radio.addEventListener('change', toggleSpedizioneForm));
    radioPagamenti.forEach(radio => radio.addEventListener('change', togglePagamentoForm));

    if (radioSpedizioni.length > 0) toggleSpedizioneForm();
    if (radioPagamenti.length > 0) togglePagamentoForm();
	
	//VALIDAZIONE FORM
    const formCheckout = document.getElementById("formCheckout");

    if (formCheckout) {
        formCheckout.addEventListener("submit", function(e) {
            let isValid = true;
            
            document.querySelectorAll(".error-msg-js").forEach(span => span.textContent = "");
            
            const radioSpedizione = document.querySelector('input[name="idSpedizione"]:checked');
            if (radioSpedizione && radioSpedizione.value === 'nuovo') {
                const via = document.getElementById("nuovaVia").value.trim();
                const civico = document.getElementById("nuovoCivico").value.trim();
                const citta = document.getElementById("nuovaCitta").value.trim();
                const provincia = document.getElementById("nuovaProvincia").value.trim();
                const cap = document.getElementById("nuovoCap").value.trim();
                const telefono = document.getElementById("nuovoTelefono").value.trim();

                if (via === "") {
                    document.getElementById("errNuovaVia").textContent = "Inserisci la via.";
                    isValid = false;
                }
                if (civico === "") {
                    document.getElementById("errNuovoCivico").textContent = "Inserisci il civico.";
                    isValid = false;
                }
                if (citta === "") {
                    document.getElementById("errNuovaCitta").textContent = "Inserisci la città.";
                    isValid = false;
                }
                
                //Esattamente 2 lettere
                if (!/^[A-Za-z]{2}$/.test(provincia)) {
                    document.getElementById("errNuovaProvincia").textContent = "Inserisci la sigla di 2 lettere (es. NA).";
                    isValid = false;
                }
                
                //Esattamente 5 numeri
                if (!/^\d{5}$/.test(cap)) {
                    document.getElementById("errNuovoCap").textContent = "Il CAP deve contenere 5 numeri.";
                    isValid = false;
                }
                
                // Regex Telefono almeno 9-10 numeri
                if (telefono !== "" && !/^\d{9,10}$/.test(telefono)) {
                    document.getElementById("errNuovoTelefono").textContent = "Inserisci un numero valido.";
                    isValid = false;
                }
            }

            //VALIDAZIONE NUOVO PAGAMENTO
            const radioPagamento = document.querySelector('input[name="idPagamento"]:checked');
            if (radioPagamento && radioPagamento.value === 'nuovo') {
                const circuito = document.getElementById("nuovoCircuito").value;
                const numeroCarta = document.getElementById("nuovoNumeroCarta").value.trim();
                const intestatario = document.getElementById("nuovoIntestatario").value.trim();
                const scadenza = document.getElementById("nuovaScadenza").value;

                if (circuito === "") {
                    document.getElementById("errNuovoCircuito").textContent = "Seleziona un circuito.";
                    isValid = false;
                }
                
                //16 numeri con spazi vuoti ogni 4
                if (!/^(\d{4}\s?){3}\d{4}$/.test(numeroCarta)) {
                    document.getElementById("errNuovoNumeroCarta").textContent = "Inserisci le 16 cifre della carta.";
                    isValid = false;
                }
                
                //Solo lettere e spazi per il nome
                if (!/^[A-Za-z\s]+$/.test(intestatario)) {
                    document.getElementById("errNuovoIntestatario").textContent = "Inserisci un nome valido.";
                    isValid = false;
                }
                
                if (scadenza === "") {
                    document.getElementById("errNuovaScadenza").textContent = "Inserisci la scadenza.";
                    isValid = false;
                } else {
                    const inputDate = new Date(scadenza + "-01");
                    const currentDate = new Date();
                    currentDate.setDate(1); // Ignoriamo il giorno attuale
                    currentDate.setHours(0,0,0,0);
                    
                    if (inputDate < currentDate) {
                        document.getElementById("errNuovaScadenza").textContent = "La carta risulta scaduta.";
                        isValid = false;
                    }
                }
            }
            if (!isValid) {
                e.preventDefault();
            }
        });
    }
});