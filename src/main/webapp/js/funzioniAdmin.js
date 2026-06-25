document.addEventListener('DOMContentLoaded', () => {
    const dataBridge = document.getElementById('js-data-bridge');
    if (!dataBridge) return;

    const contextPath = dataBridge.getAttribute('data-context-path');
    const idMicroIniziale = parseInt(dataBridge.getAttribute('data-id-micro-iniziale'), 10);
    const tipoIniziale = dataBridge.getAttribute('data-tipo-iniziale');
    
    let sottoCategorie = [];
    try {
        sottoCategorie = JSON.parse(document.getElementById('micro-data-json').textContent);
    } catch (e) {
        console.error("Errore nel parsing delle sotto-categorie JSON", e);
    }

    const macroSelect = document.getElementById('macroSelect');
    const microSelect = document.getElementById('idMicro');
    const urlImmagineInput = document.getElementById('urlImmagine');
    const imgPreview = document.getElementById('product-img-preview');

    const btnAddMacro = document.getElementById('btn-add-macro');
    const boxNuovaMacro = document.getElementById('box-nuova-macro');
    const inputNuovaMacro = document.getElementById('input-nuova-macro');
    const submitMacro = document.getElementById('submit-macro');

    const btnAddMicro = document.getElementById('btn-add-micro');
    const boxNuovaMicro = document.getElementById('box-nuova-micro');
    const inputNuovaMicro = document.getElementById('input-nuova-micro');
    const submitMicro = document.getElementById('submit-micro');

    // 1. Funzione di caricamento e filtraggio dinamico delle micro categorie
    function caricaSottoCategorie() {
        const selectedOption = macroSelect.options[macroSelect.selectedIndex];
        const idMacroSelezionata = selectedOption ? selectedOption.getAttribute('data-id') : null;
        
        microSelect.innerHTML = '<option value="">-- Seleziona Sotto-Categoria --</option>';
        
        if (idMacroSelezionata) {
            const filtrate = sottoCategorie.filter(micro => micro.idMacro == idMacroSelezionata);
            
            filtrate.forEach(micro => {
                const opt = document.createElement('option');
                opt.value = micro.idMicro;
                opt.textContent = micro.nome;
                
                if (micro.idMicro === idMicroIniziale) {
                    opt.selected = true;
                }
                microSelect.appendChild(opt);
            });
        }
    }

    // Toggle dei box input nuova macro
    btnAddMacro.addEventListener('click', () => {
        boxNuovaMacro.style.display = boxNuovaMacro.style.display === 'none' ? 'block' : 'none';
    });

    // Toggle del box input nuova micro (solo se è selezionata una macro)
    btnAddMicro.addEventListener('click', () => {
        if (!macroSelect.value) {
            alert('Seleziona prima una Categoria Principale!');
            return;
        }
        boxNuovaMicro.style.display = boxNuovaMicro.style.display === 'none' ? 'block' : 'none';
    });

    // 2. Chiamata AJAX per inserire la nuova Macrocategoria
    submitMacro.addEventListener('click', () => {
        const nome = inputNuovaMacro.value.trim();
        if(!nome) return;

        fetch(`${contextPath}/Admin/AggiungiCategoria`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `target=macro&nomeMacro=${encodeURIComponent(nome)}`
        })
        .then(res => res.json())
        .then(data => {
            if(data.success) {
                const opt = document.createElement('option');
                opt.value = data.nomeMacro;
                opt.setAttribute('data-id', data.idMacro);
                opt.textContent = data.nomeMacro;
                opt.selected = true;
                macroSelect.appendChild(opt);
                
                inputNuovaMacro.value = '';
                boxNuovaMacro.style.display = 'none';
                caricaSottoCategorie();
            } else {
                alert(data.message);
            }
        })
        .catch(err => console.error("Errore aggiunta macro:", err));
    });

    // 3. Chiamata AJAX per inserire la nuova Microcategoria
    submitMicro.addEventListener('click', () => {
        const nome = inputNuovaMicro.value.trim();
        const selectedOption = macroSelect.options[macroSelect.selectedIndex];
        const idMacro = selectedOption ? selectedOption.getAttribute('data-id') : null;

        if(!nome || !idMacro) return;

        fetch(`${contextPath}/Admin/AggiungiCategoria`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `target=micro&nomeMicro=${encodeURIComponent(nome)}&idMacro=${idMacro}`
        })
        .then(res => res.json())
        .then(data => {
            if(data.success) {
                sottoCategorie.push({ idMicro: data.idMicro, nome: data.nomeMicro, idMacro: data.idMacro });
                caricaSottoCategorie();
                microSelect.value = data.idMicro;
                
                inputNuovaMicro.value = '';
                boxNuovaMicro.style.display = 'none';
            } else {
                alert(data.message);
            }
        })
        .catch(err => console.error("Errore aggiunta micro:", err));
    });

    macroSelect.addEventListener('change', caricaSottoCategorie);
    
    // Anteprima immagine prodotto (updatePreview esterno alla JSP)
    if (urlImmagineInput && imgPreview) {
        urlImmagineInput.addEventListener('input', (e) => {
            const raw = e.target.value.trim();
            if (raw === '') {
                imgPreview.src = '';
                return;
            }
            const normalized = raw.startsWith('/')
                ? raw.substring(1)
                : raw.replace('images/', '');
            imgPreview.src = contextPath + "/images/" + normalized;
        });
    }

    // Selezione iniziale basata su "tipoIniziale"
    if (tipoIniziale) {
        macroSelect.value = tipoIniziale;
        caricaSottoCategorie();
    }
});