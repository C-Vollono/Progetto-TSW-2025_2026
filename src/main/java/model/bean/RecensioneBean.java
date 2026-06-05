package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class RecensioneBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idRecensione;          // ID_recensione INT AUTO_INCREMENT PRIMARY KEY
    private int idUtente;              // ID_Utente INT NOT NULL
    private int idProdotto;            // ID_prodotto INT NOT NULL
    private int valutazione;           // Valutazione INT NOT NULL (CHECK BETWEEN 1 AND 5)
    private String titolo;             // Titolo VARCHAR(100)
    private String corpo;              // Corpo TEXT NOT NULL
    private Timestamp dataRecensione;  // Data_recensione DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

    public RecensioneBean() {}

    // Getter e Setter
    public int getIdRecensione() { return idRecensione; }
    public void setIdRecensione(int idRecensione) { this.idRecensione = idRecensione; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public int getIdProdotto() { return idProdotto; }
    public void setIdProdotto(int idProdotto) { this.idProdotto = idProdotto; }

    public int getValutazione() { return valutazione; }
    public void setValutazione(int valutazione) { this.valutazione = valutazione; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getCorpo() { return corpo; }
    public void setCorpo(String corpo) { this.corpo = corpo; }

    public Timestamp getDataRecensione() { return dataRecensione; }
    public void setDataRecensione(Timestamp dataRecensione) { this.dataRecensione = dataRecensione; }

    @Override
    public String toString() {
        return "RecensioneBean [idRecensione=" + idRecensione + ", utente=" + idUtente + ", prodotto=" + idProdotto 
                + ", voto=" + valutazione + "/5, titolo=" + titolo + "]";
    }
}