package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importato per gestire Statement.RETURN_GENERATED_KEYS
import java.util.ArrayList;
import java.util.List;
import model.bean.UtenteBean;
import util.ConPool;

public class UtenteDAO {

    // 1. INSERIMENTO / REGISTRAZIONE (Modificato: restituisce boolean e controlla i duplicati)
    public boolean doSave(UtenteBean utente) throws SQLException {
        
        if (checkEmailExists(utente.getEmail())) {
            System.out.println("[UtenteDAO] Tentativo di registrazione fallito: Email già esistente -> " + utente.getEmail());
            return false;
        }
        
        if (checkUsernameExists(utente.getUsername())) {
            System.out.println("[UtenteDAO] Tentativo di registrazione fallito: Username già esistente -> " + utente.getUsername());
            return false;
        }

        String sql = "INSERT INTO Utente (Email, Password, Username, Nome, Cognome, Is_Admin, Data_di_nascita) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getUsername());
            ps.setString(4, utente.getNome());
            ps.setString(5, utente.getCognome());
            ps.setBoolean(6, utente.isIsAdmin());
            ps.setDate(7, utente.getDataDiNascita());

            int rowsAffected = ps.executeUpdate();

        
            if (rowsAffected == 0) {
                return false;
            }

       
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
       
                    utente.setIdUtente(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Errore nella registrazione: nessun ID utente generato restituito.");
                }
            }
            
            return true;
        }
    }

    // 2. RICERCA PER CHIAVE PRIMARIA (ID_Utente)
    public UtenteBean doRetrieveByKey(int idUtente) throws SQLException {
        String sql = "SELECT * FROM Utente WHERE ID_Utente = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 3. LOGIN / AUTENTICAZIONE (Ricerca per Email e Password)
    public UtenteBean doRetrieveByLogin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Utente WHERE Email = ? AND Password = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    // 4. VERIFICA ESISTENZA EMAIL (Evita duplicati in fase di registrazione)
    public boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT ID_Utente FROM Utente WHERE Email = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    // 4b. AGGIUNTO: VERIFICA ESISTENZA USERNAME (Evita duplicati in fase di registrazione)
    public boolean checkUsernameExists(String username) throws SQLException {
        String sql = "SELECT ID_Utente FROM Utente WHERE Username = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    // 5. RECUPERA TUTTI GLI UTENTI (Utile nel pannello Admin)
    public List<UtenteBean> doRetrieveAll() throws SQLException {
        List<UtenteBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Utente";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToBean(rs));
            }
        }
        return lista;
    }

    // 6. AGGIORNAMENTO PROFILO (doUpdate)
    public void doUpdate(UtenteBean utente) throws SQLException {
        String sql = "UPDATE Utente SET Email = ?, Password = ?, Username = ?, Nome = ?, Cognome = ?, Is_Admin = ?, Data_di_nascita = ? WHERE ID_Utente = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getUsername());
            ps.setString(4, utente.getNome());
            ps.setString(5, utente.getCognome());
            ps.setBoolean(6, utente.isIsAdmin());
            ps.setDate(7, utente.getDataDiNascita());
            ps.setInt(8, utente.getIdUtente());

            ps.executeUpdate();
        }
    }

    // 7. ELIMINAZIONE UTENTE
    public void doDelete(int idUtente) throws SQLException {
        String sql = "DELETE FROM Utente WHERE ID_Utente = ?";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ps.executeUpdate();
        }
    }

    // Metodo interno di utility per il mapping ResultSet -> Bean
    private UtenteBean mapResultSetToBean(ResultSet rs) throws SQLException {
        UtenteBean u = new UtenteBean();
        u.setIdUtente(rs.getInt("ID_Utente"));
        u.setEmail(rs.getString("Email"));
        u.setPassword(rs.getString("Password"));
        u.setUsername(rs.getString("Username"));
        u.setNome(rs.getString("Nome"));
        u.setCognome(rs.getString("Cognome"));
        u.setIsAdmin(rs.getBoolean("Is_Admin"));
        u.setDataDiNascita(rs.getDate("Data_di_nascita"));
        return u;
    }
}