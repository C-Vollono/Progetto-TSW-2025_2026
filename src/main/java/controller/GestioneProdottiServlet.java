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

// import per gestione download immagini
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet("/Admin/GestioneProdotti")
public class GestioneProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProdottoDAO productoDAO = new ProdottoDAO();
    private MacrocategoriaDAO macrocategoriaDAO = new MacrocategoriaDAO();
    private MicrocategoriaDAO microcategoriaDAO = new MicrocategoriaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            // --- GESTIONE APERTURA FORM INSERIMENTO ---
            if (action != null && action.equalsIgnoreCase("insertForm")) {
                try {
                    List<MacrocategoriaBean> tutteLeMacro = macrocategoriaDAO.doRetrieveAll();
                    List<MicrocategoriaBean> tutteLeMicro = microcategoriaDAO.doRetrieveAll();
                    request.setAttribute("tutteLeMacro", tutteLeMacro);
                    request.setAttribute("tutteLeMicro", tutteLeMicro);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                request.getRequestDispatcher("/jsp/admin/InserimentoProdottoForm.jsp").forward(request, response);
                return;
            }

            // --- GESTIONE APERTURA FORM MODIFICA ---
            if (action != null && action.equalsIgnoreCase("editForm")) {
                try {
                    List<MacrocategoriaBean> tutteLeMacro = macrocategoriaDAO.doRetrieveAll();
                    List<MicrocategoriaBean> tutteLeMicro = microcategoriaDAO.doRetrieveAll();
                    request.setAttribute("tutteLeMacro", tutteLeMacro);
                    request.setAttribute("tutteLeMicro", tutteLeMicro);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                String idParam = request.getParameter("idProdotto");
                if (idParam != null && !idParam.trim().isEmpty()) {
                    try {
                        int idProdotto = Integer.parseInt(idParam);
                        ProdottoBean prodotto = productoDAO.doRetrieveByKey(idProdotto); 
                        request.setAttribute("prodotto", prodotto);
                    } catch (NumberFormatException | SQLException e) {
                        e.printStackTrace();
                        request.getSession().setAttribute("messaggioErrore", "ID Prodotto non valido o non trovato.");
                        response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
                        return;
                    }
                } else {
                    request.getSession().setAttribute("messaggioErrore", "Impossibile modificare: ID Prodotto mancante.");
                    response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
                    return;
                }

                request.getRequestDispatcher("/jsp/admin/modificaProdottoForm.jsp").forward(request, response);
                return;
            }

            // --- GESTIONE VISUALIZZAZIONE CATALOGO STANDARD ---
            String searchQuery = request.getParameter("searchQuery");
            String categoria = request.getParameter("categoria");
            String microcategoria = request.getParameter("microcategoria");
            String marca = request.getParameter("marca");
            String prezzoRange = request.getParameter("prezzo");
            String ordina = request.getParameter("ordina");

            if (categoria == null) categoria = "All";
            if (microcategoria == null) microcategoria = "All";
            if (marca == null) marca = "All";
            if (prezzoRange == null) prezzoRange = "All";
            if (ordina == null) ordina = "rilevanza";
            if (searchQuery == null) searchQuery = "";

            // Recupero Categorie
            List<MacrocategoriaBean> tutteLeMacro = macrocategoriaDAO.doRetrieveAll(); 
            request.setAttribute("tutteLeMacro", tutteLeMacro);

            if (!categoria.equals("All")) {
                int idMacro = Integer.parseInt(categoria);
                List<MicrocategoriaBean> microDiQuestaMacro = microcategoriaDAO.doRetrieveByMacro(idMacro);
                request.setAttribute("microDiQuestaMacro", microDiQuestaMacro);
            }

            // Recupero lista marche
            List<String> tutteLeMarche = productoDAO.doRetrieveAllMarche(); 
            request.setAttribute("tutteLeMarche", tutteLeMarche);

            // true = include anche prodotti con quantita = 0
            List<ProdottoBean> listaProdottiFiltrati = productoDAO.doRetrieveByFilters(
                    categoria, microcategoria, marca, prezzoRange, searchQuery, ordina, true);
            request.setAttribute("prodottiCatalogo", listaProdottiFiltrati); 

            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("selCategoria", categoria);
            request.setAttribute("selMicrocategoria", microcategoria);
            request.setAttribute("selMarca", marca);
            request.setAttribute("selPrezzo", prezzoRange);
            request.setAttribute("selOrdina", ordina);

            request.getRequestDispatcher("/jsp/admin/Catalogo_admin.jsp").forward(request, response);

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
                if (action.equalsIgnoreCase("insert")) {
                    ProdottoBean p = new ProdottoBean();
                    
                    p.setMarca(request.getParameter("marca"));
                    p.setModello(request.getParameter("modello"));
                    p.setTipo(request.getParameter("tipo")); 
                    p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    p.setQuantita(Integer.parseInt(request.getParameter("quantita")));
                    p.setDescrizione(request.getParameter("descrizione"));

                    String rawUrlImg = request.getParameter("urlImmagine");
                    String imagePathToSave = processImageUrl(rawUrlImg, request);
                    p.setUrlImmagine(imagePathToSave);
                    
                    p.setIdMicro(Integer.parseInt(request.getParameter("idMicro")));
                    
                    productoDAO.doSave(p);
                    session.setAttribute("messaggioSuccesso", "Prodotto inserito con successo!");
                    
                } else if (action.equalsIgnoreCase("update")) {
                    ProdottoBean p = new ProdottoBean();
                    p.setIdProdotto(Integer.parseInt(request.getParameter("idProdotto")));
                    p.setModello(request.getParameter("modello"));
                    p.setMarca(request.getParameter("marca"));
                    p.setTipo(request.getParameter("tipo"));
                    p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                    p.setQuantita(Integer.parseInt(request.getParameter("quantita")));
                    p.setDescrizione(request.getParameter("descrizione"));

                    String rawUrlImg = request.getParameter("urlImmagine");
                    String imagePathToSave = processImageUrl(rawUrlImg, request);
                    p.setUrlImmagine(imagePathToSave);

                    p.setIdMicro(Integer.parseInt(request.getParameter("idMicro")));
                    
                    productoDAO.doUpdate(p);
                    session.setAttribute("messaggioSuccesso", "Prodotto aggiornato con successo!");
                    
                } else if (action.equalsIgnoreCase("delete")) {
                    int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                    productoDAO.doDelete(idProdotto);
                    session.setAttribute("messaggioSuccesso", "Prodotto eliminato correttamente!");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore durante l'elaborazione dei dati del prodotto.");
        }
        
        response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
    }

    // Gestisce urlImmagine: path interno oppure URL remoto da scaricare in /images/prodotti
    private String processImageUrl(String rawUrl, HttpServletRequest request) throws IOException {
        if (rawUrl == null) return null;
        String trimmed = rawUrl.trim();
        if (trimmed.isEmpty()) return null;

        // Caso 1: path interno (non http/https)
        if (!(trimmed.startsWith("http://") || trimmed.startsWith("https://"))) {
            if (trimmed.startsWith("images/")) {
                trimmed = trimmed.substring("images/".length());
            } else if (trimmed.startsWith("/images/")) {
                trimmed = trimmed.substring("/images/".length());
            }
            return trimmed; // es. "prodotti/chitarre/lespaul.png"
        }

        // Caso 2: URL remoto -> scarico in /images/prodotti
        URL url = new URL(trimmed);
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        String contentType = conn.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            // non è un'immagine: salvo comunque l'URL così com'è
            return trimmed;
        }

        String extension = ".jpg";
        String path = url.getPath();
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < path.length() - 1) {
            extension = path.substring(dotIndex);
        }

        String fileName = UUID.randomUUID().toString() + extension;
        String relativePath = "prodotti/" + fileName; // ciò che va nel DB

        String imagesRoot = request.getServletContext().getRealPath("/images");
        Path targetDir = Paths.get(imagesRoot, "prodotti");
        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(fileName);
        try (InputStream in = conn.getInputStream()) {
            Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return relativePath;
    }
}