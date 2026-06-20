package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Carrello;
import model.bean.ProdottoBean;
import model.bean.UtenteBean;
import util.ConPool;

@WebServlet("/ConfermaOrdine")
public class ConfermaOrdineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession(false);
            
        // CORRETTO: Cerca "utenteLoggato" per allinearsi alla LoginServlet
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null) {
            if (session == null) {
                session = request.getSession(true);
            }
            request.setAttribute("erroreLogin", "Devi effettuare l'accesso per poter completare l'acquisto!");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        // 2. Verifica stato del carrello
        Carrello carrello = (session != null) ? (Carrello) session.getAttribute("carrello") : null;
        if (carrello == null || carrello.getElementi().isEmpty()) {
            request.setAttribute("messaggioErrore", "Il tuo carrello è vuoto. Impossibile procedere.");
            request.getRequestDispatcher("/jsp/carrello.jsp").forward(request, response);
            return;
        }

        // 3. Recupero parametri form Spedizione/Pagamento con Fallback Intelligente dal DB o Mock
        String nomeCognome = request.getParameter("spedizioneNomeCognome");
        if (nomeCognome == null || nomeCognome.trim().isEmpty()) {
            nomeCognome = utente.getNome() + " " + utente.getCognome();
        }

        String via = "Via dei Musicisti";
        String civico = "45";
        String cap = "84100";
        String citta = "Salerno";
        String provincia = "SA";
        String telefono = "3331234567";
        String circuito = "Visa";
        String cartaOscurata = "************1234";

        try (Connection conDati = ConPool.getConnection()) {
            String sqlSped = "SELECT * FROM Dati_spedizione WHERE ID_Utente = ? LIMIT 1";
            try (PreparedStatement psSped = conDati.prepareStatement(sqlSped)) {
                psSped.setInt(1, utente.getIdUtente());
                try (ResultSet rsSped = psSped.executeQuery()) {
                    if (rsSped.next()) {
                        via = rsSped.getString("Via");
                        civico = rsSped.getString("Numero_civico");
                        cap = rsSped.getString("Cap");
                        citta = rsSped.getString("Citta");
                        provincia = rsSped.getString("Provincia");
                        telefono = rsSped.getString("Telefono");
                    }
                }
            }
            
            String sqlPag = "SELECT * FROM Dati_pagamento WHERE ID_Utente = ? LIMIT 1";
            try (PreparedStatement psPag = conDati.prepareStatement(sqlPag)) {
                psPag.setInt(1, utente.getIdUtente());
                try (ResultSet rsPag = psPag.executeQuery()) {
                    if (rsPag.next()) {
                        circuito = rsPag.getString("Circuito_carta");
                        cartaOscurata = rsPag.getString("Numero_carta_oscurato");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("[ConfermaOrdineServlet] Dati di default non trovati, uso i mock strutturati.");
        }

        if (request.getParameter("spedizioneVia") != null && !request.getParameter("spedizioneVia").trim().isEmpty()) 
            via = request.getParameter("spedizioneVia");
        if (request.getParameter("spedizioneNumeroCivico") != null && !request.getParameter("spedizioneNumeroCivico").trim().isEmpty()) 
            civico = request.getParameter("spedizioneNumeroCivico");
        if (request.getParameter("spedizioneCap") != null && !request.getParameter("spedizioneCap").trim().isEmpty()) 
            cap = request.getParameter("spedizioneCap");
        if (request.getParameter("spedizioneCitta") != null && !request.getParameter("spedizioneCitta").trim().isEmpty()) 
            citta = request.getParameter("spedizioneCitta");
        if (request.getParameter("spedizioneProvincia") != null && !request.getParameter("spedizioneProvincia").trim().isEmpty()) 
            provincia = request.getParameter("spedizioneProvincia");
        if (request.getParameter("spedizioneTelefono") != null && !request.getParameter("spedizioneTelefono").trim().isEmpty()) 
            telefono = request.getParameter("spedizioneTelefono");
        if (request.getParameter("pagamentoCircuito") != null && !request.getParameter("pagamentoCircuito").trim().isEmpty()) 
            circuito = request.getParameter("pagamentoCircuito");
        if (request.getParameter("pagamentoNumeroCartaOscurato") != null && !request.getParameter("pagamentoNumeroCartaOscurato").trim().isEmpty()) 
            cartaOscurata = request.getParameter("pagamentoNumeroCartaOscurato");

        // 4. APERTURA DELLA TRANSAZIONE ATOMICA (ACID)
        Connection con = null;
        try {
            con = ConPool.getConnection();
            con.setAutoCommit(false); 

            String sqlOrdine = "INSERT INTO Ordine (ID_Utente, Totale_ordine, Stato_ordine, Spedizione_Nome_Cognome, "
                           + "Spedizione_Via, Spedizione_Numero_civico, Spedizione_Cap, Spedizione_Citta, "
                           + "Spedizione_Provincia, Spedizione_Telefono, Pagamento_Circuito, Pagamento_Numero_Carta_Oscurato) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            int idOrdineGenerato = -1;
            try (PreparedStatement psO = con.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS)) {
                psO.setInt(1, utente.getIdUtente());
                psO.setDouble(2, carrello.getPrezzoTotale());
                psO.setString(3, "IN_ATTESA");
                psO.setString(4, nomeCognome);
                psO.setString(5, via);
                psO.setString(6, civico);
                psO.setString(7, cap);
                psO.setString(8, citta);
                psO.setString(9, provincia);
                psO.setString(10, telefono);
                psO.setString(11, circuito);
                psO.setString(12, cartaOscurata);
                psO.executeUpdate();
                
                try (ResultSet rs = psO.getGeneratedKeys()) {
                    if (rs.next()) {
                        idOrdineGenerato = rs.getInt(1);
                    }
                }
            }

            if (idOrdineGenerato == -1) {
                throw new SQLException("Errore critico: Impossibile generare la chiave primaria per l'Ordine.");
            }

            String sqlDettaglio = "INSERT INTO Dettaglio_Ordine (ID_ordine, ID_prodotto, Quantita, Prezzo_unitario_storico) VALUES (?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE Prodotto SET Quantita = Quantita - ? WHERE ID_prodotto = ? AND Quantita >= ?";

            try (PreparedStatement psD = con.prepareStatement(sqlDettaglio);
                 PreparedStatement psU = con.prepareStatement(sqlUpdateStock)) {
                
                for (Map.Entry<ProdottoBean, Integer> entry : carrello.getElementi().entrySet()) {
                    ProdottoBean prodotto = entry.getKey();
                    int qtaRichiesta = entry.getValue();

                    psD.setInt(1, idOrdineGenerato);
                    psD.setInt(2, prodotto.getIdProdotto());
                    psD.setInt(3, qtaRichiesta);
                    psD.setDouble(4, prodotto.getPrezzo());
                    psD.executeUpdate();

                    psU.setInt(1, qtaRichiesta);
                    psU.setInt(2, prodotto.getIdProdotto());
                    psU.setInt(3, qtaRichiesta); 
                    
                    int rowsAffected = psU.executeUpdate();
                    if (rowsAffected == 0) {
                        con.rollback(); 
                        
                        // CORREZIONE PRG: Messaggio in sessione e redirect al carrello
                        session.setAttribute("messaggioErrore", "Errore: Lo strumento '" + prodotto.getMarca() + " " + prodotto.getModello() + "' è esaurito o non disponibile nella quantità richiesta!");
                        response.sendRedirect(request.getContextPath() + "/Carrello");
                        return;
                    }
                }
            }

            con.commit();

            // 5. SVUOTAMENTO DEL CARRELLO DALLA SESSIONE UTENTE
            session.removeAttribute("carrello");

            // Successo con PRG completo
            session.setAttribute("messaggioSuccesso", "Complimenti! Il tuo ordine è stato registrato con successo. ID Ordine: #" + idOrdineGenerato);
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            
            // CORREZIONE PRG: Messaggio in sessione e redirect al carrello per evitare duplicazioni al refresh
            if (session != null) {
                session.setAttribute("messaggioErrore", "Si è verificato un errore imprevisto nel server durante il salvataggio dei dati dell'ordine.");
            }
            response.sendRedirect(request.getContextPath() + "/Carrello");
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Catalogo");
    }
}