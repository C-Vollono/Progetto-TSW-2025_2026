<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_form.css">

<jsp:include page="header_admin.jsp" />

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
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="marca">Marca</label>
                            <input type="text" id="marca" name="marca" placeholder="Es. Gibson" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="modello">Modello</label>
                            <input type="text" id="modello" name="modello" placeholder="Es. Les Paul Standard" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Classificazione</h3>
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="macroSelect">Categoria Principale</label>
                            <div class="select-btn-group" style="display: flex; gap: 8px;">
                                <select id="macroSelect" name="tipo" required style="flex-grow: 1;">
                                    <option value="">-- Seleziona Categoria --</option>
                                    <c:if test="${not empty tutteLeMacro}">
                                        <c:forEach var="macro" items="${tutteLeMacro}">
                                            <option value="${macro.nomeMacro}" data-id="${macro.idMacro}">${macro.nomeMacro}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                                <button type="button" id="btn-add-macro" class="btn-inline-add" title="Nuova Categoria">+</button>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="idMicro">Sotto-Categoria</label>
                            <div class="select-btn-group" style="display: flex; gap: 8px;">
                                <select id="idMicro" name="idMicro" required style="flex-grow: 1;">
                                    <option value="">-- Seleziona Sotto-Categoria --</option>
                                </select>
                                <button type="button" id="btn-add-micro" class="btn-inline-add" title="Nuova Sotto-Categoria">+</button>
                            </div>
                        </div>
                    </div>

                    <div id="box-nuova-macro" style="display:none; margin-top:12px; padding:10px; background:#f4f6f9; border-radius:6px;">
                        <label style="font-size:12px; font-weight:bold;">Nuova Categoria Principale:</label>
                        <div style="display:flex; gap:6px; margin-top:4px;">
                            <input type="text" id="input-nuova-macro" placeholder="Es. Amplificatori">
                            <button type="button" id="submit-macro" style="background:#28a745; color:white; border:none; padding:4px 10px; border-radius:4px; cursor:pointer;">Salva</button>
                        </div>
                    </div>

                    <div id="box-nuova-micro" style="display:none; margin-top:12px; padding:10px; background:#f4f6f9; border-radius:6px;">
                        <label style="font-size:12px; font-weight:bold;">Nuova Sotto-Categoria:</label>
                        <div style="display:flex; gap:6px; margin-top:4px;">
                            <input type="text" id="input-nuova-micro" placeholder="Es. Valvolari">
                            <button type="button" id="submit-micro" style="background:#28a745; color:white; border:none; padding:4px 10px; border-radius:4px; cursor:pointer;">Salva</button>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Prezzo e Magazzino</h3>
                    <div class="form-grid-2col">
                        <div class="form-group">
                            <label for="prezzo">Prezzo Unitario (€)</label>
                            <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" placeholder="0.00" required>
                        </div>
                        
                        <div class="form-group">
                            <label for="quantita">Pezzi Disponibili</label>
                            <input type="number" id="quantita" name="quantita" min="0" placeholder="0" required>
                        </div>
                    </div>
                </div>

                <div class="form-section-card">
                    <h3>Media e Contenuti</h3>
                    <div class="form-group mb-15">
                        <label for="urlImmagine">Nome File Immagine / URL</label>
                        <input type="text" id="urlImmagine" name="urlImmagine" placeholder="Es. chitarre/lespaul.png" required oninput="updatePreview(this.value)">
                    </div>

                    <div class="form-group">
                        <label for="descrizione">Descrizione Tecnica Estesa</label>
                        <textarea id="descrizione" name="descrizione" placeholder="Inserisci una descrizione accurata del prodotto..." required></textarea>
                    </div>
                </div>
                
            </div>

            <div class="preview-column">
                <div class="sticky-preview-card">
                    <h3>Anteprima Immagine</h3>
                    <div class="image-preview-box">
                        <img id="product-img-preview" src="" alt="Anteprima">
                    </div>
                    <p class="image-path-indicator">L'immagine deve trovarsi in: <span>/images/</span></p>
                </div>
            </div>

        </div>

        <div class="form-actions-footer-v2">
            <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti" class="btn-cancel-back-v2">Annulla</a>
            <button type="submit" class="btn-submit-save-v2">Salva Prodotto</button>
        </div>
    </form>
</main>

<div id="js-data-bridge" 
     data-context-path="${pageContext.request.contextPath}"
     data-id-micro-iniziale="0"
     data-tipo-iniziale=""
     style="display:none;">
    <script id="micro-data-json" type="application/json">
        [
            <c:if test="${not empty tutteLeMicro}">
                <c:forEach var="micro" items="${tutteLeMicro}" varStatus="loop">
                    { 
                      "idMicro": ${micro.idMicro}, 
                      "nome": "${micro.nomeMicro.replace('"', '\\"')}", 
                      "idMacro": ${micro.idMacro} 
                    }${!loop.last ? ',' : ''}
                </c:forEach>
            </c:if>
        ]
    </script>
</div>

<script src="${pageContext.request.contextPath}/js/funzioniAdmin.js"></script>
<script>
// Mantieni aggiornata l'anteprima anche col nuovo ID del tag img usato dal JS comune
function updatePreview(path) {
    const previewImg = document.getElementById('product-img-preview');
    const contextPath = '${pageContext.request.contextPath}';
    
    if (path.trim() !== '') {
        const cleanPath = path.startsWith('/') ? path.substring(1) : path;
        previewImg.src = contextPath + '/images/' + cleanPath;
    } else {
        previewImg.src = '';
    }
}
</script>

<jsp:include page="/jsp/footer.jsp" />