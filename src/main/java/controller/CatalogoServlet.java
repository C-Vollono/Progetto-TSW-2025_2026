package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.bean.ProdottoBean;
import model.dao.ProdottoDAO;

@WebServlet("/Catalogo")
public class CatalogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                // CASO DETTAGLIO: l'utente ha cliccato su un prodotto specifico
                int id = Integer.parseInt(idParam);
                ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(id);
                
                if (prodotto != null) {
                    request.setAttribute("prodottoDettaglio", prodotto);
                    request.getRequestDispatcher("/jsp/prodotto.jsp").forward(request, response);
                } else {
                    // Se l'ID non esiste, rimandiamo al catalogo generale
                    response.sendRedirect(request.getContextPath() + "/Catalogo");
                }
            } else {
                // CASO CATALOGO GENERALE: prendiamo l'intera lista strumenti
                List<ProdottoBean> listaProdotti = prodottoDAO.doRetrieveAll();
                request.setAttribute("prodottiCatalogo", listaProdotti);
                request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}