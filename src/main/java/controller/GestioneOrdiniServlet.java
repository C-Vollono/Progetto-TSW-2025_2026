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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verifica Sicurezza Admin - usa isIsAdmin() generato dal tuo Eclipse
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato.");
            return;
        }

        String action = request.getParameter("action");
        
        try {
            if ("cambiaStato".equals(action)) {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                String nuovoStato = request.getParameter("stato");
                ordineDAO.doUpdateStato(idOrdine, nuovoStato);
                request.setAttribute("messaggioSuccesso", "Stato dell'ordine #" + idOrdine + " modificato in " + nuovoStato + "!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Impossibile aggiornare lo stato dell'ordine.");
        }
        
        // Carica la lista e inoltra alla vista
        mostraRegistroOrdini(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void mostraRegistroOrdini(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<OrdineBean> totali = new ArrayList<>();
        try {
            totali = ordineDAO.doRetrieveAll();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Errore nel caricamento dei dati dal database.");
        }
        request.setAttribute("listaOrdiniTotali", totali);
        request.getRequestDispatcher("/jsp/admin/ordiniAdmin.jsp").forward(request, response);
    }
}