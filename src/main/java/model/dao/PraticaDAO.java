package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.PraticaBean;
import util.ConPool;

public class PraticaDAO {

    // 1. INSERISCI UNA NUOVA PRATICA (doSave)
    public void doSave(PraticaBean pratica) throws SQLException {
        String sql = "INSERT INTO Pratica (ID_ticket, Interventi_previsti, Costo_riparazione, Data_completamento) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pratica.getIdTicket());
            ps.setString(2, pratica.getInterventiPrevisti());
            ps.setDouble(3, pratica.getCostoRiparazione());
            ps.setTimestamp(4, pratica.getDataCompletamento());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA PRATICA PER CHIAVE PRIMARIA (doRetrieveByKey)
    public PraticaBean doRetrieveByKey(int idPratica) throws SQLException {
        String sql = "SELECT * FROM Pratica WHERE ID_pratica = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPratica);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RECUPERA PRATICA PARTENDO DALL'ID TICKET
    public PraticaBean doRetrieveByTicket(int idTicket) throws SQLException {
        String sql = "SELECT * FROM Pratica WHERE ID_ticket = ?";

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

    // 4. RECUPERA TUTTE LE PRATICHE REGISTRATE (doRetrieveAll)
    public List<PraticaBean> doRetrieveAll() throws SQLException {
        List<PraticaBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pratica ORDER BY ID_pratica DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 5. AGGIORNA INTERVENTI, COSTI O DATA DI COMPLETAMENTO (doUpdate)
    public void doUpdate(PraticaBean pratica) throws SQLException {
        String sql = "UPDATE Pratica SET ID_ticket = ?, Interventi_previsti = ?, Costo_riparazione = ?, Data_completamento = ? "
                   + "WHERE ID_pratica = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pratica.getIdTicket());
            ps.setString(2, pratica.getInterventiPrevisti());
            ps.setDouble(3, pratica.getCostoRiparazione());
            ps.setTimestamp(4, pratica.getDataCompletamento());
            ps.setInt(5, pratica.getIdPratica());

            ps.executeUpdate();
        }
    }

    // 6. ELIMINA PRATICA (doDelete)
    public void doDelete(int idPratica) throws SQLException {
        String sql = "DELETE FROM Pratica WHERE ID_pratica = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPratica);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private PraticaBean mapResultSetToBean(ResultSet rs) throws SQLException {
        PraticaBean p = new PraticaBean();
        p.setIdPratica(rs.getInt("ID_pratica"));
        p.setIdTicket(rs.getInt("ID_ticket"));
        p.setInterventiPrevisti(rs.getString("Interventi_previsti"));
        p.setCostoRiparazione(rs.getDouble("Costo_riparazione"));
        p.setDataCompletamento(rs.getTimestamp("Data_completamento"));
        return p;
    }
}