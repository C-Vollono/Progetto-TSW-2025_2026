package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConPool {
    private static DataSource dataSource;

    // Il blocco static viene eseguito una sola volta all'avvio dell'applicazione
    static {
        try {
            // Inizializziamo il contesto JNDI per parlare con Tomcat
            InitialContext context = new InitialContext();
            
            // Chiediamo a Tomcat di passarci il DataSource (il Pool) configurato nel server
            // NOTA: "jdbc/e_commerce_db" corrisponde esattamente al "name" inserito nel context.xml
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/e_commerce_db");
            System.out.println("[ConPool] Connection Pool JNDI inizializzato con successo tramite Tomcat.");
            
        } catch (NamingException e) {
            System.err.println("Errore critico: Impossibile trovare il DataSource JNDI!");
            e.printStackTrace();
        }
    }

    /**
     * Prende in prestito una connessione già pronta dal Pool di Tomcat.
     * @return java.sql.Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource non inizializzato correttamente. Controlla il file context.xml.");
        }
        // Questo NON apre una nuova connessione da zero! 
        // Riduce i tempi di attesa prelevando un canale già attivo dal pool.
        return dataSource.getConnection(); 
    }
}