package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.PreferitiBean;
import model.bean.ProdottoBean;
import util.ConPool;

public class PreferitiDAO {

    // 1. AGGIUNGI AI PREFERITI (doSave)
    public void doSave(PreferitiBean preferito) throws SQLException {

        String sql = "INSERT INTO Preferiti (ID_Utente, ID_prodotto) VALUES (?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, preferito.getIdUtente());
            ps.setInt(2, preferito.getIdProdotto());

            ps.executeUpdate();
        }
    }

    // 2. RIMUOVI DAI PREFERITI (doDelete)
    public void doDelete(int idUtente, int idProdotto) throws SQLException {
        String sql = "DELETE FROM Preferiti WHERE ID_Utente = ? AND ID_prodotto = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.setInt(2, idProdotto);

            ps.executeUpdate();
        }
    }

    // 3. RECUPERA TUTTI I PRODOTTI PREFERITI DI UN UTENTE
    public List<ProdottoBean> doRetrieveProdottiByUtente(int idUtente) throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        
        String sql = "SELECT p.* FROM Prodotto p " +
                     "JOIN Preferiti f ON p.ID_prodotto = f.ID_prodotto " +
                     "WHERE f.ID_Utente = ? ORDER BY f.Data_aggiunta DESC";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdottoBean p = new ProdottoBean();
                    p.setIdProdotto(rs.getInt("ID_prodotto"));
                    p.setMarca(rs.getString("Marca"));
                    p.setModello(rs.getString("Modello"));
                    p.setTipo(rs.getString("Tipo"));
                    p.setQuantita(rs.getInt("Quantita"));
                    p.setDescrizione(rs.getString("Descrizione"));
                    p.setPrezzo(rs.getDouble("Prezzo"));
                    p.setIdMicro(rs.getInt("ID_micro"));
                    p.setUrlImmagine(rs.getString("Url_Immagine"));
                    
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    // 4. VERIFICA SE UN PRODOTTO È GIÀ NEI PREFERITI DI UN UTENTE
    public boolean isPreferito(int idUtente, int idProdotto) throws SQLException {
        String sql = "SELECT 1 FROM Preferiti WHERE ID_Utente = ? AND ID_prodotto = ?";
        
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUtente);
            ps.setInt(2, idProdotto);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}