package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public LogoutServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recuperiamo la sessione corrente senza crearne una nuova
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            System.out.println("[LogoutServlet] Rimozione utente e invalidazione sessione ID: " + session.getId());
            
            // 2. Distrugge la vecchia sessione e tutti i relativi dati dell'utente loggato
            session.invalidate();
        }
        
        // 3. CREAZIONE DI UNA NUOVA SESSIONE PULITA (Post-Invalidate)
        // Questa sessione è totalmente nuova, non ha i vecchi dati dell'utente ma ci serve 
        // come contenitore temporaneo per far viaggiare il messaggio di successo nel redirect.
        HttpSession nuovaSessione = request.getSession(true);
        nuovaSessione.setAttribute("messaggioSuccesso", "Disconnessione effettuata con successo. A presto!");
        
        // 4. REDIRECT SICURO: l'URL del browser si aggiorna e pulisce la cronologia delle richieste
        response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
    }
}