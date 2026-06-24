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
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
        	
        	// Debug da terminale per verificare la verifica della condizione tramite messaggio di errore e recupero del ID utente 
            System.out.println("[LogoutServlet] Rimozione utente e invalidazione sessione ID: " + session.getId());
            
            // Distrugge la vecchia sessione e tutti i relativi dati dell'utente loggato
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/Home");
    }
}