<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_form.css">

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
                            <label for="macroSelect">Categoria Principale</label>
                            <div class="select-btn-group">
                                <select id="macroSelect" name="tipo" required>
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
                            <div class="select-btn-group">
                                <select id="idMicro" name="idMicro" required>
                                    <option value="">-- Seleziona Sotto-Categoria --</option>
                                </select>
                                <button type="button" id="btn-add-micro" class="btn-inline-add" title="Nuova Sotto-Categoria">+</button>
                            </div>
                        </div>
                    </div>

                    <div id="box-nuova-macro">
                        <label class="inline-box-label">Nuova Categoria Principale:</label>
                        <div class="box-inline-row">
                            <input type="text" id="input-nuova-macro" placeholder="Es. Amplificatori">
                            <button type="button" id="submit-macro" class="btn-save-inline">Salva</button>
                        </div>
                    </div>

                    <div id="box-nuova-micro">
                        <label class="inline-box-label">Nuova Sotto-Categoria:</label>
                        <div class="box-inline-row">
                            <input type="text" id="input-nuova-micro" placeholder="Es. Valvolari">
                            <button type="button" id="submit-micro" class="btn-save-inline">Salva</button>
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
                        <input type="text" id="urlImmagine" name="urlImmagine"
                               value="${prodotto.urlImmagine}" required>
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
                        <img id="product-img-preview"
                             src="${pageContext.request.contextPath}/images/${prodotto.urlImmagine}"
                             alt="Anteprima">
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

<div id="js-data-bridge"
     data-context-path="${pageContext.request.contextPath}"
     data-id-micro-iniziale="${prodotto.idMicro != null ? prodotto.idMicro : 0}"
     data-tipo-iniziale="${prodotto.tipo}">
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
<jsp:include page="/jsp/footer.jsp" />