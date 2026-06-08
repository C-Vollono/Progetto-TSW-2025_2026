<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/jsp/header.jsp" />

<section class="admin-section">
	<div class="admin-header">
		<h2>Pannello Admin</h2>
		<p>Gestione e-commerce ToneMarket.</p>
	</div>
	
	<div class="admin-container">
	
		<div class="admin-card form-container">
			<h3>Aggiungi Nuovo Prodotto</h3>
			<form action="${pageContext.request.contextPath}/AggiungiProdottoServlet" method="POST">
			
				<div class="input-group">
					<label for="nomeProdotto">Nome Strumento</label>
					<input type="text" id="nomeProdotto" name="nome" placeholder="Es. Fender Stratocaster" required>
				</div>
				
				<div class="input-group">
					<label for="prezzo">Prezzo (€)</label>
					<input type="number" id="prezzo" name="prezzo" step="0.01" min="0" placeholder="0.00" required>
				</div>
				
				<div class="input-group">
					<label for="categoria">Categoria</label>
					<select id="categoria" name="categoria" class="admin-select" required>
						<option value="">Seleziona...</option>
						<option value="Chitarre">Chitarre</option>
						<option value="Pianoforti">Pianoforti</option>
						<option value="Batterie">Batterie e Percussioni</option>
						<option value="Accessori">Accessori</option>
					</select>
				</div>
				
				<div class="input-group">
					<label for="descrizione">Descrizione</label>
					<textarea id="descrizione" name="descrizione" rows="4" class="admin-textarea" placeholder="Inserisci descrizione..." required></textarea>
				</div>
				
				<button type="submit" class="btn-gold btn-admin-submit">Salva Prodotto</button>
			</form>
		</div>
		
		<div class="admin-card table-container">
			<h3>Catalogo attuale</h3>
			
			<div class="table-responsive">
				<table class="admin-table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Nome</th>
							<th>Prezzo</th>
							<th>Categoria</th>
							<th>Azioni</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td>Chitarra acustica</td>
							<td>€ 249,00</td>
                            <td>Chitarre</td>
                            <td class="action-cells">
                                <form action="${pageContext.request.contextPath}/ModificaProdotto" method="GET" class="inline-form">
                                    <input type="hidden" name="id" value="1">
                                    <button type="submit" class="btn-edit">Modifica</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/EliminaProdotto" method="POST" class="inline-form">
                                    <input type="hidden" name="id" value="1">
                                    <button type="submit" class="btn-delete" onclick="return confirm('Sei sicuro di voler eliminare questo prodotto?');">Elimina</button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>Pianoforte a Coda Steinway</td>
                            <td>€ 15.500,00</td>
                            <td>Pianoforti</td>
                            <td class="action-cells">
                                <form action="${pageContext.request.contextPath}/ModificaProdotto" method="GET" class="inline-form">
                                    <input type="hidden" name="id" value="2">
                                    <button type="submit" class="btn-edit">Modifica</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/EliminaProdotto" method="POST" class="inline-form">
                                    <input type="hidden" name="id" value="2">
                                    <button type="submit" class="btn-delete" onclick="return confirm('Sei sicuro di voler eliminare questo prodotto?');">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</section>

<jsp:include page="/jsp/footer.jsp" />