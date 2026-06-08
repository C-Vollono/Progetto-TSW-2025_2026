package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.bean.ProdottoBean;
import model.bean.UtenteBean;
import model.dao.ProdottoDAO;

@WebServlet("/Admin/GestioneProdotti")
public class GestioneProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Controllo di sicurezza: Accessibile solo se l'utente è Admin
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) { 
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "editForm":
                    // Carica il prodotto per riempire il form di modifica
                    int idEdit = Integer.parseInt(request.getParameter("idProdotto"));
                    ProdottoBean prodDaModificare = prodottoDAO.doRetrieveByKey(idEdit);
                    request.setAttribute("prodotto", prodDaModificare);
                    request.getRequestDispatcher("/jsp/admin/formProdotto.jsp").forward(request, response);
                    break;

                case "delete":
                    // Cancellazione del prodotto
                    int idDel = Integer.parseInt(request.getParameter("idProdotto"));
                    try {
                        prodottoDAO.doDelete(idDel);
                        request.setAttribute("messaggioSuccesso", "Prodotto rimosso con successo dal catalogo.");
                    } catch (SQLException e) {
                        // Gestione vincolo RESTRICT (Se il prodotto è in un ordine non può essere rimosso)
                        request.setAttribute("messaggioErrore", "Impossibile eliminare: lo strumento è legato a ordini passati.");
                    }
                    mostraLista(request, response);
                    break;

                case "list":
                default:
                    mostraLista(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Sicurezza sulle richieste POST
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        
        // Lettura parametri dal form
        String idParam = request.getParameter("idProdotto");
        String marca = request.getParameter("marca");
        String modello = request.getParameter("modello");
        String tipo = request.getParameter("tipo");
        int quantita = Integer.parseInt(request.getParameter("quantita"));
        String descrizione = request.getParameter("descrizione");
        double prezzo = Double.parseDouble(request.getParameter("prezzo"));
        int idMicro = Integer.parseInt(request.getParameter("idMicro"));
        String urlImmagine = request.getParameter("urlImmagine");

        ProdottoBean p = new ProdottoBean();
        p.setMarca(marca);
        p.setModello(modello);
        p.setTipo(tipo);
        p.setQuantita(quantita);
        p.setDescrizione(descrizione);
        p.setPrezzo(prezzo);
        p.setIdMicro(idMicro);
        p.setUrlImmagine(urlImmagine);

        try {
            if ("update".equals(action)) {
                p.setIdProdotto(Integer.parseInt(idParam));
                prodottoDAO.doUpdate(p);
                request.setAttribute("messaggioSuccesso", "Prodotto '" + modello + "' aggiornato con successo.");
            } else { 
                prodottoDAO.doSave(p);
                request.setAttribute("messaggioSuccesso", "Nuovo strumento inserito correttamente.");
            }
            mostraLista(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Errore nel salvataggio dei dati sul Database.");
            mostraLista(request, response);
        }
    }

    // MODIFICATO: Rimosso il 'throws SQLException' dalla firma e gestito internamente
    private void mostraLista(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        try {
            prodotti = prodottoDAO.doRetrieveAll();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("messaggioErrore", "Errore critico durante il recupero dei prodotti dal database.");
        }
        request.setAttribute("listaProdotti", prodotti);
        request.getRequestDispatcher("/jsp/admin/admin.jsp").forward(request, response);
    }
}