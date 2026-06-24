package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.bean.ProdottoBean;
import model.dao.ProdottoDAO;

@WebServlet(urlPatterns = {"/Home", ""})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {

            List<ProdottoBean> prodottiDalDB = prodottoDAO.doRetrieveAll(); 
            

            if (prodottiDalDB == null) {
                prodottiDalDB = new ArrayList<>();
            }
            
            if (prodottiDalDB.size() > 4) {
                prodottiDalDB = new ArrayList<>(prodottiDalDB.subList(0, 4));
            }
            
            List<Map<String, Object>> prodottiInEvidenzaAdattati = new ArrayList<>();
            
            for (ProdottoBean p : prodottiDalDB) {
                Map<String, Object> mappaProdotto = new HashMap<>();
                
                mappaProdotto.put("idProdotto", p.getIdProdotto());                        
                mappaProdotto.put("urlImmagine", p.getUrlImmagine());                   
                
                // Uniamo Marca e Modello nel campo unico "nome" cercato dal frontend
                String nomeCompleto = (p.getMarca() != null ? p.getMarca() : "") + " " + (p.getModello() != null ? p.getModello() : "");
                
                mappaProdotto.put("nome", nomeCompleto.trim());                    
                mappaProdotto.put("descrizione", p.getDescrizione());             
                mappaProdotto.put("prezzo", p.getPrezzo());                      
                mappaProdotto.put("tipo", p.getTipo());                			
                mappaProdotto.put("valutazione", p.getValutazione());  				
                
                prodottiInEvidenzaAdattati.add(mappaProdotto);
            }
            
            request.setAttribute("prodottiInEvidenza", prodottiInEvidenzaAdattati);
            
            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("prodottiInEvidenza", new ArrayList<>());
            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}