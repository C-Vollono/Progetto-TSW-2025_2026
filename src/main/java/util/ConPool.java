package util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConPool {
    private static DataSource dataSource;

    static {
        try {
            // Inizializziamo il contesto JNDI per parlare con Tomcat
            InitialContext context = new InitialContext();
            
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/e_commerce_db");
            System.out.println("[ConPool] Connection Pool JNDI inizializzato con successo tramite Tomcat.");
            
        } catch (NamingException e) {
            System.err.println("Errore critico: Impossibile trovare il DataSource JNDI!");
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource non inizializzato correttamente. Controlla il file context.xml.");
        }

        return dataSource.getConnection(); 
    }
}