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
import model.dao.OrdineDAO;
import model.dao.TicketDAO;
import model.dao.UtenteDAO;

@WebServlet("/Profilo")
public class ProfiloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;
    private TicketDAO ticketDAO;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
        this.ticketDAO = new TicketDAO();
        this.utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("utenteLoggato");

        // Sicurezza: se non sei loggato, non puoi vedere il profilo
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        try {
            // Sfruttiamo i DAO per recuperare tutti gli ordini e ticket dell'utente
            List<OrdineBean> tuttiOrdini = ordineDAO.doRetrieveByUtente(utente.getIdUtente()); 
            List<TicketBean> tuttiTicket = ticketDAO.doRetrieveByUtente(utente.getIdUtente()); 

            // Estrapoliamo solo i primi 3 per la vista compatta della panoramica
            int maxOrdini = Math.min(tuttiOrdini.size(), 3);
            request.setAttribute("ordiniRecenti", tuttiOrdini.subList(0, maxOrdini));

            int maxTicket = Math.min(tuttiTicket.size(), 3);
            request.setAttribute("ticketRecenti", tuttiTicket.subList(0, maxTicket));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Dopo aver caricato i dati, andiamo alla pagina
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
            // --- AZIONE 1: AGGIORNAMENTO DATI PERSONALI ---
            if ("aggiornaDati".equals(action)) {
                String nome = request.getParameter("nome");
                String cognome = request.getParameter("cognome");
                String dataNascitaStr = request.getParameter("dataNascita");

                // Aggiorniamo solo i campi modificabili nel Bean
                utente.setNome(nome.trim());
                utente.setCognome(cognome.trim());
                if (dataNascitaStr != null && !dataNascitaStr.trim().isEmpty()) {
                    utente.setDataDiNascita(java.sql.Date.valueOf(dataNascitaStr));
                }

                // Chiamiamo il metodo di Update del DAO (che aggiorna l'intera riga nel DB)
                utenteDAO.doUpdate(utente); 
                
                // Aggiorniamo l'utente in sessione per far apparire le modifiche in tempo reale sulla pagina
                session.setAttribute("utenteLoggato", utente);

                response.getWriter().write("{\"success\": true, \"message\": \"Dati aggiornati con successo!\"}");
                return;
            }

            // --- AZIONE 2: CAMBIO PASSWORD ---
            if ("cambiaPassword".equals(action)) {
                String oldPassword = request.getParameter("oldPassword");
                String newPassword = request.getParameter("newPassword");
                
                // 1. Verifichiamo che la vecchia password coincida
                String hashedOld = util.PasswordHashing.toHash(oldPassword);
                if (!hashedOld.equals(utente.getPassword())) {
                    sendJsonError(response, "La password attuale inserita non è corretta.");
                    return;
                }
                
                // 2. Hash della nuova password e salvataggio
                String hashedNew = util.PasswordHashing.toHash(newPassword);
                utente.setPassword(hashedNew);
                
                utenteDAO.doUpdate(utente);
                session.setAttribute("utenteLoggato", utente);
                
                response.getWriter().write("{\"success\": true, \"message\": \"Password modificata con successo!\"}");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJsonError(response, "Errore interno del server durante il salvataggio.");
        }
    }

    // Metodo di supporto per gli errori JSON
    private void sendJsonError(HttpServletResponse response, String message) throws IOException {
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }
}