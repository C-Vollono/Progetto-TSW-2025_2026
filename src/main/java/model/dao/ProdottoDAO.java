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

    private static final int IVA_DEFAULT = 22;

    // 1. RECUPERA TUTTI I PRODOTTI (Escludendo quelli con Quantità = 0 per il catalogo principale)
    public List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();

        String sql = "SELECT * FROM Prodotto WHERE Quantita > 0";

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

    // 5. ELIMINA FISICAMENTE SE ISOLATO O PORTA A 0 LA QUANTITÀ SE COLLEGATO
    public void doDelete(int idProdotto) throws SQLException {
        // Query per verificare se il prodotto è collegato ad altre tabelle
        String sqlCheckLegami = "SELECT " +
                "(SELECT COUNT(*) FROM Dettaglio_Ordine WHERE ID_prodotto = ?) + " +
                "(SELECT COUNT(*) FROM Recensione WHERE ID_prodotto = ?) + " +
                "(SELECT COUNT(*) FROM Preferiti WHERE ID_prodotto = ?) AS TotaleLegami";

        // Caso A: Soft Delete (il prodotto ha uno storico -> portiamo la quantità a 0)
        String sqlSoftDelete = "UPDATE Prodotto SET Quantita = 0 WHERE ID_prodotto = ?";

        // Caso B: Hard Delete (il prodotto è isolato -> cancellazione fisica e pulizia orfani)
        String sqlGetIds = "SELECT p.ID_micro, m.ID_macro FROM Prodotto p " +
                           "JOIN Microcategoria m ON p.ID_micro = m.ID_micro WHERE p.ID_prodotto = ?";
        String sqlDeleteProdotto = "DELETE FROM Prodotto WHERE ID_prodotto = ?";
        String sqlCountMicro = "SELECT COUNT(*) FROM Prodotto WHERE ID_micro = ?";
        String sqlDeleteMicro = "DELETE FROM Microcategoria WHERE ID_micro = ?";
        String sqlCountMacro = "SELECT COUNT(*) FROM Microcategoria WHERE ID_macro = ?";
        String sqlDeleteMacro = "DELETE FROM Macrocategoria WHERE ID_macro = ?";

        Connection con = null;
        try {
            con = ConPool.getConnection();
            con.setAutoCommit(false); 

            int totaleLegami = 0;
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckLegami)) {
                psCheck.setInt(1, idProdotto);
                psCheck.setInt(2, idProdotto);
                psCheck.setInt(3, idProdotto);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        totaleLegami = rs.getInt("TotaleLegami");
                    }
                }
            }

            if (totaleLegami > 0) {
                // IL PRODOTTO HA STORICO: Lo nascondiamo azzerando la quantità
                try (PreparedStatement psSoft = con.prepareStatement(sqlSoftDelete)) {
                    psSoft.setInt(1, idProdotto);
                    psSoft.executeUpdate();
                }
            } else {
                // IL PRODOTTO È ISOLATO: Rimozione fisica dal DB e controllo categorie
                int idMicro = -1;
                int idMacro = -1;

                try (PreparedStatement psGet = con.prepareStatement(sqlGetIds)) {
                    psGet.setInt(1, idProdotto);
                    try (ResultSet rs = psGet.executeQuery()) {
                        if (rs.next()) {
                            idMicro = rs.getInt("ID_micro");
                            idMacro = rs.getInt("ID_macro");
                        }
                    }
                }

                if (idMicro != -1) {
                    // 1. Cancella il prodotto
                    try (PreparedStatement psDelProd = con.prepareStatement(sqlDeleteProdotto)) {
                        psDelProd.setInt(1, idProdotto);
                        psDelProd.executeUpdate();
                    }

                    // 2. Controlla se la Microcategoria è rimasta vuota
                    boolean microEliminata = false;
                    try (PreparedStatement psCountMicro = con.prepareStatement(sqlCountMicro)) {
                        psCountMicro.setInt(1, idMicro);
                        try (ResultSet rs = psCountMicro.executeQuery()) {
                            if (rs.next() && rs.getInt(1) == 0) {
                                try (PreparedStatement psDelMicro = con.prepareStatement(sqlDeleteMicro)) {
                                    psDelMicro.setInt(1, idMicro);
                                    psDelMicro.executeUpdate();
                                    microEliminata = true;
                                }
                            }
                        }
                    }

                    // 3. Controlla se la Macrocategoria è rimasta vuota
                    if (microEliminata) {
                        try (PreparedStatement psCountMacro = con.prepareStatement(sqlCountMacro)) {
                            psCountMacro.setInt(1, idMacro);
                            try (ResultSet rs = psCountMacro.executeQuery()) {
                                if (rs.next() && rs.getInt(1) == 0) {
                                    try (PreparedStatement psDelMacro = con.prepareStatement(sqlDeleteMacro)) {
                                        psDelMacro.setInt(1, idMacro);
                                        psDelMacro.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            con.commit(); 
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    // 6. SCALA LA QUANTITÀ IN MAGAZZINO DOPO UN ACQUISTO
    public void doUpdateQuantita(int idProdotto, int quantitaAcquistata, Connection con) throws SQLException {
        String sql = "UPDATE Prodotto SET Quantita = Quantita - ? WHERE ID_prodotto = ?";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantitaAcquistata);
            ps.setInt(2, idProdotto);
            ps.executeUpdate();
        }
    }

    // 7. RECUPERA PRODOTTI DI UNA SPECIFICA MICROCATEGORIA (Filtrando quelli attivi)
    public List<ProdottoBean> doRetrieveByMicrocategoria(int idMicro) throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM Prodotto WHERE ID_micro = ? AND Quantita > 0";

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
        String sql = "SELECT * FROM Prodotto WHERE Quantita > 0 ORDER BY RAND() LIMIT 3";

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
        String sql = "SELECT * FROM Prodotto WHERE (CONCAT(Marca, ' ', Modello) LIKE ? OR CONCAT(Modello, ' ', Marca) LIKE ?) AND Quantita > 0 LIMIT 5";

        try (Connection con = ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String keyword = "%" + query.trim() + "%";
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
    
 // 10. RECUPERA I PRODOTTI FILTRATI (Accetta ora includeEsauriti per differenziare l'uso lato Client o Admin)
    public List<ProdottoBean> doRetrieveByFilters(String macroIdParam, String microIdParam, String marca, String prezzoRange, String searchQuery, String ordina, boolean includeEsauriti) throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
     
        String baseQuery = "SELECT p.* FROM Prodotto p JOIN Microcategoria mi ON p.ID_micro = mi.ID_micro WHERE " 
                         + (includeEsauriti ? "1=1" : "p.Quantita > 0");
        
        StringBuilder sqlBuilder = new StringBuilder(baseQuery);
        List<Object> parametri = new ArrayList<>();

        // 1. Filtro Microcategoria
        if (microIdParam != null && !microIdParam.equalsIgnoreCase("All") && !microIdParam.trim().isEmpty()) {
            sqlBuilder.append(" AND p.ID_micro = ?");
            parametri.add(Integer.parseInt(microIdParam));
        } 
        // 2. Filtro Macrocategoria
        else if (macroIdParam != null && !macroIdParam.equalsIgnoreCase("All") && !macroIdParam.trim().isEmpty()) {
            sqlBuilder.append(" AND mi.ID_macro = ?");
            parametri.add(Integer.parseInt(macroIdParam));
        }

        // 3. Filtro Marca
        if (marca != null && !marca.equalsIgnoreCase("All")) {
            sqlBuilder.append(" AND p.Marca = ?");
            parametri.add(marca);
        }

        // 4. Filtro Barra di ricerca testuale
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            sqlBuilder.append(" AND (CONCAT(p.Marca, ' ', p.Modello) LIKE ? OR CONCAT(p.Modello, ' ', p.Marca) LIKE ? OR p.Descrizione LIKE ?)");
            String keyword = "%" + searchQuery.trim() + "%";
            parametri.add(keyword); 
            parametri.add(keyword); 
            parametri.add(keyword);
        }

        // 5. Filtro Fasce di prezzo
        if (prezzoRange != null && !prezzoRange.equalsIgnoreCase("All")) {
            if (prezzoRange.equals("0-500")) {
                sqlBuilder.append(" AND p.Prezzo BETWEEN 0 AND 500");
            } else if (prezzoRange.equals("500-2000")) {
                sqlBuilder.append(" AND p.Prezzo BETWEEN 500 AND 2000");
            } else if (prezzoRange.equals("2000-10000")) {
                sqlBuilder.append(" AND p.Prezzo BETWEEN 2000 AND 10000");
            }
        }

        // 6. Ordinamento topbar
        if (ordina != null) {
            if (ordina.equals("prezzo_asc")) {
                sqlBuilder.append(" ORDER BY p.Prezzo ASC");
            } else if (ordina.equals("prezzo_desc")) {
                sqlBuilder.append(" ORDER BY p.Prezzo DESC");
            } else {
                sqlBuilder.append(" ORDER BY p.ID_prodotto DESC");
            }
        }

        try (Connection con = util.ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlBuilder.toString())) {
            
            for (int i = 0; i < parametri.size(); i++) {
                ps.setObject(i + 1, parametri.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToBean(rs));
                }
            }
        }
        return lista;
    }    
    // 11. RECUPERA TUTTE LE MARCHE (Solo dei prodotti visibili nel catalogo)
    public List<String> doRetrieveAllMarche() throws SQLException {
        List<String> marche = new ArrayList<>();
        String sql = "SELECT DISTINCT Marca FROM Prodotto WHERE Quantita > 0 ORDER BY Marca ASC";
        
        try (Connection con = util.ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                marche.add(rs.getString("Marca"));
            }
        }
        return marche;
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
        
        p.setIva(IVA_DEFAULT); 
        
        int mediaValutazione = 0;
        String sqlMedia = "SELECT COALESCE(ROUND(AVG(Valutazione)), 0) AS Media FROM Recensione WHERE ID_prodotto = ?";
        
        try (Connection con = util.ConPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlMedia)) {
            
            ps.setInt(1, p.getIdProdotto());
            
            try (ResultSet rsMedia = ps.executeQuery()) {
                if (rsMedia.next()) {
                    mediaValutazione = rsMedia.getInt("Media");
                }
            }
        }
        
        p.setValutazione(mediaValutazione);
        return p;
    }
}