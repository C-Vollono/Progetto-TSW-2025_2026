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

    // IL METODO GET GESTISCE SOLO LA LETTURA DEI DATI E IL RENDERING DELLE FORM
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Controllo di sicurezza Admin
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        if (utente == null || !utente.isIsAdmin()) { 
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            if ("editForm".equals(action)) {
                // Carica il prodotto per riempire il form di modifica
                int idEdit = Integer.parseInt(request.getParameter("idProdotto"));
                ProdottoBean prodDaModificare = prodottoDAO.doRetrieveByKey(idEdit);
                
                if (prodDaModificare != null) {
                    request.setAttribute("prodotto", prodDaModificare);
                    request.getRequestDispatcher("/jsp/admin/formProdotto.jsp").forward(request, response);
                } else {
                    session = request.getSession();
                    session.setAttribute("messaggioErrore", "Strumento non trovato.");
                    response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
                }
            } else {
                // Caso di default: list
                mostraLista(request, response);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            mostraListaConErrore(request, response, "Identificativo prodotto non valido.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    // IL METODO POST GESTISCE TUTTE REALI SCRITTURE/CANCELLAZIONI SUL DB (PRG)
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
        session = request.getSession(); // Recuperiamo la sessione per i Flash Attributes dei messaggi

        // CASO 1: ELIMINAZIONE STRUMENTO (Spostato in POST per sicurezza architetturale)
        if ("delete".equals(action)) {
            try {
                int idDel = Integer.parseInt(request.getParameter("idProdotto"));
                prodottoDAO.doDelete(idDel);
                session.setAttribute("messaggioSuccesso", "Prodotto rimosso con successo dal catalogo.");
            } catch (NumberFormatException e) {
                session.setAttribute("messaggioErrore", "Impossibile eliminare: ID non valido.");
            } catch (SQLException e) {
                // Gestione vincolo RESTRICT 
                session.setAttribute("messaggioErrore", "Impossibile eliminare: lo strumento è presente in ordini già effettuati.");
            }
            response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
            return;
        }

        // CASO 2 & 3: INSERIMENTO O MODIFICA
        try {
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

            if ("update".equals(action)) {
                p.setIdProdotto(Integer.parseInt(idParam));
                prodottoDAO.doUpdate(p);
                session.setAttribute("messaggioSuccesso", "Prodotto '" + modello + "' aggiornato con successo.");
            } else { 
                prodottoDAO.doSave(p);
                session.setAttribute("messaggioSuccesso", "Nuovo strumento '" + modello + "' inserito correttamente.");
            }
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore di formattazione: controlla che i campi Quantità e Prezzo siano numerici.");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("messaggioErrore", "Errore nel salvataggio dei dati sul Database.");
        }

        // APPLICAZIONE RIGOROSA DEL PATTERN PRG
        response.sendRedirect(request.getContextPath() + "/Admin/GestioneProdotti");
    }

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

    private void mostraListaConErrore(HttpServletRequest request, HttpServletResponse response, String msg) 
            throws ServletException, IOException {
        request.setAttribute("messaggioErrore", msg);
        mostraLista(request, response);
    }
}