package model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class PreferitiBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idUtente;       
    private int idProdotto;     
    private Timestamp dataAggiunta; 

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