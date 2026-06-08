package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importato per gestire Statement.RETURN_GENERATED_KEYS
import java.util.ArrayList;
import java.util.List;
import model.bean.OrdineBean;
import util.ConPool;

public class OrdineDAO {

    // 1. INSERISCI NUOVO ORDINE E RECUPERA LE CHIAVI PRIMARIE (doSave)
    public void doSave(OrdineBean ordine) throws SQLException {
        String sql = "INSERT INTO Ordine (ID_Utente, Totale_ordine, Stato_ordine, Spedizione_Nome_Cognome, "
                   + "Spedizione_Via, Spedizione_Numero_civico, Spedizione_Cap, Spedizione_Citta, "
                   + "Spedizione_Provincia, Spedizione_Telefono, Pagamento_Circuito, Pagamento_Numero_Carta_Oscurato) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Passiamo Statement.RETURN_GENERATED_KEYS come secondo parametro per salvare la key
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ordine.getIdUtente());
            ps.setDouble(2, ordine.getTotaleOrdine());
            ps.setString(3, ordine.getStatoOrdine());
            ps.setString(4, ordine.getSpedizioneNomeCognome());
            ps.setString(5, ordine.getSpedizioneVia());
            ps.setString(6, ordine.getSpedizioneNumeroCivico());
            ps.setString(7, ordine.getSpedizioneCap());
            ps.setString(8, ordine.getSpedizioneCitta());
            ps.setString(9, ordine.getSpedizioneProvincia());
            ps.setString(10, ordine.getSpedizioneTelefono());
            ps.setString(11, ordine.getPagamentoCircuito());
            ps.setString(12, ordine.getPagamentoNumeroCartaOscurato());

            ps.executeUpdate();

            // Recuperiamo l'ID generato automaticamente dal database
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Iniettiamo l'ID appena generato direttamente all'interno del Bean
                    ordine.setIdOrdine(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Errore nel salvataggio dell'ordine: nessun ID generato restituito.");
                }
            }
        }
    }

    // 2. RECUPERA UN ORDINE DALLA CHIAVE PRIMARIA (doRetrieveByKey)
    public OrdineBean doRetrieveByKey(int idOrdine) throws SQLException {
        String sql = "SELECT * FROM Ordine WHERE ID_ordine = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idOrdine);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RECUPERA TUTTI GLI ORDINI DI UN DETERMINATO UTENTE ORDINATI DALL'ULTIMO EFFETTUATO AL PIU' VECCHIO (doRetrieveByUtente)
    public List<OrdineBean> doRetrieveByUtente(int idUtente) throws SQLException {
        List<OrdineBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ordine WHERE ID_Utente = ? ORDER BY Data_ordine DESC";

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

    // 4. MODIFICA STATO DELL'ORDINE (doUpdate)
    // Utile per l'Admin che deve contrassegnare l'ordine come "SPEDITO" o "CONSEGNATO"
    public void doUpdateStato(int idOrdine, String nuovoStato) throws SQLException {
        String sql = "UPDATE Ordine SET Stato_ordine = ? WHERE ID_ordine = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuovoStato);
            ps.setInt(2, idOrdine);

            ps.executeUpdate();
        }
    }

    // 5. ELIMINA UN ORDINE (doDelete)
    public void doDelete(int idOrdine) throws SQLException {
        String sql = "DELETE FROM Ordine WHERE ID_ordine = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrdine);
            ps.executeUpdate();
        }
    }
    
    // 6. RECUPERA TUTTI GLI ORDINI DEL PORTALE ORDINATI DAL PIÙ RECENTE (doRetrieveAll)
    public List<OrdineBean> doRetrieveAll() throws SQLException {
        List<OrdineBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ordine ORDER BY Data_ordine DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private OrdineBean mapResultSetToBean(ResultSet rs) throws SQLException {
        OrdineBean o = new OrdineBean();
        o.setIdOrdine(rs.getInt("ID_ordine"));
        o.setIdUtente(rs.getInt("ID_Utente"));
        o.setDataOrdine(rs.getTimestamp("Data_ordine"));
        o.setTotaleOrdine(rs.getDouble("Totale_ordine"));
        o.setStatoOrdine(rs.getString("Stato_ordine"));
        o.setSpedizioneNomeCognome(rs.getString("Spedizione_Nome_Cognome"));
        o.setSpedizioneVia(rs.getString("Spedizione_Via"));
        o.setSpedizioneNumeroCivico(rs.getString("Spedizione_Numero_civico"));
        o.setSpedizioneCap(rs.getString("Spedizione_Cap"));
        o.setSpedizioneCitta(rs.getString("Spedizione_Citta"));
        o.setSpedizioneProvincia(rs.getString("Spedizione_Provincia"));
        o.setSpedizioneTelefono(rs.getString("Spedizione_Telefono"));
        o.setPagamentoCircuito(rs.getString("Pagamento_Circuito"));
        o.setPagamentoNumeroCartaOscurato(rs.getString("Pagamento_Numero_Carta_Oscurato"));
        return o;
    }
}