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
    
    //REGEX VALIDAZIONE LATO SERVER
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
    	String action = request.getParameter("action");

        //CONTROLLO AJAX IN TEMPO REALE: EMAIL
        if ("checkEmail".equals(action)) {
            String email = request.getParameter("email");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                boolean esiste = utenteDao.checkEmailExists(email);
                response.getWriter().write("{\"esiste\": " + esiste + "}");
            } catch (SQLException e) {
                response.getWriter().write("{\"esiste\": false}");
            }
            return;
        }

        //CONTROLLO AJAX IN TEMPO REALE: USERNAME
        if ("checkUsername".equals(action)) {
            String username = request.getParameter("username");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try {
                boolean esiste = utenteDao.checkUsernameExists(username);
                response.getWriter().write("{\"esiste\": " + esiste + "}");
            } catch (SQLException e) {
                response.getWriter().write("{\"esiste\": false}");
            }
            return;
        }
        request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");
        String username = request.getParameter("username");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String dataNascitaStr = request.getParameter("dataNascita");
        String isAjax = request.getParameter("isAjax");
        
        boolean ajax = "true".equals(isAjax);

        //Validazione di sicurezza
        if (email == null || email.trim().isEmpty() || 
            passwordInChiaro == null || passwordInChiaro.isEmpty() ||
            username == null || username.trim().isEmpty() ||
            nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty()) {
            
        	if (ajax) { sendJsonError(response, "Tutti i campi obbligatori devono essere compilati!"); return; }
            request.setAttribute("messaggioErrore", "Tutti i campi obbligatori devono essere compilati!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        email = email.trim();
        username = username.trim();

        if (!Pattern.matches(EMAIL_REGEX, email)) {
        	if (ajax) { sendJsonError(response, "Formato email non valido!"); return; }
            request.setAttribute("messaggioErrore", "Formato email non valido!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (!Pattern.matches(USERNAME_REGEX, username)) {
        	if (ajax) { sendJsonError(response, "L'username deve essere alfanumerico (4-20 caratteri)!"); return; }
            request.setAttribute("messaggioErrore", "L'username deve essere alfanumerico (4-20 caratteri)!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        if (!Pattern.matches(PASSWORD_REGEX, passwordInChiaro)) {
        	if (ajax) { sendJsonError(response, "La password deve contenere almeno 8 caratteri, una lettera e un numero!"); return; }
            request.setAttribute("messaggioErrore", "La password deve contenere almeno 8 caratteri, una lettera e un numero!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }
        
     //Verifica doppioni prima del salvataggio
        try {
            if (utenteDao.checkEmailExists(email)) {
                if (ajax) { sendJsonError(response, "Email già registrata e utilizzata!"); return; }
                request.setAttribute("messaggioErrore", "Email già registrata e utilizzata!");
                request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
                return;
            }
            if (utenteDao.checkUsernameExists(username)) {
                if (ajax) { sendJsonError(response, "Username già in uso! Scegline un altro."); return; }
                request.setAttribute("messaggioErrore", "Username già in uso! Scegline un altro.");
                request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (ajax) { sendJsonError(response, "Errore di connessione al database."); return; }
        }

        //Generazione dell'hash SHA-512
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

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
            boolean inserimentoSuccesso = utenteDao.doSave(nuovoUtente); 

            if (inserimentoSuccesso) {
            	//RISPOSTA AJAX SUCCESSO
                if (ajax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": true, \"message\": \"Registrazione completata!\", \"redirect\": \"" + request.getContextPath() + "/Login\"}");
                    return;
                }
                
                HttpSession session = request.getSession(true);
                session.setAttribute("messaggioSuccesso", "Registrazione completata con successo! Adesso puoi accedere.");
                response.sendRedirect(request.getContextPath() + "/Login");
                return;
            } else {
                if (ajax) { sendJsonError(response, "Errore durante la registrazione. Riprova."); return; }
                request.setAttribute("messaggioErrore", "Errore durante la registrazione. Riprova.");
                request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            }

        } catch (SQLException e) {
        	e.printStackTrace();
            if (ajax) { sendJsonError(response, "Email o Username inseriti potrebbero essere già associati ad un profilo."); return; }
            request.setAttribute("messaggioErrore", "Email o Username inseriti potrebbero essere già associati ad un profilo.");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
        }
    }

    private void sendJsonError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }
}