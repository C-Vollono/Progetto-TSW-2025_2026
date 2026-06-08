document.addEventListener('DOMContentLoaded', () => {
  const searchInput  = document.getElementById('searchInput');
  const searchBtn    = document.getElementById('searchBtn');
  const searchBox    = document.getElementById('searchResults');

  let currentController = null;

  if (searchInput && searchBox) {
    searchInput.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        eseguiRicercaCompleta();
      }
    });

    
    if (searchBtn) {
      searchBtn.addEventListener('click', () => {
        eseguiRicercaCompleta();
      });
    }

    
    searchInput.addEventListener('input', () => {
      const term = searchInput.value.trim();

      if (term.length < 2) {
        searchBox.innerHTML = '';
        searchBox.style.display = 'none';
        return;
      }

      if (currentController) {
        currentController.abort();
      }
      currentController = new AbortController();

      fetch(`${contextPath}/RicercaSuggerimenti?q=` + encodeURIComponent(term), {
        method: 'GET',
        signal: currentController.signal
      })
        .then(res => {
          if (!res.ok) {
            throw new Error('HTTP error ' + res.status);
          }
          return res.json();
        })
        .then(data => {
          searchBox.innerHTML = '';

          if (!Array.isArray(data) || data.length === 0) {
            searchBox.style.display = 'none';
            return;
          }

          data.forEach(p => {
            const item = document.createElement('div');
            item.className = 'suggestion-item';
			const label = `${p.marca} ${p.modello} - € ${p.prezzo}`;  
            item.textContent = label;        

            item.addEventListener('click', () => {
				window.location.href = `${contextPath}/Catalogo?searchQuery=` + encodeURIComponent(`${p.marca} ${p.modello}`);
            });

            searchBox.appendChild(item);
          });

          searchBox.style.display = 'block';
        })
        .catch(err => {
          if (err.name !== 'AbortError') {
            console.error('Errore suggerimenti AJAX header', err);
          }
        });
    });

    document.addEventListener('click', (e) => {
      if (!searchBox.contains(e.target) && e.target !== searchInput) {
        searchBox.innerHTML = '';
        searchBox.style.display = 'none';
      }
    });
  }

  function eseguiRicercaCompleta() {
    const term = searchInput.value.trim();
    if (!term) return;
    window.location.href =
      `${contextPath}/Catalogo?searchQuery=` + encodeURIComponent(term);
  }
});