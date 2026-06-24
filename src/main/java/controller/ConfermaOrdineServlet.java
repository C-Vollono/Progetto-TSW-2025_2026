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
import model.bean.DatiSpedizioneBean;
import model.bean.DatiPagamentoBean;
import model.dao.DatiSpedizioneDAO;
import model.dao.DatiPagamentoDAO;
import util.ConPool;

@WebServlet("/ConfermaOrdine")
public class ConfermaOrdineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
            
        HttpSession session = request.getSession(false);
            
        // 1. VERIFICA UTENTE LOGGATO
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null) {
            if (session == null) {
                session = request.getSession(true);
            }
            request.setAttribute("erroreLogin", "Devi effettuare l'accesso per poter completare l'acquisto!");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        // 2. VERIFICA STATO DEL CARRELLO
        Carrello carrello = (session != null) ? (Carrello) session.getAttribute("carrello") : null;
        if (carrello == null || carrello.getElementi().isEmpty()) {
            session.setAttribute("messaggioErrore", "Il tuo carrello è vuoto. Impossibile procedere.");
            response.sendRedirect(request.getContextPath() + "/Carrello");
            return;
        }

        // 3. RECUPERO DATI DAL FORM DEL CHECKOUT
        String nomeCognome = utente.getNome() + " " + utente.getCognome();
        String via = "", civico = "", cap = "", citta = "", provincia = "", telefono = "";
        String circuito = "", cartaOscurata = "";

        try {
            String idSpedizione = request.getParameter("idSpedizione");
            DatiSpedizioneDAO spedizioneDAO = new DatiSpedizioneDAO();
            
            if ("nuovo".equals(idSpedizione)) {
                via = request.getParameter("nuovaVia").trim();
                civico = request.getParameter("nuovoCivico").trim();
                citta = request.getParameter("nuovaCitta").trim();
                provincia = request.getParameter("nuovaProvincia").trim().toUpperCase();
                cap = request.getParameter("nuovoCap").trim();
                telefono = request.getParameter("nuovoTelefono") != null ? request.getParameter("nuovoTelefono").trim() : "";
                
                DatiSpedizioneBean nuovoInd = new DatiSpedizioneBean();
                nuovoInd.setIdUtente(utente.getIdUtente());
                nuovoInd.setVia(via);
                nuovoInd.setNumeroCivico(civico);
                nuovoInd.setCitta(citta);
                nuovoInd.setProvincia(provincia);
                nuovoInd.setCap(cap);
                nuovoInd.setTelefono(telefono);
                spedizioneDAO.doSave(nuovoInd);
            } else {
                // Recupera i dati dell'indirizzo selezionato dal DB
                DatiSpedizioneBean indEsistente = spedizioneDAO.doRetrieveByKey(Integer.parseInt(idSpedizione));
                via = indEsistente.getVia();
                civico = indEsistente.getNumeroCivico();
                citta = indEsistente.getCitta();
                provincia = indEsistente.getProvincia();
                cap = indEsistente.getCap();
                telefono = indEsistente.getTelefono() != null ? indEsistente.getTelefono() : "";
            }

            String idPagamento = request.getParameter("idPagamento");
            DatiPagamentoDAO pagamentoDAO = new DatiPagamentoDAO();

            if ("nuovo".equals(idPagamento)) {
                circuito = request.getParameter("nuovoCircuito");
                String numeroCarta = request.getParameter("nuovoNumeroCarta").replaceAll("\\s+", "");
                cartaOscurata = "**" + numeroCarta.substring(numeroCarta.length() - 4);
                
                DatiPagamentoBean nuovaCarta = new DatiPagamentoBean();
                nuovaCarta.setIdUtente(utente.getIdUtente());
                nuovaCarta.setCircuitoCarta(circuito);
                nuovaCarta.setNumeroCartaOscurato(cartaOscurata);
                nuovaCarta.setIntestatario(request.getParameter("nuovoIntestatario").trim());
                nuovaCarta.setScadenzaCarta(java.sql.Date.valueOf(request.getParameter("nuovaScadenza") + "-01"));
                pagamentoDAO.doSave(nuovaCarta);
                
            } else {
            	
                DatiPagamentoBean cartaEsistente = pagamentoDAO.doRetrieveByKey(Integer.parseInt(idPagamento));
                circuito = cartaEsistente.getCircuitoCarta();
                cartaOscurata = cartaEsistente.getNumeroCartaOscurato();
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore nel recupero dei dati di fatturazione.");
            response.sendRedirect(request.getContextPath() + "/Checkout");
            return;
        }

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
                psO.setString(3, "In elaborazione"); // Stato iniziale più professionale
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

            String sqlDettaglio = "INSERT INTO Dettaglio_Ordine (ID_ordine, ID_prodotto, Quantita, Prezzo_unitario_storico, Iva_storicizzata) VALUES (?, ?, ?, ?, ?)";
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
                    psD.setInt(5, 22); 
                    psD.executeUpdate();

                    psU.setInt(1, qtaRichiesta);
                    psU.setInt(2, prodotto.getIdProdotto());
                    psU.setInt(3, qtaRichiesta); 
                    
                    int rowsAffected = psU.executeUpdate();
                    if (rowsAffected == 0) {
                        con.rollback();
                        
                        session.setAttribute("messaggioErrore", "Siamo spiacenti, lo strumento '" + prodotto.getMarca() + " " + prodotto.getModello() + "' è appena andato esaurito. Aggiorna il carrello.");
                        response.sendRedirect(request.getContextPath() + "/Carrello");
                        return;
                    }
                }
            }

            con.commit();
            
            session.setAttribute("ricevuta_idOrdine", idOrdineGenerato);
            session.setAttribute("ricevuta_totale", carrello.getPrezzoTotale());
            session.setAttribute("ricevuta_nome", nomeCognome);
            session.setAttribute("ricevuta_indirizzo", via + ", " + civico + " - " + cap + " " + citta + " (" + provincia + ")");
            session.setAttribute("ricevuta_metodo", circuito + " " + cartaOscurata);
            session.setAttribute("ricevuta_elementi", new java.util.HashMap<>(carrello.getElementi())); // Copia gli elementi

            // 5. SVUOTAMENTO CARRELLO E REDIRECT (Pattern PRG)
            session.removeAttribute("carrello");
            session.setAttribute("messaggioSuccesso", "Complimenti! Il tuo ordine è stato registrato con successo. ID Ordine: #" + idOrdineGenerato);
            
            response.sendRedirect(request.getContextPath() + "/jsp/common/confermaOrdine.jsp");

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            
            if (session != null) {
                session.setAttribute("messaggioErrore", "Si è verificato un errore imprevisto durante la creazione dell'ordine. Riprova.");
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