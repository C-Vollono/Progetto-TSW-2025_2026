package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

// TEST DATABASE
public class Test_Connection {
    public static void main(String[] args) {
        System.out.println("Tentativo di connessione al database e_commerce_db...");
        
        // Il try-with-resources apre la connessione e la chiude automaticamente alla fine del blocco
        try (Connection con = ConPool.getConnection()) {
            
            // Se il programma arriva qui senza lanciare eccezioni, la connessione è riuscita!
            if (con != null && !con.isClosed()) {
                System.out.println("=========================================");
                System.out.println(" CONNESSI CON SUCCESSO AL SERVER MYSQL! ");
                System.out.println(" Catalogo attivo: " + con.getCatalog());
                System.out.println("=========================================");
            }
            
        } catch (SQLException e) {
            // Se qualcosa va storto, Java cattura l'errore SQL e lo stampa qui
            System.err.println("=========================================");
            System.err.println("  ERRORE DI CONNESSIONE AL DATABASE!    ");
            System.err.println("=========================================");
            System.err.println("Codice di errore SQL: " + e.getErrorCode());
            System.err.println("Stato SQL: " + e.getSQLState());
            System.err.println("Messaggio dettagliato: " + e.getMessage());
            e.printStackTrace();
        }
    }
}