package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.DettaglioOrdineBean;
import util.ConPool;

public class DettagliOrdineDAO {

    // 1. INSERISCI UN RECORD DI DETTAGLIO (doSave)
    public void doSave(DettaglioOrdineBean dettaglio) throws SQLException {
        String sql = "INSERT INTO Dettaglio_Ordine (ID_ordine, ID_prodotto, Quantita, Prezzo_unitario_storico) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dettaglio.getIdOrdine());
            ps.setInt(2, dettaglio.getIdProdotto());
            ps.setInt(3, dettaglio.getQuantita());
            ps.setDouble(4, dettaglio.getPrezzoUnitarioStorico());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA TUTTI I PRODOTTI DI UN SINGOLO ORDINE (doRetrieveByOrdine)
    public List<DettaglioOrdineBean> doRetrieveByOrdine(int idOrdine) throws SQLException {
        List<DettaglioOrdineBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Dettaglio_Ordine WHERE ID_ordine = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idOrdine);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private DettaglioOrdineBean mapResultSetToBean(ResultSet rs) throws SQLException {
        DettaglioOrdineBean d = new DettaglioOrdineBean();
        d.setIdOrdine(rs.getInt("ID_ordine"));
        d.setIdProdotto(rs.getInt("ID_prodotto"));
        d.setQuantita(rs.getInt("Quantita"));
        d.setPrezzoUnitarioStorico(rs.getDouble("Prezzo_unitario_storico"));
        return d;
    }
}