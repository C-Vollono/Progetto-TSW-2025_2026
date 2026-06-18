package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.bean.RecensioneBean;
import model.bean.UtenteBean;
import model.dao.RecensioneDAO;

@WebServlet("/Recensione")
public class RecensioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init() throws ServletException {
        this.recensioneDAO = new RecensioneDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("utenteLoggato");

        if (utente == null) {
            session.setAttribute("messaggioErrore", "Devi effettuare l'accesso per recensire.");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        try {
            String azione = request.getParameter("azione");

            if ("inserisci".equals(azione)) {
                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                int valutazione = Integer.parseInt(request.getParameter("valutazione"));
                String titolo = request.getParameter("titolo");
                String corpo = request.getParameter("corpo");

                RecensioneBean recensione = new RecensioneBean();
                recensione.setIdUtente(utente.getIdUtente());
                recensione.setIdProdotto(idProdotto);
                recensione.setValutazione(valutazione);
                recensione.setTitolo(titolo);
                recensione.setCorpo(corpo);

                recensioneDAO.doSave(recensione);

                session.setAttribute("messaggioSuccesso", "Recensione pubblicata con successo!");
                response.sendRedirect(request.getContextPath() + "/Catalogo?id=" + idProdotto);
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore durante il salvataggio della recensione.");
            response.sendRedirect(request.getContextPath() + "/Catalogo");
        }
    }
}