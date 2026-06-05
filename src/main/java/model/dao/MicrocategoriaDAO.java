package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.MicrocategoriaBean;
import util.ConPool;

public class MicrocategoriaDAO {

	// 1. INSERISCI UN RECORD DI MICROCATEGORIA (doSave)
    public void doSave(MicrocategoriaBean micro) throws SQLException {
        String sql = "INSERT INTO Microcategoria (Nome_micro, ID_macro) VALUES (?, ?)";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, micro.getNomeMicro());
            ps.setInt(2, micro.getIdMacro());
            ps.executeUpdate();
        }
    }

    // 2. RECUPERA UNA MICROCATEGORIA IN BASE ALLA CHIAVE (doRetrieveByKey)
    public MicrocategoriaBean doRetrieveByKey(int idMicro) throws SQLException {
        String sql = "SELECT * FROM Microcategoria WHERE ID_micro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMicro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. ESTRARRE MICROCATECORIE IN BASE ALLE MACROCATEGORIE (doRetrieveByMacro) 
    public List<MicrocategoriaBean> doRetrieveByMacro(int idMacro) throws SQLException {
        List<MicrocategoriaBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Microcategoria WHERE ID_macro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMacro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }
    
    // 4. RESTITUISCE TUTTE LE MICROCATEGORIE (doRetrieveAll)
    public List<MicrocategoriaBean> doRetrieveAll() throws SQLException {
        List<MicrocategoriaBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Microcategoria";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }
    
    // 5. MODIFICA UNA MICROCATEGORIA PER CHIAVE (doUpdate)
    public void doUpdate(MicrocategoriaBean micro) throws SQLException {
        String sql = "UPDATE Microcategoria SET Nome_micro = ?, ID_macro = ? WHERE ID_micro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, micro.getNomeMicro());
            ps.setInt(2, micro.getIdMacro());
            ps.setInt(3, micro.getIdMicro());
            ps.executeUpdate();
        }
    }
    
    // 6. ELIMINA UNA MICROCATEGORIA PER LA CHIAVE (doDelete)
    public void doDelete(int idMicro) throws SQLException {
        String sql = "DELETE FROM Microcategoria WHERE ID_micro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMicro);
            ps.executeUpdate();
        }
    }
    
    // Metodo interno di utility per il mapping ResultSet -> Bean
    private MicrocategoriaBean mapResultSetToBean(ResultSet rs) throws SQLException {
        MicrocategoriaBean m = new MicrocategoriaBean();
        m.setIdMicro(rs.getInt("ID_micro"));
        m.setNomeMicro(rs.getString("Nome_micro"));
        m.setIdMacro(rs.getInt("ID_macro"));
        return m;
    }
}