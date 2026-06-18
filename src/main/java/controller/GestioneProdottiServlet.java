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

import model.bean.ProdottoBean;
import model.bean.MacrocategoriaBean;
import model.bean.MicrocategoriaBean;
import model.dao.ProdottoDAO;
import model.dao.MacrocategoriaDAO;
import model.dao.MicrocategoriaDAO;

@WebServlet("/Admin/GestioneProdotti")
public class GestioneProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Inizializzazione dei DAO in modalità sicura anti-crash container v5+
    private ProdottoDAO prodottoDAO = new ProdottoDAO();
    private MacrocategoriaDAO macrocategoriaDAO = new MacrocategoriaDAO();
    private MicrocategoriaDAO microcategoriaDAO = new MicrocategoriaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            // 1. GESTIONE APERTURA FORM DI MODIFICA (Precompilato come da wireframe)
            if (action != null && action.equalsIgnoreCase("editForm")) {
                String idParam = request.getParameter("idProdotto");
                if (idParam != null && !idParam.trim().isEmpty()) {
                    int idProdotto = Integer.parseInt(idParam);
                    ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto); 
                    request.setAttribute("prodotto", prodotto);
                }
                // Inoltro corretto al nuovo file del form di modifica
                request.getRequestDispatcher("/jsp/admin/modificaProdottoForm.jsp").forward(request, response);
                return;
            }

            // 2. LOGICA DI FILTRO E CARICAMENTO DEL CATALOGO PRINCIPALE ADMIN
            String searchQuery = request.getParameter("searchQuery");
            String categoria = request.getParameter("categoria");
            String microcategoria = request.getParameter("microcategoria");
            String marca = request.getParameter("marca");
            String prezzoRange = request.getParameter("prezzo");
            String ordina = request.getParameter("ordina");

            // Valori di default per evitare NullPointerException
            if (categoria == null) categoria = "All";
            if (microcategoria == null) microcategoria = "All";
            if (marca == null) marca = "All";
            if (prezzoRange == null) prezzoRange = "All";
            if (ordina == null) ordina = "rilevanza";
            if (searchQuery == null) searchQuery = "";

            // Caricamento macro-categorie per filtri sidebar
            List<MacrocategoriaBean> tutteLeMacro = macrocategoriaDAO.doRetrieveAll(); 
            request.setAttribute("tutteLeMacro", tutteLeMacro);

            // Caricamento micro-categorie associate alla macro selezionata
            if (!categoria.equals("All")) {
                int idMacro = Integer.parseInt(categoria);
                List<MicrocategoriaBean> microDiQuestaMacro = microcategoriaDAO.doRetrieveByMacro(idMacro);
                request.setAttribute("microDiQuestaMacro", microDiQuestaMacro);
            }

            // Chiamata al metodo del ProdottoDAO per estrarre la lista filtrata
            List<ProdottoBean> listaProdottiFiltrati = prodottoDAO.doRetrieveByFilters(categoria, microcategoria, marca, prezzoRange, searchQuery, ordina);
            request.setAttribute("prodottiCatalogo", listaProdottiFiltrati); 

            // Mantenimento dello stato dei filtri nella JSP
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("selCategoria", categoria);
            request.setAttribute("selMicrocategoria", microcategoria);
            request.setAttribute("selMarca", marca);
            request.setAttribute("selPrezzo", prezzoRange);
            request.setAttribute("selOrdina", ordina);

            // Inoltro alla griglia del catalogo principale admin
            request.getRequestDispatcher("/jsp/admin/formProdotto.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("messaggioErrore", "Errore nel caricamento del catalogo amministrativo.");
            response.sendRedirect(request.getContextPath() + "/jsp/admin/admin.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        
        try {
            if (action != null) {
                // INSERIMENTO NUOVO PRODOTTO
                if (action.equalsIgnoreCase("save")) {
                    ProdottoBean p = new ProdottoBean();
                    p.setModello(request.getParameter("modello"));
                    p.setMarca(request.getParameter("marca"));
                    p.setTipo(request.getParameter("tipo"));
                    p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    p.setQuantita(Integer.parseInt(request.getParameter("quantita")));
                    p.setDescrizione(request.getParameter("descrizione"));
                    p.setUrlImmagine(request.getParameter("urlImmagine"));
                    p.setIdMicro(Integer.parseInt(request.getParameter("idMicro")));
                    
                    prodottoDAO.doSave(p);
                    session.setAttribute("messaggioSuccesso", "Prodotto inserito con successo!");
                    
                // AGGIORNAMENTO PRODOTTO ESISTENTE (Inviato dal form di modifica)
                } else if (action.equalsIgnoreCase("update")) {
                    ProdottoBean p = new ProdottoBean();
                    p.setIdProdotto(Integer.parseInt(request.getParameter("idProdotto")));
                    p.setModello(request.getParameter("modello"));
                    p.setMarca(request.getParameter("marca"));
                    p.setTipo(request.getParameter("tipo"));
                    p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    p.setQuantita(Integer.parseInt(request.getParameter("quantita")));
                    p.setDescrizione(request.getParameter("descrizione"));
                    p.setUrlImmagine(request.getParameter("urlImmagine"));
                    p.setIdMicro(Integer.parseInt(request.getParameter("idMicro")));
                    
                    prodottoDAO.doUpdate(p);
                    session.setAttribute("messaggioSuccesso", "Prodotto aggiornato con successo!");
                    
                // CANCELLAZIONE PRODOTTO
                } else if (action.equalsIgnoreCase("delete")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    prodottoDAO.doDelete(idProdotto);
                    session.setAttribute("messaggioSuccesso", "Prodotto eliminato correttamente!");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore durante l'elaborazione dei dati del prodotto.");
        }
        
        // Ritorna sempre al catalogo principale aggiornato
        response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
    }
}