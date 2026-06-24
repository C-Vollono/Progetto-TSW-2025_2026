package model.bean;

import java.io.Serializable;
import java.sql.Timestamp; // Usiamo Timestamp visto che sul DB è DATETIME

public class OrdineBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idOrdine; 
    private int idUtente;
    private Timestamp dataOrdine; 
    private double totaleOrdine; 
    private String statoOrdine; 
    
    private String spedizioneNomeCognome; 
    private String spedizioneVia; 
    private String spedizioneNumeroCivico; 
    private String spedizioneCap; 
    private String spedizioneCitta; 
    private String spedizioneProvincia; 
    private String spedizioneTelefono; 
    
    private String pagamentoCircuito; 
    private String pagamentoNumeroCartaOscurato; 

    public OrdineBean() {}

    // Getter e Setter
    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public Timestamp getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }

    public double getTotaleOrdine() { return totaleOrdine; }
    public void setTotaleOrdine(double totaleOrdine) { this.totaleOrdine = totaleOrdine; }

    public String getStatoOrdine() { return statoOrdine; }
    public void setStatoOrdine(String statoOrdine) { this.statoOrdine = statoOrdine; }

    public String getSpedizioneNomeCognome() { return spedizioneNomeCognome; }
    public void setSpedizioneNomeCognome(String spedizioneNomeCognome) { this.spedizioneNomeCognome = spedizioneNomeCognome; }

    public String getSpedizioneVia() { return spedizioneVia; }
    public void setSpedizioneVia(String spedizioneVia) { this.spedizioneVia = spedizioneVia; }

    public String getSpedizioneNumeroCivico() { return spedizioneNumeroCivico; }
    public void setSpedizioneNumeroCivico(String spedizioneNumeroCivico) { this.spedizioneNumeroCivico = spedizioneNumeroCivico; }

    public String getSpedizioneCap() { return spedizioneCap; }
    public void setSpedizioneCap(String spedizioneCap) { this.spedizioneCap = spedizioneCap; }

    public String getSpedizioneCitta() { return spedizioneCitta; }
    public void setSpedizioneCitta(String spedizioneCitta) { this.spedizioneCitta = spedizioneCitta; }

    public String getSpedizioneProvincia() { return spedizioneProvincia; }
    public void setSpedizioneProvincia(String spedizioneProvincia) { this.spedizioneProvincia = spedizioneProvincia; }

    public String getSpedizioneTelefono() { return spedizioneTelefono; }
    public void setSpedizioneTelefono(String spedizioneTelefono) { this.spedizioneTelefono = spedizioneTelefono; }

    public String getPagamentoCircuito() { return pagamentoCircuito; }
    public void setPagamentoCircuito(String pagamentoCircuito) { this.pagamentoCircuito = pagamentoCircuito; }

    public String getPagamentoNumeroCartaOscurato() { return pagamentoNumeroCartaOscurato; }
    public void setPagamentoNumeroCartaOscurato(String pagamentoNumeroCartaOscurato) { this.pagamentoNumeroCartaOscurato = pagamentoNumeroCartaOscurato; }

    @Override
    public String toString() {
        return "OrdineBean [idOrdine=" + idOrdine + ", idUtente=" + idUtente + ", totale=" + totaleOrdine + "€, stato=" + statoOrdine + "]";
    }
}