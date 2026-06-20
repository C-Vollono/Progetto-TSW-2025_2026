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

@WebServlet("/Profilo")
public class ProfiloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;
    private TicketDAO ticketDAO;
    private UtenteDAO utenteDAO;
    private DatiSpedizioneDAO spedizioneDAO;
    private DatiPagamentoDAO pagamentoDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
        this.ticketDAO = new TicketDAO();
        this.utenteDAO = new UtenteDAO();
        this.spedizioneDAO = new DatiSpedizioneDAO();
        this.pagamentoDAO = new DatiPagamentoDAO();
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

            int maxOrdini = Math.min(tuttiOrdini.size(), 3);
            request.setAttribute("ordiniRecenti", tuttiOrdini.subList(0, maxOrdini));

            int maxTicket = Math.min(tuttiTicket.size(), 3);
            request.setAttribute("ticketRecenti", tuttiTicket.subList(0, maxTicket));
            
            request.setAttribute("datiSpedizione", datiSpedizione);
            
            request.setAttribute("datiPagamento", datiPagamento);

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
            //AGGIORNAMENTO DATI PERSONALI
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

            //CAMBIO PASSWORD
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
            
         //AGGIUNGI INDIRIZZO
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

            //ELIMINA INDIRIZZO
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
            
         // --- AZIONE 5: AGGIUNGI CARTA DI PAGAMENTO ---
            if ("aggiungiPagamento".equals(action)) {
                String numeroCarta = request.getParameter("numeroCarta").replaceAll("\\s+", "");
                // Salviamo solo le ultime 4 cifre oscurate
                String oscurato = "****" + numeroCarta.substring(numeroCarta.length() - 4);

                DatiPagamentoBean carta = new DatiPagamentoBean();
                carta.setIdUtente(utente.getIdUtente());
                carta.setCircuitoCarta(request.getParameter("circuitoCarta"));
                carta.setNumeroCartaOscurato(oscurato);
                carta.setIntestatario(request.getParameter("intestatario").trim());
                // input type="month" restituisce "yyyy-MM", convertiamo in Date aggiungendo il giorno 01
                carta.setScadenzaCarta(java.sql.Date.valueOf(request.getParameter("scadenzaCarta") + "-01"));
                pagamentoDAO.doSave(carta);

                response.getWriter().write("{\"success\": true, \"message\": \"Carta aggiunta con successo!\"}");
                return;
            }

            // --- AZIONE 6: ELIMINA CARTA DI PAGAMENTO ---
            if ("eliminaPagamento".equals(action)) {
                int idPagamento = Integer.parseInt(request.getParameter("idPagamento"));
                DatiPagamentoBean carta = pagamentoDAO.doRetrieveByKey(idPagamento);
                if (carta != null && carta.getIdUtente() == utente.getIdUtente()) {
                    pagamentoDAO.doDelete(idPagamento);
                    response.getWriter().write("{\"success\": true, \"message\": \"Carta rimossa.\"}");
                } else {
                    sendJsonError(response, "Carta non trovata o non autorizzata.");
                }
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonError(response, "Errore interno del server durante il salvataggio.");
        }
    }

    private void sendJsonError(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }
}