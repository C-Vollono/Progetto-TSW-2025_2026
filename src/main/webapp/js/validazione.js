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
		const dataInput = document.getElementById("data_di_nascita");
		
		function sbloccaBottoneSeValido() {
			const nomeOk = /^[a-zA-Z\s\']{2,50}$/.test(nomeInput.value.trim());
			const cognomeOk = /^[a-zA-Z\s\']{2,50}$/.test(cognomeInput.value.trim());
			const usernameOk = /^[a-zA-Z0-9_]{4,20}$/.test(usernameInput.value.trim());
			const emailOk = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/.test(emailInput.value.trim());
			const passwordOk = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(passwordInput.value);
			const confermaOk = (confermaPasswordInput.value === passwordInput.value && passwordInput.value !== "");
			const dataOk = (dataInput.value !== "");

			if (nomeOk && cognomeOk && usernameOk && emailOk && passwordOk && confermaOk && dataOk) {
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
		
		usernameInput.addEventListener("blur", function(){
			const regex = /^[a-zA-Z0-9_]{4,20}$/;
			const span = document.getElementById("usernameError");
			if(usernameInput.value.trim() !== "" && !regex.test(usernameInput.value.trim())){
				span.innerText = "L'username può contenere solo lettere, numeri e underscore (min 4 caratteri).";
			}else{
				span.innerText = "";
			}
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
		
		emailInput.addEventListener("blur", function(){
			const emailValue= emailInput.value.trim();
			const emailErrorSpan = document.getElementById("emailError");
			
			if(emailValue === ""){
				emailErrorSpan.innerText = "";
				return;
			}
			
			const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
			
			if(!emailRegex.test(emailValue)){
				
				emailErrorSpan.innerText = "Formato email non valido.";
				emailErrorSpan.style.color = "#D8000C";
				return;
			} else {
				emailErrorSpan.innerText = "";
			}
			
			fetch("CheckEmailServlet?email=" + encodeURIComponent(emailValue))
				.then(response => {
					if(!response.ok){
						throw new Error("Errore di rete durante la verifica dell'email.");
					}
					return response.json();
				})
				
				.then(data => {
					if(data.esiste){
						emailErrorSpan.innerText = "Email già registrata e utilizzata!";
						emailErrorSpan.style.color = "#D8000C"; 
					}else{
						emailErrorSpan.innerText = "Email disponibile.";
						emailErrorSpan.style.color = "green";
					}
				})
				.catch(error => {
					console.error("Errore Fetch API:", error);
				});
		});
		
		formRegistrazione.addEventListener("submit", function(event){
			let formValido = true;
			
			const nomeRegex = /^[a-zA-Z\s\']{2,50}$/;
			const nomeErrorSpan = document.getElementById("nomeError");
			if(!nomeRegex.test(nomeInput.value.trim())){
				nomeErrorSpan.innerText = "Il nome deve contenere solo lettere (minimo 2).";
				formValido = false;
			}else{
				nomeErrorSpan.innerText = "";
			}
			
			const cognomeRegex = /^[a-zA-Z\s\']{2,50}$/;
			const cognomeErrorSpan = document.getElementById("cognomeError");
			if(!cognomeRegex.test(cognomeInput.value.trim())){
				cognomeErrorSpan.innerText = "Il cognome deve contenere solo lettere (minimo 2).";
				formValido = false;
			}else{
				cognomeErrorSpan.innerText = "";
			}
			
			const usernameRegex = /^[a-zA-Z0-9_]{4,20}$/;
			const usernameErrorSpan = document.getElementById("usernameError");
			if(!usernameRegex.test(usernameInput.value.trim())){
				usernameErrorSpan.innerText = "L'username può contenere solo lettere, numeri e underscore (min4 caratteri).";
				formValido = false;
			}else{
				usernameErrorSpan.innerText = "";
			}
			
			const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
			const passwordErrorSpan = document.getElementById("passwordError");
			
			if(!passwordRegex.test(passwordInput.value)){
				passwordErrorSpan.innerText = "La password deve contenere almeno 8 caratteri, inclusi una lettera e un numero .";
				formValido = false;
			}else{
				passwordErrorSpan.innerText = "";
			}
			
			const confermaPasswordErrorSpan = document.getElementById("confermaPasswordError");
			if(confermaPasswordInput.value !== passwordInput.value){
					confermaPasswordErrorSpan.innerText = "Le password non coincidono.";
					formValido = false;
			}else{
				confermaPasswordErrorSpan.innerText = "";
			}
			
			const emailErrorSpan = document.getElementById("emailError");
			if(emailErrorSpan.innerText === "Email già registrata e utilizzata!" || emailErrorSpan.innerText === "Formato email non valido."){
				formValido = false;
			}
			
			if(!formValido){
				event.preventDefault();
			}
		});
	}
});