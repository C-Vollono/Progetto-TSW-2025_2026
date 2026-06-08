package util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpFilter;
import model.bean.UtenteBean;

@WebFilter(filterName = "/AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[FILTRO-INIT] L'AccessControlFilter è partito con successo!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 1. Recuperiamo la sessione (senza crearne una nuova se non esiste)
        HttpSession session = httpRequest.getSession(false);
        
        // 2. Estraiamo il Bean Utente e il flag isAdmin
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utenteLoggato") : null;
        
        Boolean isAdmin = null;
        if (utente != null) {
            isAdmin = utente.isIsAdmin(); 
        }
        
        // 3. Controlliamo l'URI completo convertito in MINUSCOLO
        String path = httpRequest.getRequestURI().toLowerCase();
        
        System.out.println("[AccessControl-DEBUG] Richiesta intercettata verso l'URI: " + path);
        
        // --- 4. BLOCCO DELLE ECCEZIONI (FONDAMENTALE) ---
        // Se la richiesta riguarda asset statici, login, registrazione o logout,
        // lasciamo passare la richiesta senza alcun controllo di sicurezza.
        // NOTA: Avendo usato .toLowerCase(), i controlli vengono fatti tutti in minuscolo.
        if (path.contains("/css/") || 
            path.contains("/images/") || 
            path.contains("/js/") || 
            path.endsWith("login.jsp") || 
            path.endsWith("/login") ||
            path.endsWith("registrazione.jsp") || 
            path.endsWith("/registrazione") ||
            path.endsWith("/logout")) { // <--- AGGIUNTO: Eccezione per la LogoutServlet
            
            chain.doFilter(request, response);
            return;
        }

        // --- APPLICAZIONE LOGICA DEL PROFESSORE ---
        
        // Caso A: L'utente prova ad andare in un'area comune ma non è loggato
        if (path.contains("/jsp/common/") && utente == null) {
            System.out.println("[AccessControl] Bloccato utente non loggato su area common: " + path);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp");
            return;
        }
        
        // Caso B: L'utente prova ad andare in un'area amministrativa ma non è loggato OPPURE non è admin
        if (path.contains("/jsp/admin/")) {
            if (utente == null || isAdmin == null || !isAdmin) {
                System.out.println("[AccessControl] ACCESSO NEGATO su area admin per la risorsa: " + path);
                
                // Impostiamo l'errore da mostrare nel form
                httpRequest.setAttribute("erroreLogin", "Accesso negato! Area riservata agli amministratori.");
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp");
                return;
            }
        }

        // Se supera le restrizioni, la risorsa è accessibile (es: index.jsp generale)
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}