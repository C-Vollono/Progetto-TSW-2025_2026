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
        

        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");
        String isAjax = request.getParameter("isAjax");


        if (email == null || email.trim().isEmpty() || passwordInChiaro == null || passwordInChiaro.isEmpty()) {
        	if ("true".equals(isAjax)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Tutti i campi sono obbligatori!\"}");
                return;
            }
            
            request.setAttribute("messaggioErrore", "Tutti i campi sono obbligatori!");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        //Generazione dell'hash SHA-512 per il confronto
        String passwordCifrata = PasswordHashing.toHash(passwordInChiaro);

        try {
            UtenteBean utente = utenteDao.doRetrieveByLogin(email.trim(), passwordCifrata);

            if (utente != null) {

                HttpSession session = request.getSession(true);
                
                session.setAttribute("utenteLoggato", utente);
                session.setAttribute("ruolo", utente.isIsAdmin() ? "admin" : "customer");
                
                String url;
                String redirectDopoLogin = (String) session.getAttribute("redirectDopoLogin");
                
                if (redirectDopoLogin != null) {
                    url = request.getContextPath() + redirectDopoLogin;
                    session.removeAttribute("redirectDopoLogin");
                } else {
                    url = request.getContextPath() + (utente.isIsAdmin() ? "/jsp/admin/admin.jsp" : "/Home");
                }
 

                //Risposta AJAX
                if ("true".equals(isAjax)) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": true, \"message\": \"Login effettuato!\", \"redirect\": \"" + url + "\"}");
                    return; 
                }
                System.out.println("[LoginServlet] Login riuscito per: " + email);
                response.sendRedirect(url);
                return;
                
            } else {
                //Login fallito
                //Risposta AJAX
                if ("true".equals(isAjax)) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": false, \"message\": \"Email o Password errati!\"}");
                    return;
                }
                
                System.out.println("[LoginServlet] Fallimento: Credenziali non trovate per " + email);
                request.setAttribute("messaggioErrore", "Email o Password errati!");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            
            //Risposta AJAX
            if ("true".equals(isAjax)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"Errore tecnico del server.\"}");
                return;
            }

            request.setAttribute("messaggioErrore", "Si è verificato un errore tecnico nel server. Riprova più tardi.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}