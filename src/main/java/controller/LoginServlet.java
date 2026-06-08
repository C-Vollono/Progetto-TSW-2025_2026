package controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.bean.UtenteBean;
import model.dao.UtenteDAO;
import util.PasswordHashing;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Recupero dei parametri inviati tramite form HTML
        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");

        // Validazione di sicurezza formale sui campi di input
        if (email == null || email.trim().isEmpty() || passwordInChiaro == null || passwordInChiaro.isEmpty()) {
            request.setAttribute("erroreLogin", "Tutti i campi sono obbligatori!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // Generazione dell'hash SHA-512 per confrontarlo in sicurezza con il record nel DB
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

        UtenteDAO utenteDao = new UtenteDAO();

        try {
            // 4. Interrogazione del Database tramite il metodo doRetrieveByLogin del UtenteDAO
            UtenteBean utente = utenteDao.doRetrieveByLogin(email, passwordCifrata);

            if (utente != null) {
                // LOGIN ANDATO A BUON FINE
                HttpSession session = request.getSession();
                
                // Salviamo l'intero Bean dell'utente in sessione
                session.setAttribute("utenteLoggato", utente);
                
                // Gestione della profilazione utenti
                if (utente.isIsAdmin()) {
                    session.setAttribute("ruolo", "admin");
                } else {
                    session.setAttribute("ruolo", "customer");
                }
                
                System.out.println("[LoginServlet] Login riuscito nel DB per l'utente: " + email);
                
                // --- MODIFICA: SMISTAMENTO UTENTI IN BASE AL RUOLO ---
                if (utente.isIsAdmin()) {
                    // Se l'utente è amministratore, lo reindirizziamo all'area admin
                    response.sendRedirect(request.getContextPath() + "/jsp/admin/admin");
                } else {
                    // Se l'utente è un cliente normale, lo mandiamo alla index generale
                    response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
                }
                
            } else {
                // LOGIN FALLITO: Le credenziali inserite non trovano corrispondenze nel DB
                request.setAttribute("erroreLogin", "Email o Password errati!");
                System.out.println("[LoginServlet] Fallimento: Credenziali non trovate nel database per " + email);
                
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            // 5. GESTIONE ECCEZIONI: Catturiamo l'errore SQL senza far crashare l'applicazione
            System.err.println("[LoginServlet] Errore critico di connessione al database durante il login:");
            e.printStackTrace();
            
            // Reindirizziamo a una pagina di errore generica o rimandiamo un messaggio pulito alla JSP
            request.setAttribute("erroreLogin", "Si è verificato un errore tecnico nel server. Riprova più tardi.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}