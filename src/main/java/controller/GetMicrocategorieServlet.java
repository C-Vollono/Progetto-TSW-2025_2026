package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.bean.MicrocategoriaBean;
import model.dao.MicrocategoriaDAO;

@WebServlet("/GetMicrocategorie")
public class GetMicrocategorieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MicrocategoriaDAO microcategoriaDAO;

    @Override
    public void init() throws ServletException {
        this.microcategoriaDAO = new MicrocategoriaDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String macroIdParam = request.getParameter("macroId");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (macroIdParam != null && !macroIdParam.equalsIgnoreCase("All") && !macroIdParam.trim().isEmpty()) {
            try {
                int idMacro = Integer.parseInt(macroIdParam);
                
                // CHIAMATA AL TUO METODO DO_RETRIEVE_BY_MACRO
                List<MicrocategoriaBean> lista = microcategoriaDAO.doRetrieveByMacro(idMacro);
                
                // Creazione manuale della stringa JSON standard compatibile
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < lista.size(); i++) {
                    MicrocategoriaBean m = lista.get(i);
                    json.append("{\"id\":").append(m.getIdMicro())
                        .append(",\"nome\":\"").append(m.getNomeMicro().replace("\"", "\\\"")).append("\"}");
                    if (i < lista.size() - 1) json.append(",");
                }
                json.append("]");
                
                response.getWriter().write(json.toString());
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("[]");
            }
        } else {
            response.getWriter().write("[]");
        }
    }
}