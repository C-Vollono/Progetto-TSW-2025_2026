package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.bean.UtenteBean;
import model.dao.UtenteDAO;
import util.PasswordHashing;

@WebServlet("/Registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public RegistrazioneServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Se un utente prova ad accedere via GET, lo mostriamo il form di registrazione
        request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recupero dei parametri inviati dal form HTML di registrazione
        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");
        String username = request.getParameter("username");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String dataNascitaStr = request.getParameter("data_di_nascita");

        // 2. Validazione di sicurezza formale sui campi obbligatori
        if (email == null || email.trim().isEmpty() || 
            passwordInChiaro == null || passwordInChiaro.isEmpty() ||
            username == null || username.trim().isEmpty() ||
            nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty()) {
            
            request.setAttribute("erroreRegistrazione", "Tutti i campi obbligatori devono essere compilati!");
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            return;
        }

        // 3. Generazione dell'hash SHA-512 per salvare la password in totale sicurezza nel DB
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

        // 4. Costruzione del Bean Utente da passare al DAO
        UtenteBean nuovoUtente = new UtenteBean();
        nuovoUtente.setEmail(email.trim());
        nuovoUtente.setPassword(passwordCifrata);
        nuovoUtente.setUsername(username.trim());
        nuovoUtente.setNome(nome.trim());
        nuovoUtente.setCognome(cognome.trim());
        nuovoUtente.setIsAdmin(false); // Un utente che si registra da solo è SEMPRE un cliente/customer

        // Gestione facoltativa della data di nascita
        if (dataNascitaStr != null && !dataNascitaStr.trim().isEmpty()) {
            try {
                nuovoUtente.setDataDiNascita(Date.valueOf(dataNascitaStr)); // Converte stringa YYYY-MM-DD in java.sql.Date
            } catch (IllegalArgumentException e) {
                System.out.println("[RegistrazioneServlet] Formato data non valido: " + dataNascitaStr);
            }
        }

        UtenteDAO utenteDao = new UtenteDAO();

        try {
            // 5. Chiamata al DAO per inserire il record nel DB (presuppone un metodo doSave)
            // NOTA: Se il tuo DAO restituisce un boolean o lancia eccezione per chiavi duplicate (Email/Username), lo gestiamo qui
            boolean inserimentoSuccesso = utenteDao.doSave(nuovoUtente); 

            if (inserimentoSuccesso) {
                System.out.println("[RegistrazioneServlet] Registrazione riuscita nel DB per: " + email);
                
                // Impostiamo un messaggio di successo da mostrare nella pagina di login
                request.setAttribute("messaggioSuccesso", "Registrazione completata con successo! Adesso puoi accedere.");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            } else {
                request.setAttribute("erroreRegistrazione", "Errore durante la registrazione. Scegli un'altra Email o Username.");
                request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            System.err.println("[RegistrazioneServlet] Errore critico SQL durante l'inserimento dell'utente:");
            e.printStackTrace();
            
            // Gestione classica del vincolo UNIQUE violato (Email o Username già esistenti nel DB)
            String msgErrore = "Si è verificato un errore tecnico. Email o Username potrebbero essere già registrati.";
            request.setAttribute("erroreRegistrazione", msgErrore);
            request.getRequestDispatcher("/jsp/registrazione.jsp").forward(request, response);
        }
    }
}