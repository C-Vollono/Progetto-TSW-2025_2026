document.addEventListener("DOMContentLoaded", function(){
	
	const formRegistrazione = document.getElementById("formRegistrazione");
	const emailInput = document.getElementById("email");
	const passwordInput = document.getElementById("password");
	const confermaPasswordInput = document.getElementById("confermaPassword"); 
	const nomeInput = document.getElementById("nome");
	const cognomeInput = document.getElementById("cognome");
	const usernameInput = document.getElementById("username");
	
	if(formRegistrazione){
		const btnRegistrati = document.getElementById("btnRegistrati");
		const dataInput = document.getElementById("dataNascita");
		let isEmailDisponibile = false;
		let isUsernameDisponibile = false;
		
		function sbloccaBottoneSeValido() {
			const nomeOk = /^[a-zA-Z\s\']{2,50}$/.test(nomeInput.value.trim());
			const cognomeOk = /^[a-zA-Z\s\']{2,50}$/.test(cognomeInput.value.trim());
			const usernameOk = /^[a-zA-Z0-9_]{4,20}$/.test(usernameInput.value.trim());
			const emailOk = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/.test(emailInput.value.trim());
			const passwordOk = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(passwordInput.value);
			const confermaOk = (confermaPasswordInput.value === passwordInput.value && passwordInput.value !== "");
			const dataOk = (dataInput.value !== "");

			//Sblocco del tasto Registrati
			if (nomeOk && cognomeOk && usernameOk && emailOk && passwordOk && confermaOk && dataOk && isEmailDisponibile && isUsernameDisponibile) {
				btnRegistrati.disabled = false;
			} else {
				btnRegistrati.disabled = true;
			}
		}
		
		formRegistrazione.addEventListener("input", sbloccaBottoneSeValido);
		
		nomeInput.addEventListener("blur", function(){
			const regex = /^[a-zA-Z\s\']{2,50}$/;
			const span = document.getElementById("nomeError");
			if(nomeInput.value.trim() !== "" && !regex.test(nomeInput.value.trim())){
				span.innerText = "Il nome deve contenere solo lettere (minimo 2).";
			}else{
				span.innerText = "";
			}
		});
		
		cognomeInput.addEventListener("blur", function(){
			const regex = /^[a-zA-Z\s\']{2,50}$/;
			const span= document.getElementById("cognomeError");
			if(cognomeInput.value.trim() !== "" && !regex.test(cognomeInput.value.trim())){
				span.innerText = "Il cognome deve contenere solo lettere (minimo 2).";
			}else{
				span.innerText = "";
			}
		});
		
		usernameInput.addEventListener("input", function() {
			isUsernameDisponibile = false;
			sbloccaBottoneSeValido();
		});
		
		usernameInput.addEventListener("blur", function(){
			const usernameValue = usernameInput.value.trim();
			const regex = /^[a-zA-Z0-9_]{4,20}$/;
			const span = document.getElementById("usernameError");
			
			if(usernameValue === ""){ span.innerText = ""; return; }
			
			if(!regex.test(usernameValue)){
				span.innerText = "L'username può contenere solo lettere, numeri e underscore (min 4 caratteri).";
				span.style.color = "#D8000C"; 
				return;
			} else {
				span.innerText = "";
			}
			
			fetch(contextPath + "/Registrazione?action=checkUsername&username=" + encodeURIComponent(usernameValue))
				.then(response => response.json())
				.then(data => {
					if(data.esiste){
						span.innerText = "Username già in uso! Scegline un altro.";
						span.style.color = "#D8000C"; 
						isUsernameDisponibile = false;
					}else{
						span.innerText = "Username disponibile.";
						span.style.color = "green";
						isUsernameDisponibile = true;
					}
					sbloccaBottoneSeValido();
				})
				.catch(error => console.error("Errore Fetch API:", error));
		});
		
		passwordInput.addEventListener("blur", function(){
			const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
			const span = document.getElementById("passwordError");
			if(passwordInput.value !== "" && !regex.test(passwordInput.value)){
				span.innerText = "La password deve contenere almeno 8 caratteri, inclusi una lettera e un numero.";
			}else{
				span.innerText = "";
			}
		});
		
		confermaPasswordInput.addEventListener("input", function(){
			const span = document.getElementById("confermaPasswordError");
			if(confermaPasswordInput.value === ""){
				span.innerText = "";
			} else if(confermaPasswordInput.value !== passwordInput.value){
				span.innerText = "Le password non coincidono.";
				span.style.color = "#D8000C";
			} else {
				span.innerText = "Le password coincidono.";
				span.style.color = "green";
			}
		});
		
		emailInput.addEventListener("input", function() {
			isEmailDisponibile = false;
			sbloccaBottoneSeValido();
		});

		emailInput.addEventListener("blur", function(){
			const emailValue= emailInput.value.trim();
			const emailErrorSpan = document.getElementById("emailError");
			
			if(emailValue === ""){ emailErrorSpan.innerText = ""; return; }
			
			const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
			
			if(!emailRegex.test(emailValue)){
				emailErrorSpan.innerText = "Formato email non valido.";
				emailErrorSpan.style.color = "#D8000C";
				return;
			} else {
				emailErrorSpan.innerText = "";
			}
			
			fetch(contextPath + "/Registrazione?action=checkEmail&email=" + encodeURIComponent(emailValue))
				.then(response => response.json())
				.then(data => {
					if(data.esiste){
						emailErrorSpan.innerText = "Email già registrata e utilizzata!";
						emailErrorSpan.style.color = "#D8000C"; 
						isEmailDisponibile = false;
					}else{
						emailErrorSpan.innerText = "Email disponibile.";
						emailErrorSpan.style.color = "green";
						isEmailDisponibile = true;
					}
					sbloccaBottoneSeValido();
				})
				.catch(error => console.error("Errore Fetch API:", error));
		});
	}
});