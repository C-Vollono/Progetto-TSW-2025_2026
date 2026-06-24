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

@WebServlet("/SuggerimentiProdotti")
public class SuggerimentiProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String term = request.getParameter("term");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (term != null && term.trim().length() >= 2) {
            try {
               
                List<ProdottoBean> suggerimenti = prodottoDAO.doRetrieveBySearch(term.trim());
                
                StringBuilder jsonBuilder = new StringBuilder("[");
                for (int i = 0; i < suggerimenti.size(); i++) {
                    ProdottoBean p = suggerimenti.get(i);
                    
                    String nomeCompleto = (p.getMarca() + " " + p.getModello()).replace("\"", "\\\"");
                    
                    jsonBuilder.append("{\"nome\":\"").append(nomeCompleto).append("\"}");
                    
                    if (i < suggerimenti.size() - 1) {
                        jsonBuilder.append(",");
                    }
                }
                jsonBuilder.append("]");
                
             
                response.getWriter().write(jsonBuilder.toString());
                
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
 
            response.getWriter().write("[]");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}