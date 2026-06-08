package model.bean;

import java.io.Serializable;

public class DettaglioOrdineBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
    // caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
    private static final long serialVersionUID = 1L;

    private int idOrdine;               // ID_ordine INT NOT NULL
    private int idProdotto;             // ID_prodotto INT NOT NULL
    private int quantita;               // Quantita INT NOT NULL DEFAULT 1
    private double prezzoUnitarioStorico; // Prezzo_unitario_storico DECIMAL(10, 2) NOT NULL
    private int ivaStoricizzata;        // Iva_storicizzata INT NOT NULL DEFAULT 22

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