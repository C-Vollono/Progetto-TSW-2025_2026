package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.DatiPagamentoBean;
import util.ConPool;

public class DatiPagamentoDAO {

    // 1. SALVA NUOVO METODO DI PAGAMENTO (doSave)
    public void doSave(DatiPagamentoBean pagamento) throws SQLException {
        String sql = "INSERT INTO Dati_pagamento (ID_Utente, Circuito_carta, Numero_carta_oscurato, Scadenza_carta, Intestatario) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pagamento.getIdUtente());
            ps.setString(2, pagamento.getCircuitoCarta());
            ps.setString(3, pagamento.getNumeroCartaOscurato());
            ps.setDate(4, pagamento.getScadenzaCarta());
            ps.setString(5, pagamento.getIntestatario());

            ps.executeUpdate();
        }
    }

    // 2. RECUPERA CARTA PER ID CHIAVE PRIMARIA (ID_pagamento)
    public DatiPagamentoBean doRetrieveByKey(int idPagamento) throws SQLException {
        String sql = "SELECT * FROM Dati_pagamento WHERE ID_pagamento = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idPagamento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RECUPERA TUTTE LE CARTE SALVATE DA UN DETERMINATO UTENTE
    public List<DatiPagamentoBean> doRetrieveByUtente(int idUtente) throws SQLException {
        List<DatiPagamentoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Dati_pagamento WHERE ID_Utente = ?";

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

    // 4. MODIFICA CARTA (doUpdate)
    public void doUpdate(DatiPagamentoBean pagamento) throws SQLException {
        String sql = "UPDATE Dati_pagamento SET ID_Utente = ?, Circuito_carta = ?, Numero_carta_oscurato = ?, Scadenza_carta = ?, Intestatario = ? WHERE ID_pagamento = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pagamento.getIdUtente());
            ps.setString(2, pagamento.getCircuitoCarta());
            ps.setString(3, pagamento.getNumeroCartaOscurato());
            ps.setDate(4, pagamento.getScadenzaCarta());
            ps.setString(5, pagamento.getIntestatario());
            ps.setInt(6, pagamento.getIdPagamento()); // Condizione WHERE

            ps.executeUpdate();
        }
    }

    // 5. ELIMINA CARTA SALVATA (doDelete)
    public void doDelete(int idPagamento) throws SQLException {
        String sql = "DELETE FROM Dati_pagamento WHERE ID_pagamento = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPagamento);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private DatiPagamentoBean mapResultSetToBean(ResultSet rs) throws SQLException {
        DatiPagamentoBean p = new DatiPagamentoBean();
        p.setIdPagamento(rs.getInt("ID_pagamento"));
        p.setIdUtente(rs.getInt("ID_Utente"));
        p.setCircuitoCarta(rs.getString("Circuito_carta"));
        p.setNumeroCartaOscurato(rs.getString("Numero_carta_oscurato"));
        p.setScadenzaCarta(rs.getDate("Scadenza_carta"));
        p.setIntestatario(rs.getString("Intestatario"));
        return p;
    }
}