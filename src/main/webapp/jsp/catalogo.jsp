<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/catalogo.css">

<jsp:include page="/jsp/header.jsp" />

<main class="main-catalogo">
<section class="catalog-section">
	<div class="catalog-header">
		<h2>Il Nostro Catalogo <span>Premium</span></h2>
		<p> Esplora il mondo della musica Premium, prodotti di altissima qualità.</p>
	</div>
	
	<c:if test="${not empty sessionScope.messaggioErrore}">
    <div class="error-msg server-error catalog-error-container">
        ${sessionScope.messaggioErrore}
    </div>
    <c:remove var="messaggioErrore" scope="session" />
</c:if>
	
	<div class="catalog-layout">
		
		<aside class="catalog-sidebar">
			<h3>Filtri</h3>
			<form action="${pageContext.request.contextPath}/Catalogo" method="GET">
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
		
			<form action="${pageContext.request.contextPath}/Catalogo" method="GET" class="form-topbar-flex">
				<input type="hidden" name="categoria" value="${empty selCategoria ? 'All' : selCategoria}">
				<input type="hidden" name="microcategoria" value="${empty selMicrocategoria ? 'All' : selMicrocategoria}">
				<input type="hidden" name="marca" value="${empty selMarca ? 'All' : selMarca}">
				<input type="hidden" name="prezzo" value="${empty selPrezzo ? 'All' : selPrezzo}">
			
				<div class="catalog-topbar">
					<div class="search-wrapper">
						<input type="text" id="catalog-search" placeholder="Cerca prodotti..." class="catalog-search" name="searchQuery" value="${searchQuery}" autocomplete="off">
						
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
						<div class="empty-catalog-msg">
							<p>Nessun prodotto corrisponde ai criteri di ricerca.</p>
						</div>
					</c:when>
					
					<c:otherwise>
						<c:forEach var="prodotto" items="${prodottiCatalogo}">
							<div class="product-card">
								<div class="product-image">
								<a href="${pageContext.request.contextPath}/Catalogo?id=${prodotto.idProdotto}" class="product-image-link">
									<img src="${pageContext.request.contextPath}/images/${prodotto.urlImmagine}" alt="${prodotto.modello}">
								</a>
								</div>
								
								<div class="product-info">
									<a href="${pageContext.request.contextPath}/Catalogo?id=${prodotto.idProdotto}" class="product-image-link">
									<h3>${prodotto.marca} ${prodotto.modello}</h3>
									</a>
									<p class="product-brand">Categoria: ${prodotto.tipo}</p>
									<p class="product-price">€ ${prodotto.prezzo}</p>
									<div class="product-rating">
										<c:forEach begin="1" end="5" var="i">
											<c:choose>
												<%-- Se il contatore è minore o uguale alla valutazione del prodotto, stella piena d'oro --%>
												<c:when test="${not empty prodotto.valutazione && i <= prodotto.valutazione}">
													<span class="star filled">★</span>
												</c:when>
												<%-- Altrimenti, stella vuota grigia --%>
												<c:otherwise>
													<span class="star empty">☆</span>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									<span class="rating-number">
										${not empty prodotto.valutazione ? prodotto.valutazione : '0'}/5
									</span>
								</div>
									
								<div class="product-actions">
									<a href="${pageContext.request.contextPath}/Catalogo?id=${prodotto.idProdotto}" class="btn-details">Dettagli</a>
										<form action="${pageContext.request.contextPath}/Carrello" method="POST" class="form-add-cart-flex">
											<input type="hidden" name="azione" value="aggiungi">
											<input type="hidden" name="quantita" value="1">
											<input type="hidden" name="idProdotto" value="${prodotto.idProdotto}">
											<button type="submit" class="btn-gold btn-add-cart">
												<img src="${pageContext.request.contextPath}/images/cart.svg" alt="Carrello" class="btn-cart-icon">
											</button>
										</form>
								</div>
								</div>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>

			<!--<c:if test="${not empty prodottiCatalogo}">
				<div class="load-more-container">
					<button class="btn-details load-more-btn">Vedi altro</button>
				</div>
			</c:if>-->
		</div>
	</div>
	
	<script>const contextPath = '${pageContext.request.contextPath}';</script>
	<script defer src="${pageContext.request.contextPath}/js/catalogo-search.js"></script>
</section>

<jsp:include page="/jsp/footer.jsp" />