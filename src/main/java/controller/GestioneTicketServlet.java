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

import model.bean.TicketBean;
import model.bean.UtenteBean;
import model.dao.TicketDAO;


@WebServlet("/Admin/GestioneTicket")
public class GestioneTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TicketDAO ticketDAO;

    @Override
    public void init() throws ServletException {
        // Inizializzazione dei DAO all'avvio della servlet
        ticketDAO = new TicketDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. CONTROLLO DI SICUREZZA (RBAC)
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("utenteLoggato") == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        UtenteBean utente = (UtenteBean) session.getAttribute("utenteLoggato");
        if (!utente.isIsAdmin()) { 
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato. Area riservata agli amministratori.");
            return;
        }

        // 2. GESTIONE DELLE AZIONI
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; 
        }

        try {
            switch (action) {
                case "list":
                    listTickets(request, response);
                    break;
                case "view":
                    viewTicket(request, response);
                    break;
                case "updateStatus":
                    updateTicketStatus(request, response);
                    break;
                default:
                    listTickets(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Errore nel database durante la gestione dei ticket admin", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirige tutte le richieste POST al doGet per centralizzare la logica
        doGet(request, response);
    }

    // AZIONE: Mostra tutti i ticket presenti nel database
    private void listTickets(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        List<TicketBean> listaTicket = ticketDAO.doRetrieveAll();
        
        request.setAttribute("listaTicket", listaTicket);
        request.getRequestDispatcher("/jsp/admin/ticket_admin.jsp").forward(request, response);
    }

    // AZIONE: Mostra i dettagli di un singolo ticket selezionato e la relativa Pratica
    private void viewTicket(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        int idTicket = Integer.parseInt(request.getParameter("idTicket"));
        TicketBean ticket = ticketDAO.doRetrieveByKey(idTicket);
        
        if (ticket != null) {
            request.setAttribute("ticket", ticket);
         
           
            
           
            request.getRequestDispatcher("/jsp/admin/dettagliTicket.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/Admin/GestioneTicket?action=list");
        }
    }

    // AZIONE: Modifica lo stato del ticket (es. da IN_ATTESA a ACCETTATO)
    private void updateTicketStatus(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        int idTicket = Integer.parseInt(request.getParameter("idTicket"));
        String nuovoStato = request.getParameter("stato"); 

        TicketBean ticket = ticketDAO.doRetrieveByKey(idTicket);
        
        if (ticket != null && nuovoStato != null) {
            ticket.setStato(nuovoStato);
            ticketDAO.doUpdate(ticket); 
        }
        
        response.sendRedirect(request.getContextPath() + "/Admin/GestioneTicket?action=view&idTicket=" + idTicket);
    }

   
}