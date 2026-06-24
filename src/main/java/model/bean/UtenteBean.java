package model.bean;

import java.io.Serializable;
import java.sql.Date;

public class UtenteBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idUtente; 
    private String email; 
    private String password; 
    private String username; 
    private String nome; 
    private String cognome; 
    private boolean isAdmin;
    private Date dataDiNascita; 

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