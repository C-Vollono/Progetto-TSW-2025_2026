package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class TicketBean implements Serializable {
 
	private static final long serialVersionUID = 1L;

    private int idTicket;           
    private int idUtente;         
    private Timestamp dataApertura; 
    private String descrizione;    
    private String allegato;    
    private String stato;        

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