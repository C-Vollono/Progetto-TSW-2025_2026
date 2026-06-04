package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class PreferitiBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idUtente;       // ID_Utente INT NOT NULL
    private int idProdotto;     // ID_prodotto INT NOT NULL
    private Timestamp dataAggiunta; // Data_aggiunta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

    public PreferitiBean() {}

    // Getter e Setter
    public int getIdUtente() {return idUtente;}
    public void setIdUtente(int idUtente) {this.idUtente = idUtente;}

    public int getIdProdotto() {return idProdotto;}
    public void setIdProdotto(int idProdotto) {this.idProdotto = idProdotto;}

    public Timestamp getDataAggiunta() {return dataAggiunta;}
    public void setDataAggiunta(Timestamp dataAggiunta) {this.dataAggiunta = dataAggiunta;}

    @Override
    public String toString() {
        return "PreferitiBean [idUtente=" + idUtente + ", idProdotto=" + idProdotto + ", dataAggiunta=" + dataAggiunta + "]";
    }
}