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
            request.setAttribute("erroreLogin", "Devi effettuare l'accesso per visualizzare il tuo storico ordini.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        try {
            // 2. Recupero degli ordini dal DB usando il tuo metodo del DAO
            List<OrdineBean> ordini = ordineDAO.doRetrieveByUtente(utente.getIdUtente());
            
            // 3. Passiamo la lista alla JSP tramite l'oggetto request
            request.setAttribute("listaOrdini", ordini);
            
            // 4. INOLTRO ALLA PAGINA DENTRO LA CARTELLA PROTETTA 'COMMON'
            request.getRequestDispatcher("/jsp/common/storicoOrdini.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Impossibile recuperare lo storico degli ordini in questo momento.");
            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
