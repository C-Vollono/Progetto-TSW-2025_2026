package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.bean.OrdineBean;
import model.bean.UtenteBean;
import model.dao.OrdineDAO;
import model.dao.DettagliOrdineDAO;
import model.bean.DettaglioOrdineBean;

@WebServlet("/Admin/GestioneOrdini")
public class GestioneOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;
    private DettagliOrdineDAO dettagliOrdineDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
        this.dettagliOrdineDAO = new DettagliOrdineDAO(); // <-- AGGIUNTO
    }
    // IL METODO GET SI OCCUPA SOLO DI LETTURA E FILTRAGGIO (IDEMPOTENTE)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Verifica Sicurezza Admin
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato.");
            return;
        }

        String action = request.getParameter("action");

        // ==========================================
        // NUOVO SCENARIO: VISUALIZZAZIONE DETTAGLI
        // ==========================================
        if ("dettagli".equals(action)) {
            try {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                
                // 1. Recuperiamo i dati generali dell'ordine (Spedizione, Totale, Stato, ecc...)
                OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
                
                if (ordine != null) {
                    // 2. Recuperiamo la lista degli articoli associati a quell'ordine dal DettagliOrdineDAO
                    List<DettaglioOrdineBean> articoliOrdine = dettagliOrdineDAO.doRetrieveByOrdine(idOrdine);
                    
                    // 3. Passiamo tutto alla JSP tramite la request
                    request.setAttribute("ordine", ordine);
                    request.setAttribute("articoliOrdine", articoliOrdine);
                    
                    // 4. Inoltriamo alla pagina dei dettagli dell'ordine per l'admin
                    request.getRequestDispatcher("/jsp/admin/dettaglioOrdineAdmin.jsp").forward(request, response);
                    return; // Interrompiamo il metodo qui
                } else {
                    request.setAttribute("messaggioErrore", "Ordine non trovato nel sistema.");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("messaggioErrore", "Identificativo ordine non valido.");
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("messaggioErrore", "Errore nel recupero dei dettagli dal database.");
            }
        }

        // =========================================================================
        // IL TUO CODICE PREESISTENTE PER IL FILTRAGGIO GLOBALE (Resta invariato)
        // =========================================================================
        String dataInizio = request.getParameter("dataInizio");
        String dataFine = request.getParameter("dataFine");
        String filtroCliente = request.getParameter("filtroCliente");
        
        List<OrdineBean> listaOrdini = new ArrayList<>();
        boolean filtroAttivo = false;
        
        try {
            boolean haDate = (dataInizio != null && !dataInizio.trim().isEmpty() && dataFine != null && !dataFine.trim().isEmpty());
            boolean haCliente = (filtroCliente != null && !filtroCliente.trim().isEmpty());

            if (haCliente && haDate) {
                try {
                    int idCliente = Integer.parseInt(filtroCliente.trim());
                    listaOrdini = ordineDAO.doRetrieveByUtenteAndDates(idCliente, dataInizio, dataFine);
                    request.setAttribute("clienteSelezionato", filtroCliente);
                    request.setAttribute("dataInizioSelezionata", dataInizio);
                    request.setAttribute("dataFineSelezionata", dataFine);
                    filtroAttivo = true;
                } catch (NumberFormatException e) {
                    request.setAttribute("messaggioErrore", "L'ID Cliente deve essere un numero intero.");
                    listaOrdini = ordineDAO.doRetrieveAll();
                }
            }
            else if (haCliente) {
                try {
                    int idCliente = Integer.parseInt(filtroCliente.trim());
                    listaOrdini = ordineDAO.doRetrieveByClienteAdmin(idCliente);
                    request.setAttribute("clienteSelezionato", filtroCliente);
                    filtroAttivo = true;
                } catch (NumberFormatException e) {
                    request.setAttribute("messaggioErrore", "L'ID Cliente deve essere un numero intero.");
                    listaOrdini = ordineDAO.doRetrieveAll();
                }
            }
            else if (haDate) {
                listaOrdini = ordineDAO.doRetrieveByDates(dataInizio, dataFine);
                request.setAttribute("dataInizioSelezionata", dataInizio);
                request.setAttribute("dataFineSelezionata", dataFine);
                filtroAttivo = true;
            }
            else {
                listaOrdini = ordineDAO.doRetrieveAll();
            }

            request.setAttribute("filtroAttivo", filtroAttivo);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Errore nel caricamento dei dati o nel filtraggio del database.");
        }
        
        request.setAttribute("listaOrdiniTotali", listaOrdini);
        request.getRequestDispatcher("/jsp/admin/ordiniAdmin.jsp").forward(request, response);
    }
    
    // IL METODO POST GESTISCE LE MODIFICHE AL DATABASE (CAMBIO STATO ORDINE)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verifica Sicurezza Admin anche sul POST
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato.");
            return;
        }

        String action = request.getParameter("action");

        if ("cambiaStato".equals(action)) {
            try {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                String nuovoStato = request.getParameter("stato");
                
                ordineDAO.doUpdateStato(idOrdine, nuovoStato);
                
                // Salviamo il feedback in sessione perché stiamo per fare un redirect (PRG)
                session.setAttribute("messaggioSuccesso", "Stato dell'ordine #" + idOrdine + " modificato in " + nuovoStato + "!");
                
            } catch (NumberFormatException e) {
                e.printStackTrace();
                session.setAttribute("messaggioErrore", "Identificativo ordine non valido.");
            } catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("messaggioErrore", "Errore di sistema durante l'aggiornamento dello stato.");
            }
        }

        // REDIRECT (PRG) alla GET della stessa servlet per rinfrescare la lista in totale sicurezza
        response.sendRedirect(request.getContextPath() + "/Admin/GestioneOrdini");
    }
}