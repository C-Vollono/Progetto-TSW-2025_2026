package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.RecensioneBean;
import util.ConPool;

public class RecensioneDAO {

    // 1. INSERISCI UNA RECENSIONE (doSave)
    public void doSave(RecensioneBean recensione) throws SQLException {
        // Lasciamo che Data_recensione prenda il DEFAULT CURRENT_TIMESTAMP del DB
        String sql = "INSERT INTO Recensione (ID_Utente, ID_prodotto, Valutazione, Titolo, Corpo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recensione.getIdUtente());
            ps.setInt(2, recensione.getIdProdotto());
            ps.setInt(3, recensione.getValutazione());
            ps.setString(4, recensione.getTitolo());
            ps.setString(5, recensione.getCorpo());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA RECENSIONE PER CHIAVE PRIMARIA (doRetrieveByKey)
    public RecensioneBean doRetrieveByKey(int idRecensione) throws SQLException {
        String sql = "SELECT * FROM Recensione WHERE ID_recensione = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idRecensione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RECUPERA TUTTE LE RECENSIONI DI UN PRODOTTO (Scheda Prodotto Front-End)
    public List<RecensioneBean> doRetrieveByProdotto(int idProdotto) throws SQLException {
        List<RecensioneBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Recensione WHERE ID_prodotto = ? ORDER BY Data_recensione DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProdotto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }

    // 4. RECUPERA TUTTE LE RECENSIONI SCRITTE DA UN UTENTE (Area Personale)
    public List<RecensioneBean> doRetrieveByUtente(int idUtente) throws SQLException {
        List<RecensioneBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Recensione WHERE ID_Utente = ? ORDER BY Data_recensione DESC";

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

    // 5. ELIMINA UNA RECENSIONE (doDelete)
    public void doDelete(int idRecensione) throws SQLException {
        String sql = "DELETE FROM Recensione WHERE ID_recensione = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRecensione);
            ps.executeUpdate();
        }
    }

    // Metodo di utility interno per mapping ResultSet -> Bean
    private RecensioneBean mapResultSetToBean(ResultSet rs) throws SQLException {
        RecensioneBean r = new RecensioneBean();
        r.setIdRecensione(rs.getInt("ID_recensione"));
        r.setIdUtente(rs.getInt("ID_Utente"));
        r.setIdProdotto(rs.getInt("ID_prodotto"));
        r.setValutazione(rs.getInt("Valutazione"));
        r.setTitolo(rs.getString("Titolo"));
        r.setCorpo(rs.getString("Corpo"));
        r.setDataRecensione(rs.getTimestamp("Data_recensione"));
        return r;
    }
}