package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.ConPool; // Importa il ConPool che abbiamo sistemato!

@WebServlet("/TestConnection")
public class TestConnectionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Diagnostica Database</h2>");

        try (Connection con = ConPool.getConnection()) {
            out.println("<p style='color:green;'><b>SUCCESSO:</b> Il Connection Pool funziona!</p>");
            out.println("<p>Connessione prelevata dal container Tomcat: " + con.toString() + "</p>");
        } catch (SQLException e) {
            out.println("<p style='color:red;'><b>ERRORE:</b> Connessione fallita!</p>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        }

        out.println("</body></html>");
    }
}