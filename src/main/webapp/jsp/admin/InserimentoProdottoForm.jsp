<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header_admin.jsp" />

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_form.css">

<main class="admin-form-container-v2">
    <div class="form-title-wrapper">
        <h2>Inserisci Nuovo Prodotto</h2>
        <p class="subtitle">Aggiungi un nuovo strumento o accessorio al catalogo di ToneMarket</p>
    </div>

    <form action="${pageContext.request.contextPath}/Admin/GestioneProdotti" method="POST" class="main-form-layout">
        <input type="hidden" name="action" value="insert">

        <div class="form-content-columns">
            <div class="inputs-column">
                
                <div class="form-section-card">
                    <h3>Informazioni Generali</h3>
                    <div class="form-group">
                        <label for="nome">Nome Prodotto</label>
                        <input type="text" id="nome" name="nome" placeholder="Es. Gibson Les Paul Standard" required>
                    </div>
                    <div class="form-group">
                        <label for="marca">Marca</label>
                        <input type="text" id="marca" name="marca" placeholder="Es. Gibson" required>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Dettagli Prezzo e Stock</h3>
                    <div class="form-row-two-columns">
                        <div class="form-group-v2">
                            <label for="prezzo">Prezzo (€)</label>
                            <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" placeholder="0.00" required>
                        </div>
                        <div class="form-group-v2">
                            <label for="quantita">Quantità</label>
                            <input type="number" id="quantita" name="quantita" min="0" placeholder="0" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Descrizione</h3>
                    <div class="form-group">
                        <label for="descrizione">Descrizione Tecnica Estesa</label>
                        <textarea id="descrizione" name="descrizione" rows="6" required></textarea>
                    </div>
                </div>

            </div>

            <div class="preview-column">
                <div class="sticky-preview-card">
                    <h3>Anteprima Immagine</h3>
                    <div class="image-preview-box">
                        <img id="preview-img" src="" alt="Anteprima">
                    </div>
                    <div class="form-group-v2">
                        <label for="urlImmagine">Percorso File Immagine</label>
                        <input type="text" id="urlImmagine" name="urlImmagine" placeholder="Es. chitarre/lespaul.png" required oninput="updatePreview(this.value)">
                        <p class="image-path-indicator" style="margin-top: 8px;">
                            L'immagine deve trovarsi in: <span>/images/</span>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-actions-footer-v2">
            <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti" class="btn-cancel-v2">Annulla</a>
            <button type="submit" class="btn-submit-v2">Salva Prodotto</button>
        </div>
    </form>
</main>

<script>
function updatePreview(path) {
    const previewImg = document.getElementById('preview-img');
    const contextPath = '${pageContext.request.contextPath}';
    
    if (path.trim() !== '') {
        // Rimuove slash iniziale se presente
        const cleanPath = path.startsWith('/') ? path.substring(1) : path;
        previewImg.src = contextPath + '/images/' + cleanPath;
    } else {
        previewImg.src = '';
    }
}
</script>

<jsp:include page="/jsp/footer.jsp" />