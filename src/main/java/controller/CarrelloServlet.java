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
        
        HttpSession session = request.getSession(true); // Recupera o crea la sessione utente
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");
        
        try {
            if (azione != null) {
                if (azione.equalsIgnoreCase("aggiungi")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    int quantitaRichiesta = Integer.parseInt(request.getParameter("quantita"));
                    
                    // Recuperiamo le informazioni fresche dal DB prima di inserire nel carrello
                    ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);
                    
                    // MODIFICATO: Usiamo getQuantita() che mappa lo stock del tuo fagiolino
                    if (prodotto != null && prodotto.getQuantita() > 0) {
                        carrello.aggiungiProdotto(prodotto, quantitaRichiesta);
                    }
                    
                } else if (azione.equalsIgnoreCase("modifica")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    int nuovaQuantita = Integer.parseInt(request.getParameter("quantita"));
                    
                    carrello.modificaQuantita(idProdotto, nuovaQuantita);
                    
                } else if (azione.equalsIgnoreCase("rimuovi")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    
                    carrello.rimuoviProdotto(idProdotto);
                }
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

        // Reindirizzamento verso la vista del carrello gestita dal tuo collega
        request.getRequestDispatcher("/jsp/carrello.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}