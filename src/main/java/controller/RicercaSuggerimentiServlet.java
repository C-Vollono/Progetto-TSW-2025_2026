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

@WebServlet("/RicercaSuggerimenti")
public class RicercaSuggerimentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Configurazione nativa della risposta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String testoDigitato = request.getParameter("q");

        // Se l'utente non ha digitato nulla o solo spazi, restituiamo un array JSON vuoto immediatamente
        if (testoDigitato == null || testoDigitato.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        try {
            List<ProdottoBean> suggerimenti = prodottoDAO.doRetrieveBySearch(testoDigitato.trim());
            
            // 2. COSTRUZIONE MANUALE DEL JSON CON CONTROLLO ANTI-NULL
            StringBuilder json = new StringBuilder();
            json.append("["); 
            
            for (int i = 0; i < suggerimenti.size(); i++) {
                ProdottoBean p = suggerimenti.get(i);
                
                // Fallback di sicurezza se i testi nel DB sono nulli
                String marcaSanitizzata = (p.getMarca() != null) ? p.getMarca().replace("\"", "\\\"") : "";
                String modelloSanitizzato = (p.getModello() != null) ? p.getModello().replace("\"", "\\\"") : "";
                
                json.append("{");
                json.append("\"idProdotto\":").append(p.getIdProdotto()).append(",");
                json.append("\"marca\":\"").append(marcaSanitizzata).append("\",");
                json.append("\"modello\":\"").append(modelloSanitizzato).append("\",");
                json.append("\"prezzo\":").append(p.getPrezzo());
                json.append("}");
                
                if (i < suggerimenti.size() - 1) {
                    json.append(",");
                }
            }
            
            json.append("]"); 
            
            // 3. Spediamo la stringa al JavaScript della pagina
            response.getWriter().write(json.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            // Impostiamo lo status HTTP 500 (Internal Server Error)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // IMPORTANTE: Manteniamo la coerenza semantica della chiave usando 'messaggioErrore' anche nel JSON
            response.getWriter().write("{\"messaggioErrore\": \"Errore di comunicazione con il database durante la ricerca.\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}