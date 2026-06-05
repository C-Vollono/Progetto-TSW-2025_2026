package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.bean.MacrocategoriaBean;
import util.ConPool;

public class MacrocategoriaDAO {

	// 1. INSERISCI UN RECORD DI MACROCATEGORIA (doSave)
    public void doSave(MacrocategoriaBean macro) throws SQLException {
        String sql = "INSERT INTO Macrocategoria (Nome_macro) VALUES (?)";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, macro.getNomeMacro());
            ps.executeUpdate();
        }
    }

    // 2. RECUPERA UNA MACROCATEGORIA IN BASE ALLA CHIAVE (doRetrieveByKey)
    public MacrocategoriaBean doRetrieveByKey(int idMacro) throws SQLException {
        String sql = "SELECT * FROM Macrocategoria WHERE ID_macro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMacro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. RESTITUISCE TUTTE LE MACROCATEGORIE (doRetrieveAll)
    public List<MacrocategoriaBean> doRetrieveAll() throws SQLException {
        List<MacrocategoriaBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Macrocategoria";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 4. MODIFICA UNA MACROCATEGORIA PER CHIAVE (doUpdate)
    public void doUpdate(MacrocategoriaBean macro) throws SQLException {
        String sql = "UPDATE Macrocategoria SET Nome_macro = ? WHERE ID_macro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, macro.getNomeMacro());
            ps.setInt(2, macro.getIdMacro());
            ps.executeUpdate();
        }
    }

    // 5. ELIMINA UNA MACROCATEGORIA PER LA CHIAVE (doDelete)
    public void doDelete(int idMacro) throws SQLException {
        String sql = "DELETE FROM Macrocategoria WHERE ID_macro = ?";
        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMacro);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private MacrocategoriaBean mapResultSetToBean(ResultSet rs) throws SQLException {
        MacrocategoriaBean m = new MacrocategoriaBean();
        m.setIdMacro(rs.getInt("ID_macro"));
        m.setNomeMacro(rs.getString("Nome_macro"));
        return m;
    }
}