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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // 1. CONTROLLO LOGIN
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        
        if (utente == null) {
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("messaggioErrore", "Devi effettuare il login per procedere al checkout.");
            session.setAttribute("redirectDopoLogin", "/Checkout");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        // 2. CONTROLLO CARRELLO
        model.Carrello carrello = (session != null) ? (model.Carrello) session.getAttribute("carrello") : null;
        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Carrello");
            return;
        }

        try {
            // 3. RECUPERO DATI E INOLTRO ALLA PAGINA COMMON
            List<DatiSpedizioneBean> listaSpedizioni = spedizioneDAO.doRetrieveByUtente(utente.getIdUtente()); 
            List<DatiPagamentoBean> listaPagamenti = pagamentoDAO.doRetrieveByUtente(utente.getIdUtente()); 

            request.setAttribute("listaSpedizioni", listaSpedizioni);
            request.setAttribute("listaPagamenti", listaPagamenti);

            request.getRequestDispatcher("/jsp/common/checkout.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Errore nel recupero dei dati utente per il checkout", e);
        }
    }
}