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

    // Gestiamo sia GET che POST per massima flessibilità
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
        
        // 1. Recuperiamo la sessione corrente. Passando 'false', se la sessione 
        // non esiste o è già scaduta, non ne viene creata una nuova inutilmente.
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            System.out.println("[LogoutServlet] Rimozione utente e invalidazione sessione ID: " + session.getId());
            
            // 2. Distrugge la sessione e tutti i relativi dati (utenteLoggato, ruolo, carrello, ecc.)
            session.invalidate();
        }
        
        // 3. Impostiamo un messaggio di conferma da mostrare sulla pagina di login (Richiesto dalla Checklist!)
        request.setAttribute("messaggioSuccesso", "Disconnessione effettuata con successo. A presto!");
        
        // 4. Reindirizziamo l'utente alla pagina di Login (o alla index.jsp se preferisci)
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }
}