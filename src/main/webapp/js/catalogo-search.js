document.addEventListener('DOMContentLoaded', () => {
  //LOGICA SUGGERIMENTI RICERCA TESTUALE (AUTOCOMPLETE AJAX)
  const input = document.getElementById('catalog-search');
  const box   = document.getElementById('search-suggestions');

  if (input && box) {
    let currentController = null;

    input.addEventListener('input', () => {
      const term = input.value.trim();

      if (term.length < 2) {
        box.innerHTML = '';
        box.style.display = 'none';
        return;
      }

      if (currentController) {
        currentController.abort();
      }
      currentController = new AbortController();

      fetch(`${contextPath}/SuggerimentiProdotti?term=` + encodeURIComponent(term), {
        method: 'GET',
        signal: currentController.signal
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('HTTP error ' + response.status);
          }
          return response.json();
        })
        .then(data => {
          box.innerHTML = '';

          if (!Array.isArray(data) || data.length === 0) {
            box.style.display = 'none';
            return;
          }

          data.forEach(prod => {
            const item = document.createElement('div');
            item.className = 'suggestion-item';
            item.textContent = prod.nome;

            item.addEventListener('click', () => {
              input.value = prod.nome;
              box.innerHTML = '';
              box.style.display = 'none';
              input.form.submit();
            });

            box.appendChild(item);
          });

          box.style.display = 'block';
        })
        .catch(err => {
          if (err.name !== 'AbortError') {
            console.error('Errore suggerimenti AJAX', err);
          }
        });
    });
  }
});

//FUNZIONE PER AGGIORNAMENTO DELLE MICROCATEGORIE

const macroSelect = document.getElementById('categoria'); 
  
  if (macroSelect) {
    macroSelect.addEventListener('change', (event) => {
      // Prende l'ID della categoria selezionata
      const idSelezionato = event.target.value; 
      // Richiama la funzione AJAX che popola le sotto-categorie
      window.caricaMicrocategorie(idSelezionato); 
    });
  }

//FUNZIONE GLOBALE: CARICAMENTO ASINCRONO DELLE MICROCATEGORIE (ID MACRO)
window.caricaMicrocategorie = function(macroId) {
  const microSelect = document.getElementById('microcategoria');
  const microGroup = document.getElementById('filter-group-micro');

  // Se l'utente seleziona "Tutte" (All), svuota e nascondi il filtro micro
  if (!macroId || macroId === 'All') {
    if (microSelect) microSelect.innerHTML = '<option value="All">Tutte le sotto-categorie</option>';
    if (microGroup) microGroup.style.display = 'none';
    return;
  }

  // Chiamata AJAX verso la servlet GetMicrocategorie passando l'id numerico
  fetch(`${contextPath}/GetMicrocategorie?macroId=` + encodeURIComponent(macroId))
    .then(response => {
      if (!response.ok) {
        throw new Error('Errore di comunicazione server AJAX (Microcategorie)');
      }
      return response.json();
    })
    .then(data => {
      if (!microSelect) return;
      
      // Ripristina l'opzione di default del select
      microSelect.innerHTML = '<option value="All">Tutte le sotto-categorie</option>';

      if (data && data.length > 0) {
        // Popola la select ciclando l'array JSON ricevuto
        data.forEach(micro => {
          const option = document.createElement('option');
          option.value = micro.id;
          option.textContent = micro.nome;
          microSelect.appendChild(option);
        });
        
        // Rende visibile il contenitore della sotto-categoria
        if (microGroup) microGroup.style.display = 'block';
      } else {
        if (microGroup) microGroup.style.display = 'none';
      }
    })
    .catch(error => {
      console.error('Errore nel caricamento delle microcategorie:', error);
    });
};