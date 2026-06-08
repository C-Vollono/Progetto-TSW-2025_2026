document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('catalog-search');
  const box   = document.getElementById('search-suggestions');

  if (!input || !box) return;

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
});