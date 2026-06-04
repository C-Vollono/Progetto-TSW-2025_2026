package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.TicketBean;

public class TicketDAO {

    // 1. INSERISCI UN NUOVO TICKET (doSave)
    public void doSave(TicketBean ticket) throws SQLException {
        // Lasciamo che Data_apertura e Stato prendano automaticamente i DEFAULT definiti sul database
        String sql = "INSERT INTO Ticket (ID_Utente, Descrizione, Allegato) VALUES (?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticket.getIdUtente());
            ps.setString(2, ticket.getDescrizione());
            ps.setString(3, ticket.getAllegato());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA UN TICKET PER ID CHIAVE PRIMARIA (doRetrieveByKey)
    public TicketBean doRetrieveByKey(int idTicket) throws SQLException {
        String sql = "SELECT * FROM Ticket WHERE ID_ticket = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idTicket);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RECUPERA TUTTI I TICKET DI UN DETERMINATO UTENTE (doRetrieveByUtente)
    public List<TicketBean> doRetrieveByUtente(int idUtente) throws SQLException {
        List<TicketBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ticket WHERE ID_Utente = ? ORDER BY Data_apertura DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }

    // 4. RECUPERA TUTTI I TICKET REGISTRATI NEL SITO (doRetrieveAll)
    public List<TicketBean> doRetrieveAll() throws SQLException {
        List<TicketBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ticket ORDER BY Data_apertura DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 5. AGGIORNA STATO O DETTAGLI DI UN TICKET (doUpdate)
    public void doUpdate(TicketBean ticket) throws SQLException {
        String sql = "UPDATE Ticket SET ID_Utente = ?, Descrizione = ?, Allegato = ?, Stato = ? WHERE ID_ticket = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ticket.getIdUtente());
            ps.setString(2, ticket.getDescrizione());
            ps.setString(3, ticket.getAllegato());
            ps.setString(4, ticket.getStato());
            ps.setInt(5, ticket.getIdTicket());

            ps.executeUpdate();
        }
    }

    // 6. ELIMINA UN TICKET (doDelete)
    public void doDelete(int idTicket) throws SQLException {
        String sql = "DELETE FROM Ticket WHERE ID_ticket = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTicket);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private TicketBean mapResultSetToBean(ResultSet rs) throws SQLException {
        TicketBean t = new TicketBean();
        t.setIdTicket(rs.getInt("ID_ticket"));
        t.setIdUtente(rs.getInt("ID_Utente"));
        t.setDataApertura(rs.getTimestamp("Data_apertura"));
        t.setDescrizione(rs.getString("Descrizione"));
        t.setAllegato(rs.getString("Allegato"));
        t.setStato(rs.getString("Stato"));
        return t;
    }
}