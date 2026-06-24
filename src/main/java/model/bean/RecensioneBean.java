package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class RecensioneBean implements Serializable {
 
	private static final long serialVersionUID = 1L;

    private int idRecensione;       
    private int idUtente;        
    private int idProdotto;          
    private int valutazione;    
    private String titolo;           
    private String corpo;             
    private Timestamp dataRecensione;  
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