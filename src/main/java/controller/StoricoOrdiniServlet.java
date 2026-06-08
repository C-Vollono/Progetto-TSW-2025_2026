package controller;

import java.io.IOException;
import java.sql.SQLException;
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

@WebServlet("/StoricoOrdini")
public class StoricoOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // 1. Controllo sicurezza: l'utente deve essere loggato
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null) {
            // Usiamo la chiave centralizzata per mostrare l'errore nella pagina di login
            request.setAttribute("messaggioErrore", "Devi effettuare l'accesso per visualizzare il tuo storico ordini.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // Recupero i parametri del filtro date
        String dataInizio = request.getParameter("dataInizio");
        String dataFine = request.getParameter("dataFine");
        List<OrdineBean> ordini;

        try {
            // 2. GESTIONE FILTRO DATE O CARICAMENTO COMPLESSIVO
            if (dataInizio != null && !dataInizio.trim().isEmpty() && dataFine != null && !dataFine.trim().isEmpty()) {
                ordini = ordineDAO.doRetrieveByUtenteAndDates(utente.getIdUtente(), dataInizio, dataFine);
                
                // Rimandiamo le date alla JSP per tenerle scritte nei campi input del form
                request.setAttribute("dataInizioSelezionata", dataInizio);
                request.setAttribute("dataFineSelezionata", dataFine);
                request.setAttribute("filtroAttivo", true);
            } else {
                // Recupero classico di tutti gli ordini se non c'è il filtro
                ordini = ordineDAO.doRetrieveByUtente(utente.getIdUtente());
            }
            
            // 3. Passiamo la lista alla JSP tramite l'oggetto request
            request.setAttribute("listaOrdini", ordini);
            
            // 4. INOLTRO ALLA PAGINA DENTRO LA CARTELLA PROTETTA 'COMMON'
            request.getRequestDispatcher("/jsp/common/storicoOrdini.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // CORREZIONE DI SICUREZZA: in caso di errore DB grave, salviamo in sessione e facciamo redirect 
            // per evitare che il refresh della pagina mantenga l'URL bloccato su /StoricoOrdini
            if (session != null) {
                session.setAttribute("messaggioErrore", "Impossibile recuperare lo storico degli ordini in questo momento a causa di un errore tecnico.");
            }
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}