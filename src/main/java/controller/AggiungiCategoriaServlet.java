package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bean.MacrocategoriaBean;
import model.bean.MicrocategoriaBean;
import model.dao.MacrocategoriaDAO;
import model.dao.MicrocategoriaDAO;

@WebServlet("/Admin/AggiungiCategoria")
public class AggiungiCategoriaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MacrocategoriaDAO macroDAO = new MacrocategoriaDAO();
    private MicrocategoriaDAO microDAO = new MicrocategoriaDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String target = request.getParameter("target"); // "macro" o "micro"
        PrintWriter out = response.getWriter();

        try {
            if ("macro".equalsIgnoreCase(target)) {
                String nomeMacro = request.getParameter("nomeMacro");
                if (nomeMacro == null || nomeMacro.trim().isEmpty()) {
                    out.print("{\"success\": false, \"message\": \"Nome macro vuoto\"}");
                    return;
                }
                
                MacrocategoriaBean macro = new MacrocategoriaBean();
                macro.setNomeMacro(nomeMacro.trim());
                macroDAO.doSave(macro);
                
                // Recuperiamo l'oggetto appena salvato per ottenere l'ID generato dal DB
                // Nota: se non hai un doRetrieveByName, usiamo doRetrieveAll prendendo l'ultimo inserito o ottimizziamo
                int idGenerato = 0;
                for(MacrocategoriaBean m : macroDAO.doRetrieveAll()) {
                    if(m.getNomeMacro().equalsIgnoreCase(nomeMacro.trim())) {
                        idGenerato = m.getIdMacro();
                    }
                }
                
                out.print("{\"success\": true, \"idMacro\": " + idGenerato + ", \"nomeMacro\": \"" + nomeMacro.trim() + "\"}");
                
            } else if ("micro".equalsIgnoreCase(target)) {
                String nomeMicro = request.getParameter("nomeMicro");
                String idMacroParam = request.getParameter("idMacro");
                
                if (nomeMicro == null || nomeMicro.trim().isEmpty() || idMacroParam == null) {
                    out.print("{\"success\": false, \"message\": \"Dati micro incompleti\"}");
                    return;
                }
                
                int idMacro = Integer.parseInt(idMacroParam);
                MicrocategoriaBean micro = new MicrocategoriaBean();
                micro.setNomeMicro(nomeMicro.trim());
                micro.setIdMacro(idMacro);
                microDAO.doSave(micro);
                
                int idGenerato = 0;
                for(MicrocategoriaBean m : microDAO.doRetrieveAll()) {
                    if(m.getNomeMicro().equalsIgnoreCase(nomeMicro.trim()) && m.getIdMacro() == idMacro) {
                        idGenerato = m.getIdMicro();
                    }
                }
                
                out.print("{\"success\": true, \"idMicro\": " + idGenerato + ", \"nomeMicro\": \"" + nomeMicro.trim() + "\", \"idMacro\": " + idMacro + "}");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Errore DB: " + e.getMessage() + "\"}");
        }
    }
}