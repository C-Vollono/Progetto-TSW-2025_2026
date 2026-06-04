package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class PraticaBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idPratica;               // ID_pratica INT AUTO_INCREMENT PRIMARY KEY
    private int idTicket;                // ID_ticket INT NOT NULL UNIQUE
    private String interventiPrevisti;   // Interventi_previsti TEXT
    private double costoRiparazione;     // Costo_riparazione DECIMAL(10, 2) NOT NULL DEFAULT 0.00
    private Timestamp dataCompletamento; // Data_completamento DATETIME NULL

    public PraticaBean() {}

    // Getter e Setter
    public int getIdPratica() { return idPratica; }
    public void setIdPratica(int idPratica) { this.idPratica = idPratica; }

    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public String getInterventiPrevisti() { return interventiPrevisti; }
    public void setInterventiPrevisti(String interventiPrevisti) { this.interventiPrevisti = interventiPrevisti; }

    public double getCostoRiparazione() { return costoRiparazione; }
    public void setCostoRiparazione(double costoRiparazione) { this.costoRiparazione = costoRiparazione; }

    public Timestamp getDataCompletamento() { return dataCompletamento; }
    public void setDataCompletamento(Timestamp dataCompletamento) { this.dataCompletamento = dataCompletamento; }

    @Override
    public String toString() {
        return "PraticaBean [idPratica=" + idPratica + ", idTicket=" + idTicket 
                + ", costoRiparazione=" + costoRiparazione + "€, dataCompletamento=" + dataCompletamento + "]";
    }
}