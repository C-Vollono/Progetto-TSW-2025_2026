<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/catalogo.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin_catalogo.css">

<jsp:include page="header_admin.jsp" />

<section class="catalog-section">
    
    <div class="catalog-header">
        <div class="catalog-header-title">
            <h2>Gestione Catalogo <span class="premium-text">Premium</span></h2>
        </div>
        <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti?action=insertForm" class="btn-add-product">
            + Inserisci Nuovo Prodotto
        </a>
    </div>
    
    <c:if test="${not empty sessionScope.messaggioSuccesso}">
        <div class="alert-message alert-success">
            ${sessionScope.messaggioSuccesso}
        </div>
        <c:remove var="messaggioSuccesso" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.messaggioErrore}">
        <div class="alert-message alert-danger">
            ${sessionScope.messaggioErrore}
        </div>
        <c:remove var="messaggioErrore" scope="session" />
    </c:if>
    
    <div class="catalog-layout">
        
        <aside class="catalog-sidebar">
            <h3>Filtri</h3>
            <form action="${pageContext.request.contextPath}/Admin/GestioneProdotti" method="GET">
                <input type="hidden" name="searchQuery" value="${searchQuery}">
                <input type="hidden" name="ordina" value="${empty selOrdina ? 'rilevanza' : selOrdina}">
                
                <div class="filter-group">
                    <label for="categoria">Categoria:</label>
                    <select name="categoria" id="categoria">
                        <option value="All" ${empty selCategoria || selCategoria == 'All' ? 'selected' : ''}>Tutte le categorie</option>
                        <c:forEach var="macro" items="${tutteLeMacro}">
                            <option value="${macro.idMacro}" ${selCategoria != 'All' && selCategoria == macro.idMacro ? 'selected' : ''}>
                                ${macro.nomeMacro}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="filter-group" id="filter-group-micro" style="${selCategoria == 'All' || empty selCategoria ? 'display:none;' : ''}">
                    <label for="microcategoria">Sotto-categoria:</label>
                    <select name="microcategoria" id="microcategoria">
                        <option value="All" ${empty selMicrocategoria || selMicrocategoria == 'All' ? 'selected' : ''}>Tutte le sotto-categorie</option>
                        <c:if test="${not empty microDiQuestaMacro}">
                            <c:forEach var="micro" items="${microDiQuestaMacro}">
                                <option value="${micro.idMicro}" ${selMicrocategoria != 'All' && selMicrocategoria == micro.idMicro ? 'selected' : ''}>
                                    ${micro.nomeMicro}
                                </option>
                            </c:forEach>
                        </c:if>
                    </select>
                </div>
                
                <div class="filter-group">
                    <label for="marca">Marca:</label>
                    <select name="marca" id="marca">
                        <option value="All" ${empty selMarca || selMarca == 'All' ? 'selected' : ''}>Tutte</option>
                        <option value="Yamaha" ${selMarca == 'Yamaha' ? 'selected' : ''}>Yamaha</option>
                        <option value="Roland" ${selMarca == 'Roland' ? 'selected' : ''}>Roland</option>
                        <option value="Fender" ${selMarca == 'Fender' ? 'selected' : ''}>Fender</option>
                    </select>
                </div>
                
                <div class="filter-group">
                    <label for="prezzo">Range di prezzo:</label>
                    <select name="prezzo" id="prezzo">
                        <option value="All" ${empty selPrezzo || selPrezzo == 'All' ? 'selected' : ''}>Tutti i prezzi</option>
                        <option value="0-500" ${selPrezzo == '0-500' ? 'selected' : ''}>0€ - 500€</option>
                        <option value="500-2000" ${selPrezzo == '500-2000' ? 'selected' : ''}>500€ - 2.000€</option>
                        <option value="2000-10000" ${selPrezzo == '2000-10000' ? 'selected' : ''}>2.000€ - 10.000€</option>
                    </select>
                </div>
                
                <button type="submit" class="btn-gold filter-btn">Applica filtri</button>
            </form>
        </aside>

        <div class="catalog-main">
            <form action="${pageContext.request.contextPath}/Admin/GestioneProdotti" method="GET" class="form-topbar-flex">
                <input type="hidden" name="categoria" value="${empty selCategoria ? 'All' : selCategoria}">
                <input type="hidden" name="microcategoria" value="${empty selMicrocategoria ? 'All' : selMicrocategoria}">
                <input type="hidden" name="marca" value="${empty selMarca ? 'All' : selMarca}">
                <input type="hidden" name="prezzo" value="${empty selPrezzo ? 'All' : selPrezzo}">
            
                <div class="catalog-topbar">
                    <div class="search-wrapper">
                        <input type="text" id="catalog-search" placeholder="Cerca prodotti nel pannello admin..." class="catalog-search" name="searchQuery" value="${searchQuery}" autocomplete="off">
                        <button type="submit" class="btn-lente-catalogo" aria-label="Cerca">
                            <img src="${pageContext.request.contextPath}/images/lente.svg" alt="Cerca" class="icona-lente">
                        </button>
                        <div id="search-suggestions" class="search-suggestions"></div>
                    </div>
                    
                    <div class="catalog-sort">
                        <label for="ordina">Ordina per:</label>
                        <select name="ordina" id="ordina" onchange="this.form.submit()">
                            <option value="rilevanza" ${selOrdina == 'rilevanza' ? 'selected' : ''}>Rilevanza</option>
                            <option value="prezzo_asc" ${selOrdina == 'prezzo_asc' ? 'selected' : ''}>Prezzo crescente</option>
                            <option value="prezzo_desc" ${selOrdina == 'prezzo_desc' ? 'selected' : ''}>Prezzo decrescente</option>
                        </select>
                    </div>
                </div>
            </form>

            <div class="catalog-grid">
                <c:choose>
                    <c:when test="${empty prodottiCatalogo}">
                        <p>Nessun prodotto trovato nel catalogo.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="prodotto" items="${prodottiCatalogo}">
                            <div class="product-card">
                                <div class="product-image">
                                    <img src="${pageContext.request.contextPath}/images/${prodotto.urlImmagine.replace('images/', '')}" alt="${prodotto.modello}">
                                </div>
                                
                                <div class="product-info">
                                    <h3>${prodotto.marca} ${prodotto.modello}</h3>
                                    <p class="product-brand">Categoria: ${prodotto.tipo}</p>
                                    
                                    <p class="product-brand">Stato Magazzino: 
                                        <c:choose>
                                            <c:when test="${prodotto.quantita == 0}">
                                                <span style="color: #dc3545; font-weight: bold; background: #f8d7da; padding: 2px 6px; border-radius: 4px; font-size: 13px;">
                                                    Non in commercio (Esaurito)
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <strong style="color: #28a745;">${prodotto.quantita} pz</strong>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    
                                    <p class="product-price">€ ${prodotto.prezzo}</p>
                                    
                                    <div class="product-actions">
                                        <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti?action=editForm&idProdotto=${prodotto.idProdotto}" class="btn-details">Modifica</a>
                                        <form action="${pageContext.request.contextPath}/Admin/GestioneProdotti" method="POST" class="form-delete-container" onsubmit="return confirm('Sei sicuro di voler eliminare permanentemente questo prodotto?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
                                            <button type="submit" class="btn-delete-filled">Elimina</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <script>const contextPath = '${pageContext.request.contextPath}';</script>
    <script src="${pageContext.request.contextPath}/js/catalogo-search.js"></script>
</section>

<jsp:include page="/jsp/footer.jsp" />