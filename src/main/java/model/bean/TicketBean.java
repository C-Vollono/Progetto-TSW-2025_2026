package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class TicketBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idTicket;            // ID_ticket INT AUTO_INCREMENT PRIMARY KEY
    private int idUtente;            // ID_Utente INT NOT NULL
    private Timestamp dataApertura;  // Data_apertura DATETIME NOT NULL
    private String descrizione;      // Descrizione TEXT NOT NULL
    private String allegato;         // Allegato VARCHAR(255)
    private String stato;            // Stato VARCHAR(30) NOT NULL

    public TicketBean() {}

    // Getter e Setter
    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public Timestamp getDataApertura() { return dataApertura; }
    public void setDataApertura(Timestamp dataApertura) { this.dataApertura = dataApertura; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getAllegato() { return allegato; }
    public void setAllegato(String allegato) { this.allegato = allegato; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    @Override
    public String toString() {
        return "TicketBean [idTicket=" + idTicket + ", idUtente=" + idUtente + ", dataApertura=" + dataApertura 
                + ", descrizione=" + descrizione + ", allegato=" + allegato + ", stato=" + stato + "]";
    }
}