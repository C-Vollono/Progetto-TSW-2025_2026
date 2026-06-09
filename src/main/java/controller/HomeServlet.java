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
            // 1. Recupero dei prodotti reali dal Database
            List<ProdottoBean> prodottiDalDB = prodottoDAO.doRetrieveAll(); 
            
            // Protezione Anti-Null: se il DAO restituisce null, inizializziamo una lista vuota
            if (prodottiDalDB == null) {
                prodottiDalDB = new ArrayList<>();
            }
            
            // Limitiamo in sicurezza a un massimo di 4 elementi creando una nuova lista (evita UnsupportedOperationException)
            if (prodottiDalDB.size() > 4) {
                prodottiDalDB = new ArrayList<>(prodottiDalDB.subList(0, 4));
            }
            
            // 2. OPERAZIONE DI WRAPPING/ADATTAMENTO PER LA TUA INDEX.JSP
            List<Map<String, Object>> prodottiInEvidenzaAdattati = new ArrayList<>();
            
            for (ProdottoBean p : prodottiDalDB) {
                Map<String, Object> mappaProdotto = new HashMap<>();
                
                // Mappiamo le proprietà del tuo Bean sui nomi esatti cercati dall'Expression Language della tua JSP
                mappaProdotto.put("id", p.getIdProdotto());                         // Per ${prodotto.id}
                mappaProdotto.put("immagine", p.getUrlImmagine());                   // Per ${prodotto.immagine}
                
                // Uniamo Marca e Modello nel campo unico "nome" cercato dal frontend
                String nomeCompleto = (p.getMarca() != null ? p.getMarca() : "") + " " + (p.getModello() != null ? p.getModello() : "");
                mappaProdotto.put("nome", nomeCompleto.trim());                      // Per ${prodotto.nome}
                
                mappaProdotto.put("descrizione", p.getDescrizione());               // Per ${prodotto.descrizione}
                mappaProdotto.put("prezzo", p.getPrezzo());                         // Per ${prodotto.prezzo}
                
                prodottiInEvidenzaAdattati.add(mappaProdotto);
            }
            
            // 3. SPEDIZIONE ALLA REQUEST CON LA CHIAVE ESATTA DELLA JSP
            request.setAttribute("prodottiInEvidenza", prodottiInEvidenzaAdattati);
            
            // Inoltro alla index.jsp (il carosello statico rimarrà intatto e funzionante)
            request.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            // In caso di errore sul DB, passiamo una lista vuota per far scattare il <c:otherwise> del catalogo in aggiornamento
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