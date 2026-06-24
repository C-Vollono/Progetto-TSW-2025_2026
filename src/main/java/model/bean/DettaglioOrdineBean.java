package model.bean;

import java.io.Serializable;

public class DettaglioOrdineBean implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private int idOrdine;              
    private int idProdotto;        
    private int quantita;           
    private double prezzoUnitarioStorico; 
    private int ivaStoricizzata;       

    public DettaglioOrdineBean() {}

    // Getter e Setter
    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

    public int getIdProdotto() { return idProdotto; }
    public void setIdProdotto(int idProdotto) { this.idProdotto = idProdotto; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public double getPrezzoUnitarioStorico() { return prezzoUnitarioStorico; }
    public void setPrezzoUnitarioStorico(double prezzoUnitarioStorico) { this.prezzoUnitarioStorico = prezzoUnitarioStorico; }

    public int getIvaStoricizzata() { return ivaStoricizzata; }
    public void setIvaStoricizzata(int ivaStoricizzata) { this.ivaStoricizzata = ivaStoricizzata; }

    @Override
    public String toString() {
        return "DettaglioOrdineBean [idOrdine=" + idOrdine + ", idProdotto=" + idProdotto 
                + ", quantita=" + quantita + ", prezzoUnitarioStorico=" + prezzoUnitarioStorico 
                + ", ivaStoricizzata=" + ivaStoricizzata + "]";
    }
}