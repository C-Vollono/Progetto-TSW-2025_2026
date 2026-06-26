document.addEventListener('DOMContentLoaded', () => {

    // =========================================================
    // 1. LOGICA FORM ADMIN (solo nelle pagine che hanno js-data-bridge)
    // =========================================================
    const dataBridge = document.getElementById('js-data-bridge');

    if (dataBridge) {
        const contextPath = dataBridge.getAttribute('data-context-path');
        const idMicroIniziale = parseInt(dataBridge.getAttribute('data-id-micro-iniziale'), 10);
        const tipoIniziale = dataBridge.getAttribute('data-tipo-iniziale');

        let sottoCategorie = [];
        try {
            const microDataJson = document.getElementById('micro-data-json');
            if (microDataJson) {
                sottoCategorie = JSON.parse(microDataJson.textContent);
            }
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

        function caricaSottoCategorie() {
            if (!macroSelect || !microSelect) return;

            const selectedOption = macroSelect.options[macroSelect.selectedIndex];
            const idMacroSelezionata = selectedOption
                ? selectedOption.getAttribute('data-id')
                : null;

            microSelect.innerHTML = '<option value="">-- Seleziona Sotto-Categoria --</option>';

            if (idMacroSelezionata) {
                const filtrate = sottoCategorie.filter(
                    micro => micro.idMacro == idMacroSelezionata
                );

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

        if (btnAddMacro && boxNuovaMacro) {
            btnAddMacro.addEventListener('click', () => {
                boxNuovaMacro.style.display =
                    boxNuovaMacro.style.display === 'none' ? 'block' : 'none';
            });
        }

        if (btnAddMicro && boxNuovaMicro) {
            btnAddMicro.addEventListener('click', () => {
                if (!macroSelect || !macroSelect.value) {
                    alert('Seleziona prima una Categoria Principale!');
                    return;
                }

                boxNuovaMicro.style.display =
                    boxNuovaMicro.style.display === 'none' ? 'block' : 'none';
            });
        }

        if (submitMacro && inputNuovaMacro && macroSelect && boxNuovaMacro) {
            submitMacro.addEventListener('click', () => {
                const nome = inputNuovaMacro.value.trim();
                if (!nome) return;

                fetch(`${contextPath}/Admin/AggiungiCategoria`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `target=macro&nomeMacro=${encodeURIComponent(nome)}`
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
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
        }

        if (submitMicro && inputNuovaMicro && macroSelect && boxNuovaMicro) {
            submitMicro.addEventListener('click', () => {
                const nome = inputNuovaMicro.value.trim();
                const selectedOption = macroSelect.options[macroSelect.selectedIndex];
                const idMacro = selectedOption
                    ? selectedOption.getAttribute('data-id')
                    : null;

                if (!nome || !idMacro) return;

                fetch(`${contextPath}/Admin/AggiungiCategoria`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `target=micro&nomeMicro=${encodeURIComponent(nome)}&idMacro=${idMacro}`
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        sottoCategorie.push({
                            idMicro: data.idMicro,
                            nome: data.nomeMicro,
                            idMacro: data.idMacro
                        });

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
        }

        if (macroSelect) {
            macroSelect.addEventListener('change', caricaSottoCategorie);
        }

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

        if (tipoIniziale && macroSelect) {
            macroSelect.value = tipoIniziale;
            caricaSottoCategorie();
        }
    }

    // =========================================================
    // 2. MODALE ELIMINAZIONE (sempre attiva nel catalogo admin)
    // =========================================================
    const deleteModalOverlay = document.getElementById('delete-confirm-modal');
    const deleteConfirmBtn = document.getElementById('delete-confirm-btn');
    const deleteCancelBtn = document.getElementById('delete-cancel-btn');
    const deleteMessageEl = document.getElementById('delete-modal-message');
    const deleteForms = document.querySelectorAll('.form-delete-container');

    let deleteFormToSubmit = null;

    if (
        deleteModalOverlay &&
        deleteConfirmBtn &&
        deleteCancelBtn &&
        deleteMessageEl &&
        deleteForms.length > 0
    ) {
        deleteForms.forEach(form => {
            form.addEventListener('submit', (e) => {
                e.preventDefault();

                deleteFormToSubmit = form;

                const nomeProdotto =
                    form.getAttribute('data-product-name') || 'questo prodotto';

                deleteMessageEl.textContent =
                    `Sei sicuro di voler eliminare definitivamente ${nomeProdotto}?`;

                deleteModalOverlay.style.display = 'flex';
            });
        });

        const chiudiModale = () => {
            deleteModalOverlay.style.display = 'none';
            deleteFormToSubmit = null;
        };

        deleteConfirmBtn.addEventListener('click', () => {
            if (deleteFormToSubmit) {
                const form = deleteFormToSubmit;
                chiudiModale();
                form.submit();
            }
        });

        deleteCancelBtn.addEventListener('click', chiudiModale);

        deleteModalOverlay.addEventListener('click', (e) => {
            if (e.target === deleteModalOverlay) {
                chiudiModale();
            }
        });
    }
});