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

    // Costante applicativa per l'aliquota IVA standard del negozio (22%)
    private static final int IVA_DEFAULT = 22;

    // 1. RECUPERA TUTTI I PRODOTTI
    public List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prodotto";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
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

    // 6. SCALA LA QUANTITÀ IN MAGAZZINO DOPO UN ACQUISTO (Essenziale per transazione di Checkout)
    public void doUpdateQuantita(int idProdotto, int quantitaAcquistata, Connection con) throws SQLException {
        String sql = "UPDATE Prodotto SET Quantita = Quantita - ? WHERE ID_prodotto = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantitaAcquistata);
            ps.setInt(2, idProdotto);
            ps.executeUpdate();
        }
    }

    // 7. RECUPERA PRODOTTI DI UNA SPECIFICA MICROCATEGORIA
    public List<ProdottoBean> doRetrieveByMicrocategoria(int idMicro) throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prodotto WHERE ID_micro = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idMicro);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }

    // 8. RECUPERA 3 PRODOTTI CASUALI PER L'HERO DELLA HOMEPAGE
    public List<ProdottoBean> doRetrieveInEvidenza() throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prodotto ORDER BY RAND() LIMIT 3";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 9. RECUPERA I PRODOTTI PER I SUGGERIMENTI DI RICERCA (LIVE SEARCH AJAX)
    public List<ProdottoBean> doRetrieveBySearch(String query) throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        // Cerchiamo corrispondenze parziali sulla Marca o sul Modello nel DB
        String sql = "SELECT * FROM Prodotto WHERE Marca LIKE ? OR Modello LIKE ? LIMIT 5";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Applichiamo i caratteri jolly per la corrispondenza parziale
            String keyword = "%" + query + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
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
        
        // Assegniamo al Bean il valore dell'IVA corrente dell'e-commerce,
        // che verrà poi catturato e scritto sul DB dalla servlet di checkout.
        p.setIva(IVA_DEFAULT); 
        
        return p;
    }
}