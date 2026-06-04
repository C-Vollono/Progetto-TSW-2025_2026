package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.DatiSpedizioneBean;

public class DatiSpedizioneDAO {

    // 1. INSERISCI NUOVO INDIRIZZO (doSave)
    public void doSave(DatiSpedizioneBean spedizione) throws SQLException {
        String sql = "INSERT INTO Dati_spedizione (ID_Utente, Telefono, Numero_civico, Via, Cap, Citta, Provincia) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, spedizione.getIdUtente());
            ps.setString(2, spedizione.getTelefono());
            ps.setString(3, spedizione.getNumeroCivico());
            ps.setString(4, spedizione.getVia());
            ps.setString(5, spedizione.getCap());
            ps.setString(6, spedizione.getCitta());
            ps.setString(7, spedizione.getProvincia());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA SINGOLO INDIRIZZO DALLA CHIAVE PRIMARIA (ID_spedizione)
    public DatiSpedizioneBean doRetrieveByKey(int idSpedizione) throws SQLException {
        String sql = "SELECT * FROM Dati_spedizione WHERE ID_spedizione = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idSpedizione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. FONDAMENTALE: RECUPERA TUTTI GLI INDIRIZZI DI UN DETERMINATO UTENTE
    public List<DatiSpedizioneBean> doRetrieveByUtente(int idUtente) throws SQLException {
        List<DatiSpedizioneBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Dati_spedizione WHERE ID_Utente = ?";

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

    // 4. MODIFICA UN INDIRIZZO (doUpdate)
    public void doUpdate(DatiSpedizioneBean spedizione) throws SQLException {
        String sql = "UPDATE Dati_spedizione SET ID_Utente = ?, Telefono = ?, Numero_civico = ?, Via = ?, Cap = ?, Citta = ?, Provincia = ? WHERE ID_spedizione = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, spedizione.getIdUtente());
            ps.setString(2, spedizione.getTelefono());
            ps.setString(3, spedizione.getNumeroCivico());
            ps.setString(4, spedizione.getVia());
            ps.setString(5, spedizione.getCap());
            ps.setString(6, spedizione.getCitta());
            ps.setString(7, spedizione.getProvincia());
            ps.setInt(8, spedizione.getIdSpedizione()); // Condizione WHERE

            ps.executeUpdate();
        }
    }

    // 5. ELIMINA UN INDIRIZZO (doDelete)
    public void doDelete(int idSpedizione) throws SQLException {
        String sql = "DELETE FROM Dati_spedizione WHERE ID_spedizione = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSpedizione);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private DatiSpedizioneBean mapResultSetToBean(ResultSet rs) throws SQLException {
        DatiSpedizioneBean s = new DatiSpedizioneBean();
        s.setIdSpedizione(rs.getInt("ID_spedizione"));
        s.setIdUtente(rs.getInt("ID_Utente"));
        s.setTelefono(rs.getString("Telefono"));
        s.setNumeroCivico(rs.getString("Numero_civico"));
        s.setVia(rs.getString("Via"));
        s.setCap(rs.getString("Cap"));
        s.setCitta(rs.getString("Citta"));
        s.setProvincia(rs.getString("Provincia"));
        return s;
    }
}