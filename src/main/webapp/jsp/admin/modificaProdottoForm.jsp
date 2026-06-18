<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-style.css">

<jsp:include page="header_admin.jsp" />

<main class="admin-form-container-v2">
    <div class="form-title-wrapper">
        <h2>Modifica Dettagli Prodotto</h2>
        <p class="subtitle">Modifica le informazioni correnti del prodotto all'interno del catalogo.</p>
    </div>
    
    <form action="${pageContext.request.contextPath}/Admin/GestioneProdotti" method="POST" class="main-form-layout">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">

        <div class="form-content-columns">
            
            <div class="inputs-column">
                
                <div class="form-section-card">
                    <h3>Informazioni Generali</h3>
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="marca">Marca</label>
                            <input type="text" id="marca" name="marca" value="${prodotto.marca}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="modello">Modello</label>
                            <input type="text" id="modello" name="modello" value="${prodotto.modello}" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Classificazione</h3>
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="tipo">Categoria (Tipo)</label>
                            <input type="text" id="tipo" name="tipo" value="${prodotto.tipo}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="idMicro">ID Sotto-Categoria</label>
                            <input type="number" id="idMicro" name="idMicro" value="${prodotto.idMicro}" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Prezzo e Magazzino</h3>
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="prezzo">Prezzo Unitario (€)</label>
                            <input type="number" id="prezzo" name="prezzo" step="0.01" value="${prodotto.prezzo}" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="quantita">Pezzi Disponibili</label>
                            <input type="number" id="quantita" name="quantita" value="${prodotto.quantita}" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Media e Contenuti</h3>
                    <div class="form-group mb-15">
                        <label for="urlImmagine">Nome File Immagine / URL</label>
                        <input type="text" id="urlImmagine" name="urlImmagine" value="${prodotto.urlImmagine}" oninput="updatePreview(this.value)" required>
                    </div>

                    <div class="form-group">
                        <label for="descrizione">Descrizione Tecnica Estesa</label>
                        <textarea id="descrizione" name="descrizione" required>${prodotto.descrizione}</textarea>
                    </div>
                </div>
                
            </div>

            <div class="preview-column">
                <div class="sticky-preview-card">
                    <h3>Anteprima Immagine</h3>
                    <div class="image-preview-box">
                        <img id="product-img-preview" src="${pageContext.request.contextPath}/images/${prodotto.urlImmagine.replace('images/', '')}" alt="Anteprima">
                    </div>
                    <p class="image-path-indicator">File caricato: <span>${prodotto.urlImmagine}</span></p>
                </div>
            </div>

        </div>

        <div class="form-actions-footer-v2">
            <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti" class="btn-cancel-back-v2">Annulla Modifiche</a>
            <button type="submit" class="btn-submit-save-v2">Salva ed Applica</button>
        </div>
    </form>
</main>

<script>
function updatePreview(val) {
    const img = document.getElementById('product-img-preview');
    const cleanVal = val.replace('images/', '');
    img.src = "${pageContext.request.contextPath}/images/" + cleanVal;
}
</script>

<jsp:include page="/jsp/footer.jsp" />