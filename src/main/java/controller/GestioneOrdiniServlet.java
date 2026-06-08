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

@WebServlet("/Admin/GestioneOrdini")
public class GestioneOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
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

        String dataInizio = request.getParameter("dataInizio");
        String dataFine = request.getParameter("dataFine");
        String filtroCliente = request.getParameter("filtroCliente");
        
        List<OrdineBean> listaOrdini = new ArrayList<>();
        boolean filtroAttivo = false;
        
        try {
            boolean haDate = (dataInizio != null && !dataInizio.trim().isEmpty() && dataFine != null && !dataFine.trim().isEmpty());
            boolean haCliente = (filtroCliente != null && !filtroCliente.trim().isEmpty());

            // SCENARIO 1: Filtro Combinato (ID Cliente + Intervallo Date)
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
            // SCENARIO 2: Solo Filtro ID Cliente
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
            // SCENARIO 3: Solo Filtro Intervallo Date
            else if (haDate) {
                listaOrdini = ordineDAO.doRetrieveByDates(dataInizio, dataFine);
                request.setAttribute("dataInizioSelezionata", dataInizio);
                request.setAttribute("dataFineSelezionata", dataFine);
                filtroAttivo = true;
            }
            // SCENARIO 4: Nessun filtro attivo (Caricamento globale)
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