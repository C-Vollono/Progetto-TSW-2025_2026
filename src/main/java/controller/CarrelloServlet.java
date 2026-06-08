package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Carrello;
import model.bean.ProdottoBean;
import model.dao.ProdottoDAO;

@WebServlet("/Carrello")
public class CarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");
        
        try {
            // Se l'azione è presente, elaboriamo la modifica dello stato del carrello
            if (azione != null) {
                if (azione.equalsIgnoreCase("aggiungi")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    int quantitaRichiesta = Integer.parseInt(request.getParameter("quantita"));
                    
                    ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);
                    
                    if (prodotto != null && prodotto.getQuantita() > 0) {
                        carrello.aggiungiProdotto(prodotto, quantitaRichiesta);
                        session.setAttribute("messaggioSuccesso", "Prodotto aggiunto al carrello con successo!");
                    } else {
                        session.setAttribute("messaggioErrore", "Spiacenti, il prodotto selezionato è esaurito!");
                    }
                    
                } else if (azione.equalsIgnoreCase("modifica")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    int nuovaQuantita = Integer.parseInt(request.getParameter("quantita"));
                    
                    carrello.modificaQuantita(idProdotto, nuovaQuantita);
                    session.setAttribute("messaggioSuccesso", "Quantità aggiornata!");
                    
                } else if (azione.equalsIgnoreCase("rimuovi")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    
                    carrello.rimuoviProdotto(idProdotto);
                    session.setAttribute("messaggioSuccesso", "Prodotto rimosso dal carrello.");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Se scatta l'eccezione qui dentro, l'azione era necessariamente non null, quindi salviamo sempre in sessione per il redirect
            session.setAttribute("messaggioErrore", "Formato dei parametri non valido.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore di comunicazione con il database.");
        }

        // --- PATTERN PRG PERFETTO ---
        if (azione != null) {
            // C'è stata un'azione (andata a buon fine o finita nel catch): facciamo redirect per pulire l'URL
            response.sendRedirect(request.getContextPath() + "/Carrello");
        } else {
            // Nessuna azione: inoltro diretto alla pagina JSP per la visualizzazione pura
            request.getRequestDispatcher("/jsp/carrello.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}