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

import model.bean.UtenteBean;
import model.bean.DatiSpedizioneBean;
import model.bean.DatiPagamentoBean;
import model.dao.DatiSpedizioneDAO;
import model.dao.DatiPagamentoDAO;

@WebServlet("/Checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DatiSpedizioneDAO spedizioneDAO;
    private DatiPagamentoDAO pagamentoDAO;

    @Override
    public void init() throws ServletException {
        spedizioneDAO = new DatiSpedizioneDAO();
        pagamentoDAO = new DatiPagamentoDAO();
    }

    // Se provano ad accedere direttamente digitando l'URL, li rimandiamo al carrello
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Carrello");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // CORRETTO: Cerca "utenteLoggato" per allinearsi alla LoginServlet
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        
        if (utente == null) {
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("messaggioErrore", "Devi effettuare il login per procedere al checkout.");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        try {
            // Recuperiamo i dati salvati nel DB per l'utente corrente usando i tuoi DAO
            List<DatiSpedizioneBean> listaSpedizioni = spedizioneDAO.doRetrieveByUtente(utente.getIdUtente()); //
            List<DatiPagamentoBean> listaPagamenti = pagamentoDAO.doRetrieveByUtente(utente.getIdUtente()); //

            // Passiamo le liste come attributi della richiesta alla pagina di checkout
            request.setAttribute("listaSpedizioni", listaSpedizioni);
            request.setAttribute("listaPagamenti", listaPagamenti);

            // Inoltriamo alla pagina di riepilogo dati
            request.getRequestDispatcher("/jsp/checkout.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Errore nel recupero dei dati utente per il checkout", e);
        }
    }
}