package model.bean;

import java.io.Serializable;
import java.sql.Date;

public class UtenteBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idUtente; // ID_Utente INT AUTO_INCREMENT PRIMARY KEY
    private String email; // Email VARCHAR(100) NOT NULL UNIQUE
    private String password; // Password VARCHAR(255) NOT NULL
    private String username; //Username VARCHAR(50) NOT NULL UNIQUE
    private String nome; // Nome VARCHAR(50) NOT NULL
    private String cognome; // Cognome VARCHAR(50) NOT NULL
    private boolean isAdmin; // Is_Admin BOOL DEFAULT false
    private Date dataDiNascita; // Data_di_nascita DATE

    public UtenteBean() {}

    // Getter e Setter
    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public boolean isIsAdmin() { return isAdmin; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public Date getDataDiNascita() { return dataDiNascita; }
    public void setDataDiNascita(Date dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    @Override
    public String toString() {
        return "UtenteBean [idUtente=" + idUtente + ", email=" + email + ", username=" + username + ", nome=" + nome
                + ", cognome=" + cognome + ", isAdmin=" + isAdmin + "]";
    }
}