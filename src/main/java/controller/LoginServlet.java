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
    
    // Dichiarazione del Dao utente per l'utilizzo in questa servlet essendo che devo utilizzare i metodi di inserimento del Dao (doRetriveByLogin)
    private UtenteDAO utenteDao;
       
    public LoginServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        this.utenteDao = new UtenteDAO();
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

        // Validazione formale con chiave standard 'messaggioErrore'
        if (email == null || email.trim().isEmpty() || passwordInChiaro == null || passwordInChiaro.isEmpty()) {
            request.setAttribute("messaggioErrore", "Tutti i campi sono obbligatori!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // 3. Generazione dell'hash SHA-512 per il confronto
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

        try {
            // 4. Interrogazione del Database
            UtenteBean utente = utenteDao.doRetrieveByLogin(email.trim(), passwordCifrata);

            if (utente != null) {
                // --- LOGIN ANDATO A BUON FINE ---
                HttpSession session = request.getSession(true);
                
                // Salviamo l'intero Bean dell'utente in sessione
                session.setAttribute("utenteLoggato", utente);
                
                // Gestione della profilazione utenti
                if (utente.isIsAdmin()) {
                    session.setAttribute("ruolo", "admin");
                } else {
                    session.setAttribute("ruolo", "customer");
                }
                
                System.out.println("[LoginServlet] Login riuscito nel DB per l'utente: " + email);
                
                // --- SMISTAMENTO UTENTI IN BASE AL RUOLO ---
                if (utente.isIsAdmin()) {
                    // Se l'utente è amministratore, lo reindirizziamo all'area admin
                    response.sendRedirect(request.getContextPath() + "/jsp/admin/admin.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
                }
                return; 
                
            } else {
                // LOGIN FALLITO: Uniformata la chiave del messaggio d'errore
                request.setAttribute("messaggioErrore", "Email o Password errati!");
                System.out.println("[LoginServlet] Fallimento: Credenziali non trovate nel database per " + email);
                
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            // 5. GESTIONE ECCEZIONI: Uniformata la chiave del messaggio d'errore
            System.err.println("[LoginServlet] Errore critico di connessione al database durante il login:");
            e.printStackTrace();
            
            request.setAttribute("messaggioErrore", "Si è verificato un errore tecnico nel server. Riprova più tardi.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}