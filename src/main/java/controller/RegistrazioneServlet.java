package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.regex.Pattern; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.bean.UtenteBean;
import model.dao.UtenteDAO;
import util.PasswordHashing;

@WebServlet("/Registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDao;
    
    // --- COSTANTI REGEX PER LA VALIDAZIONE LATO SERVER ---
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{4,20}$";

    public RegistrazioneServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        this.utenteDao = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recupero dei parametri inviati dal form HTML
        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");
        String username = request.getParameter("username");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String dataNascitaStr = request.getParameter("data_di_nascita");

        // 2. Validazione di sicurezza formale (Chiave standard)
        if (email == null || email.trim().isEmpty() || 
            passwordInChiaro == null || passwordInChiaro.isEmpty() ||
            username == null || username.trim().isEmpty() ||
            nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty()) {
            
            request.setAttribute("messaggioErrore", "Tutti i campi obbligatori devono essere compilati!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        email = email.trim();
        username = username.trim();

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            request.setAttribute("messaggioErrore", "Formato email non valido!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (!Pattern.matches(USERNAME_REGEX, username)) {
            request.setAttribute("messaggioErrore", "L'username deve essere alfanumerico (4-20 caratteri)!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (!Pattern.matches(PASSWORD_REGEX, passwordInChiaro)) {
            request.setAttribute("messaggioErrore", "La password deve contenere almeno 8 caratteri, una lettera e un numero!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        // 3. Generazione dell'hash SHA-512
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

        // 4. Costruzione del Bean Utente
        UtenteBean nuovoUtente = new UtenteBean();
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(passwordCifrata);
        nuovoUtente.setUsername(username);
        nuovoUtente.setNome(nome.trim());
        nuovoUtente.setCognome(cognome.trim());
        nuovoUtente.setIsAdmin(false); 

        if (dataNascitaStr != null && !dataNascitaStr.trim().isEmpty()) {
            try {
                nuovoUtente.setDataDiNascita(Date.valueOf(dataNascitaStr)); 
            } catch (IllegalArgumentException e) {
                System.out.println("[RegistrazioneServlet] Formato data non valido: " + dataNascitaStr);
            }
        }

        try {
            // 5. Scrittura sul DB
            boolean inserimentoSuccesso = utenteDao.doSave(nuovoUtente); 

            if (inserimentoSuccesso) {
                System.out.println("[RegistrazioneServlet] Registrazione riuscita nel DB per: " + email);
                
                // CORREZIONE PRG: Salviamo in sessione e facciamo un Redirect esplicito a /Login
                HttpSession session = request.getSession(true);
                session.setAttribute("messaggioSuccesso", "Registrazione completata con successo! Adesso puoi accedere.");
                response.sendRedirect(request.getContextPath() + "/Login");
                return;
            } else {
                request.setAttribute("messaggioErrore", "Errore durante la registrazione. Scegli un'altra Email o Username.");
                request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            System.err.println("[RegistrazioneServlet] Errore critico SQL durante l'inserimento dell'utente:");
            e.printStackTrace();
            
            request.setAttribute("messaggioErrore", "Email o Username inseriti potrebbero essere già associati ad un profilo esistente.");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
        }
    }
}