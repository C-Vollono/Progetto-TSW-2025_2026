package model.bean;

import java.io.Serializable;
import java.sql.Date;

public class DatiPagamentoBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idPagamento;
    private int idUtente;             
    private String circuitoCarta;        
    private String numeroCartaOscurato;  
    private Date scadenzaCarta;         
    private String intestatario;     

    public DatiPagamentoBean() {}

    // Getter e Setter
    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getCircuitoCarta() { return circuitoCarta; }
    public void setCircuitoCarta(String circuitoCarta) { this.circuitoCarta = circuitoCarta; }

    public String getNumeroCartaOscurato() { return numeroCartaOscurato; }
    public void setNumeroCartaOscurato(String numeroCartaOscurato) { this.numeroCartaOscurato = numeroCartaOscurato; }

    public Date getScadenzaCarta() { return scadenzaCarta; }
    public void setScadenzaCarta(Date scadenzaCarta) { this.scadenzaCarta = scadenzaCarta; }

    public String getIntestatario() { return intestatario; }
    public void setIntestatario(String intestatario) { this.intestatario = intestatario; }

    @Override
    public String toString() {
        return "DatiPagamentoBean [idPagamento=" + idPagamento + ", idUtente=" + idUtente + ", circuito=" + circuitoCarta
                + ", carta=" + numeroCartaOscurato + "]";
    }
}