package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.ProdottoBean;
import util.ConPool;

public class ProdottoDAO {

    // 1. RECUPERA TUTTI I PRODOTTI
    public List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prodotto";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Sfruttiamo il metodo privato in fondo al file
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 2. RECUPERA UN PRODOTTO DALLA CHIAVE PRIMARIA 
    public ProdottoBean doRetrieveByKey(int idProdotto) throws SQLException {
        String sql = "SELECT * FROM Prodotto WHERE ID_prodotto = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idProdotto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Sfruttiamo il metodo privato in fondo al file
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. INSERISCE UN NUOVO PRODOTTO (doSave)
    public void doSave(ProdottoBean prodotto) throws SQLException {
        String sql = "INSERT INTO Prodotto (Marca, Modello, Tipo, Quantita, Descrizione, Prezzo, ID_micro, Url_Immagine) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, prodotto.getMarca());
            ps.setString(2, prodotto.getModello());
            ps.setString(3, prodotto.getTipo());
            ps.setInt(4, prodotto.getQuantita());
            ps.setString(5, prodotto.getDescrizione());
            ps.setDouble(6, prodotto.getPrezzo());
            ps.setInt(7, prodotto.getIdMicro());
            ps.setString(8, prodotto.getUrlImmagine());

            ps.executeUpdate();
        }
    }

    // 4. MODIFICA UN PRODOTTO ESISTENTE (doUpdate)
    public void doUpdate(ProdottoBean prodotto) throws SQLException {
        String sql = "UPDATE Prodotto SET Marca = ?, Modello = ?, Tipo = ?, Quantita = ?, "
                   + "Descrizione = ?, Prezzo = ?, ID_micro = ?, Url_Immagine = ? "
                   + "WHERE ID_prodotto = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, prodotto.getMarca());
            ps.setString(2, prodotto.getModello());
            ps.setString(3, prodotto.getTipo());
            ps.setInt(4, prodotto.getQuantita());
            ps.setString(5, prodotto.getDescrizione());
            ps.setDouble(6, prodotto.getPrezzo());
            ps.setInt(7, prodotto.getIdMicro());
            ps.setString(8, prodotto.getUrlImmagine());
            ps.setInt(9, prodotto.getIdProdotto()); 

            ps.executeUpdate();
        }
    }

    // 5. ELIMINA UN PRODOTTO (doDelete)
    public void doDelete(int idProdotto) throws SQLException {
        String sql = "DELETE FROM Prodotto WHERE ID_prodotto = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProdotto);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private ProdottoBean mapResultSetToBean(ResultSet rs) throws SQLException {
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
        return p;
    }
}