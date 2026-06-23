package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Paths;
import javax.servlet.http.Part;

import model.bean.UtenteBean;
import model.bean.OrdineBean;
import model.bean.TicketBean;
import model.bean.DatiSpedizioneBean;
import model.dao.OrdineDAO;
import model.dao.TicketDAO;
import model.dao.UtenteDAO;
import model.dao.DatiSpedizioneDAO;
import model.bean.DatiPagamentoBean;
import model.dao.DatiPagamentoDAO;
import model.dao.DettagliOrdineDAO;
import model.bean.DettaglioOrdineBean;
import model.dao.ProdottoDAO;
import model.bean.ProdottoBean;
import model.dao.PreferitiDAO;
import model.dao.RecensioneDAO;
import model.bean.RecensioneBean;


@WebServlet("/Profilo")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProfiloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;
    private TicketDAO ticketDAO;
    private UtenteDAO utenteDAO;
    private DatiSpedizioneDAO spedizioneDAO;
    private DatiPagamentoDAO pagamentoDAO;
    private PreferitiDAO preferitiDAO;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
        this.ticketDAO = new TicketDAO();
        this.utenteDAO = new UtenteDAO();
        this.spedizioneDAO = new DatiSpedizioneDAO();
        this.pagamentoDAO = new DatiPagamentoDAO();
        this.preferitiDAO = new PreferitiDAO();
        this.recensioneDAO = new RecensioneDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("utenteLoggato");

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        try {
            List<OrdineBean> tuttiOrdini = ordineDAO.doRetrieveByUtente(utente.getIdUtente()); 
            List<TicketBean> tuttiTicket = ticketDAO.doRetrieveByUtente(utente.getIdUtente()); 
            List<DatiSpedizioneBean> datiSpedizione = spedizioneDAO.doRetrieveByUtente(utente.getIdUtente());
            List<DatiPagamentoBean> datiPagamento = pagamentoDAO.doRetrieveByUtente(utente.getIdUtente());
            List<ProdottoBean> preferiti = preferitiDAO.doRetrieveProdottiByUtente(utente.getIdUtente());
            List<RecensioneBean> mieRecensioni = recensioneDAO.doRetrieveByUtente(utente.getIdUtente());
            int maxTicket = Math.min(tuttiTicket.size(), 3);
            int maxOrdini = Math.min(tuttiOrdini.size(), 3);
            
            request.setAttribute("ordiniRecenti", tuttiOrdini.subList(0, maxOrdini));
            request.setAttribute("ticketRecenti", tuttiTicket.subList(0, maxTicket));
            request.setAttribute("tuttiTicket", tuttiTicket);
            request.setAttribute("tuttiOrdini", tuttiOrdini);
            request.setAttribute("datiSpedizione", datiSpedizione);
            request.setAttribute("datiPagamento", datiPagamento);
            request.setAttribute("preferiti", preferiti);            
            request.setAttribute("mieRecensioni", mieRecensioni);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/jsp/common/profilo.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("utenteLoggato");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (utente == null) {
            sendJsonError(response, "Sessione scaduta. Effettua di nuovo il login.");
            return;
        }

        String action = request.getParameter("action");

        try {
            // AGGIORNAMENTO DATI PERSONALI
            if ("aggiornaDati".equals(action)) {
                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String dataNascitaStr = request.getParameter("dataNascita");

                utente.setNome(nome.trim());
                utente.setCognome(cognome.trim());
                if (dataNascitaStr != null && !dataNascitaStr.trim().isEmpty()) {
                    utente.setDataDiNascita(java.sql.Date.valueOf(dataNascitaStr));
                }

                utenteDAO.doUpdate(utente); 
                session.setAttribute("utenteLoggato", utente);

                response.getWriter().write("{\"success\": true, \"message\": \"Dati aggiornati con successo!\"}");
                return;
            }

            // CAMBIO PASSWORD
            if ("cambiaPassword".equals(action)) {
                String oldPassword = request.getParameter("oldPassword");
                String newPassword = request.getParameter("newPassword");
                
                String hashedOld = util.PasswordHashing.toHash(oldPassword);
                if (!hashedOld.equals(utente.getPassword())) {
                    sendJsonError(response, "La password attuale inserita non è corretta.");
                    return;
                }
                
                String hashedNew = util.PasswordHashing.toHash(newPassword);
                utente.setPassword(hashedNew);
                
                utenteDAO.doUpdate(utente);
                session.setAttribute("utenteLoggato", utente);
                
                response.getWriter().write("{\"success\": true, \"message\": \"Password modificata con successo!\"}");
                return;
            }
            
            // AGGIUNGI INDIRIZZO
            if ("aggiungiIndirizzo".equals(action)) {
                DatiSpedizioneBean ind = new DatiSpedizioneBean();
                ind.setIdUtente(utente.getIdUtente());
                ind.setVia(request.getParameter("via").trim());
                ind.setNumeroCivico(request.getParameter("numeroCivico").trim());
                ind.setCitta(request.getParameter("citta").trim());
                ind.setProvincia(request.getParameter("provincia").trim().toUpperCase());
                ind.setCap(request.getParameter("cap").trim());
                String tel = request.getParameter("telefono");
                ind.setTelefono(tel != null ? tel.trim() : "");
                spedizioneDAO.doSave(ind);
                response.getWriter().write("{\"success\": true, \"message\": \"Indirizzo aggiunto con successo!\"}");
                return;
            }

            // ELIMINA INDIRIZZO
            if ("eliminaIndirizzo".equals(action)) {
                int idSpedizione = Integer.parseInt(request.getParameter("idSpedizione"));
                DatiSpedizioneBean ind = spedizioneDAO.doRetrieveByKey(idSpedizione);
                if (ind != null && ind.getIdUtente() == utente.getIdUtente()) {
                    spedizioneDAO.doDelete(idSpedizione);
                    response.getWriter().write("{\"success\": true, \"message\": \"Indirizzo eliminato.\"}");
                } else {
                    sendJsonError(response, "Indirizzo non trovato o non autorizzato.");
                }
                return;
            }
            
            // --- CARTA DI PAGAMENTO ---
            if ("aggiungiPagamento".equals(action)) {
                String numeroCarta = request.getParameter("numeroCarta").replaceAll("\\s+", "");
                String oscurato = "****" + numeroCarta.substring(numeroCarta.length() - 4);

                DatiPagamentoBean carta = new DatiPagamentoBean();
                carta.setIdUtente(utente.getIdUtente());
                carta.setCircuitoCarta(request.getParameter("circuitoCarta"));
                carta.setNumeroCartaOscurato(oscurato);
                carta.setIntestatario(request.getParameter("intestatario").trim());
                carta.setScadenzaCarta(java.sql.Date.valueOf(request.getParameter("scadenzaCarta") + "-01"));
                pagamentoDAO.doSave(carta);

                response.getWriter().write("{\"success\": true, \"message\": \"Carta aggiunta con successo!\"}");
                return;
            }

            if ("eliminaPagamento".equals(action)) {
                int idPagamento = Integer.parseInt(request.getParameter("idPagamento"));
                DatiPagamentoBean carta = pagamentoDAO.doRetrieveByKey(idPagamento);
                if (carta != null && carta.getIdUtente() == utente.getIdUtente()) {
                    pagamentoDAO.doDelete(idPagamento);
                    response.getWriter().write("{\"success\": true, \"message\": \"Carta rimossa.\"}");
                } else {
                    sendJsonError(response, "Carta non trovata o non autorizzato.");
                }
                return;
            }
            
            // --- DETTAGLI ORDINE (AJAX) ---
            if ("dettagliOrdine".equals(action)) {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                
                DettagliOrdineDAO dettDao = new DettagliOrdineDAO();
                ProdottoDAO prodDao = new ProdottoDAO();
                List<DettaglioOrdineBean> dettagli = dettDao.doRetrieveByOrdine(idOrdine);

                StringBuilder json = new StringBuilder("[");
                for(int i=0; i<dettagli.size(); i++) {
                    DettaglioOrdineBean d = dettagli.get(i);
                    ProdottoBean p = prodDao.doRetrieveByKey(d.getIdProdotto());
                    String nomeProd = "Prodotto rimosso dal catalogo";
                    
                    if (p != null) {
                        String completo = p.getMarca() + " " + p.getModello();
                        nomeProd = completo.replace("\"", "\\\"");
                    }
                    
                    json.append("{")
                        .append("\"nome\": \"").append(nomeProd).append("\",")
                        .append("\"quantita\": ").append(d.getQuantita()).append(",")
                        .append("\"prezzo\": ").append(d.getPrezzoUnitarioStorico())
                        .append("}");
                    if(i < dettagli.size() - 1) json.append(",");
                }
                json.append("]");

                response.getWriter().write("{\"success\": true, \"dettagli\": " + json.toString() + "}");
                return;
            }

            // --- RIMUOVI DAI PREFERITI (AJAX) ---
            if ("rimuoviPreferito".equals(action)) {
                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                preferitiDAO.doDelete(utente.getIdUtente(), idProdotto);
                response.getWriter().write("{\"success\": true, \"message\": \"Prodotto rimosso dai preferiti.\"}");
                return;
            }
            
            // --- ELIMINA RECENSIONE (AJAX) ---
            if ("eliminaRecensione".equals(action)) {
                int idRecensione = Integer.parseInt(request.getParameter("idRecensione"));
                RecensioneBean r = recensioneDAO.doRetrieveByKey(idRecensione);
                if (r != null && r.getIdUtente() == utente.getIdUtente()) {
                    recensioneDAO.doDelete(idRecensione);
                    response.getWriter().write("{\"success\": true, \"message\": \"Recensione eliminata.\"}");
                } else {
                    sendJsonError(response, "Recensione non trovato o non autorizzata.");
                }
                return;
            }
            
            // --- APRI NUOVO TICKET (AJAX) --
            if ("apriTicket".equals(action)) {
                String oggetto = request.getParameter("oggetto");
                String messaggio = request.getParameter("messaggio");

                if (messaggio == null || messaggio.trim().isEmpty()) {
                    sendJsonError(response, "La descrizione del problema non può essere vuota.");
                    return;
                }

                String descrizioneCompleta = "OGGETTO: " + (oggetto != null ? oggetto.trim() : "Nessuno") + "\n\n" + messaggio.trim();

                TicketBean nuovoTicket = new TicketBean();
                nuovoTicket.setIdUtente(utente.getIdUtente());
                nuovoTicket.setDescrizione(descrizioneCompleta);
                nuovoTicket.setStato("Aperto");

                Part filePart = request.getPart("allegato");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                    
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "tickets";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    
                    filePart.write(uploadPath + File.separator + uniqueFileName);
                    nuovoTicket.setAllegato(uniqueFileName);
                } else {
                    nuovoTicket.setAllegato(null);
                }

                ticketDAO.doSave(nuovoTicket);
                response.getWriter().write("{\"success\": true, \"message\": \"Ticket aperto con successo!\"}");
                return;
            }
            
            // --- DETTAGLI TICKET (AJAX) ---
            if ("dettagliTicket".equals(action)) {
                int idTicket = Integer.parseInt(request.getParameter("idTicket"));
                TicketBean ticket = ticketDAO.doRetrieveByKey(idTicket);
                
                if (ticket != null && ticket.getIdUtente() == utente.getIdUtente()) {
                    String descrizione = ticket.getDescrizione() != null ? 
                        ticket.getDescrizione().replace("\"", "\\\"").replace("\n", "\\n") : "";
                        
                    String stato = ticket.getStato() != null ? ticket.getStato() : "";
                    String allegato = ticket.getAllegato() != null ? ticket.getAllegato().replace("\"", "\\\"") : "";
                    
                    response.getWriter().write("{"
                        + "\"success\": true, "
                        + "\"messaggio\": \"" + descrizione + "\", " 
                        + "\"stato\": \"" + stato + "\", "
                        + "\"allegato\": \"" + allegato + "\""
                        + "}");
                } else {
                    sendJsonError(response, "Ticket non trovato o non autorizzato.");
                }
                return;
            }
            
            // --- AGGIUNGI AI PREFERITI (AJAX) ---
            if ("aggiungiPreferito".equals(action)) {
                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                
                // Usiamo l'istanza globale preferitiDAO già presente e inizializzata nell'init()
                if (preferitiDAO.isPreferito(utente.getIdUtente(), idProdotto)) {
                    sendJsonError(response, "Questo strumento è già nei tuoi preferiti!");
                    return;
                }

                model.bean.PreferitiBean nuovoPreferito = new model.bean.PreferitiBean();
                nuovoPreferito.setIdUtente(utente.getIdUtente());
                nuovoPreferito.setIdProdotto(idProdotto);
                
                preferitiDAO.doSave(nuovoPreferito);
                response.getWriter().write("{\"success\": true, \"message\": \"Aggiunto ai preferiti con successo!\"}");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonError(response, "Errore interno del server durante l'elaborazione.");
        }
    }

    private void sendJsonError(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }
}