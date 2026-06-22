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
import model.bean.RecensioneBean;
import model.dao.ProdottoDAO;
import model.dao.MacrocategoriaDAO;
import model.dao.MicrocategoriaDAO;
import model.dao.RecensioneDAO;


@WebServlet("/Catalogo")
public class CatalogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;
    private MacrocategoriaDAO macrocategoriaDAO;
    private MicrocategoriaDAO microcategoriaDAO;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
        this.macrocategoriaDAO = new MacrocategoriaDAO();
        this.microcategoriaDAO = new MicrocategoriaDAO();
        this.recensioneDAO = new RecensioneDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");

        try {
            if (idParam != null && !idParam.trim().isEmpty()) {
                int id = Integer.parseInt(idParam);
                ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(id);
                
                if (prodotto != null) {
                    
                    List<RecensioneBean> recensioni = recensioneDAO.doRetrieveByProdotto(id);
                    request.setAttribute("recensioniProdotto", recensioni);
                    
                    request.setAttribute("prodottoDettaglio", prodotto);
                    request.getRequestDispatcher("/jsp/prodotto.jsp").forward(request, response);
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("messaggioErrore", "Lo strumento richiesto non è presente a catalogo.");
                    response.sendRedirect(request.getContextPath() + "/Catalogo");
                }
            } else {
                //CATALOGO GENERALE CON DOPPIO FILTRO ID
                String categoria = request.getParameter("categoria"); // ID Macrocategoria
                String microcategoria = request.getParameter("microcategoria"); // ID Microcategoria
                String marca = request.getParameter("marca");
                String prezzoRange = request.getParameter("prezzo");
                String searchQuery = request.getParameter("searchQuery");
                String ordina = request.getParameter("ordina");

                if (categoria == null) categoria = "All";
                if (microcategoria == null) microcategoria = "All";
                if (marca == null) marca = "All";
                if (prezzoRange == null) prezzoRange = "All";
                if (searchQuery == null) searchQuery = "";
                if (ordina == null) ordina = "rilevanza";

                //Chiamata al DAO con i filtri per ID
                List<ProdottoBean> listaProdotti = prodottoDAO.doRetrieveByFilters(categoria, microcategoria, marca, prezzoRange, searchQuery, ordina);
                
                //Carichiamo dinamicamente tutte le Macrocategorie dal DB per la sidebar
                List<MacrocategoriaBean> tutteLeMacro = macrocategoriaDAO.doRetrieveAll();
                request.setAttribute("tutteLeMacro", tutteLeMacro);

                // =========================================================================
                // CORREZIONE / NUOVO INSERIMENTO: 
                // Carichiamo dinamicamente tutte le marche dal DB per popolare il select/sidebar
                // =========================================================================
                List<String> tutteLeMarche = prodottoDAO.doRetrieveAllMarche();
                request.setAttribute("tutteLeMarche", tutteLeMarche);
                // =========================================================================

                //Se una macro è selezionata, pre-carichiamo le sue micro per mantenere lo stato dei select
                if (!categoria.equalsIgnoreCase("All") && !categoria.trim().isEmpty()) {
                    int idMacro = Integer.parseInt(categoria);
                    List<MicrocategoriaBean> microDiQuestaMacro = microcategoriaDAO.doRetrieveByMacro(idMacro);
                    request.setAttribute("microDiQuestaMacro", microDiQuestaMacro);
                }
                
                //Ripassiamo i parametri per il "selected" in JSTL
                request.setAttribute("selCategoria", categoria);
                request.setAttribute("selMicrocategoria", microcategoria);
                request.setAttribute("selMarca", marca);
                request.setAttribute("selPrezzo", prezzoRange);
                request.setAttribute("searchQuery", searchQuery);
                request.setAttribute("selOrdina", ordina);
                
                request.setAttribute("prodottiCatalogo", listaProdotti);
                request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("messaggioErrore", "Parametri identificativi non validi.");
            response.sendRedirect(request.getContextPath() + "/Catalogo");
            
        } catch (SQLException e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("messaggioErrore", "Errore di sistema nel recupero dei prodotti del catalogo.");
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}