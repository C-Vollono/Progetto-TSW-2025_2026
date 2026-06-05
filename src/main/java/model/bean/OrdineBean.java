package model.bean;

import java.io.Serializable;
import java.sql.Timestamp; // Usiamo Timestamp visto che sul DB è DATETIME

public class OrdineBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idOrdine; // ID_ordine INT AUTO_INCREMENT PRIMARY KEY
    private int idUtente; // ID_Utente INT NOT NULL
    private Timestamp dataOrdine; // Data_ordine DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    private double totaleOrdine; // Totale_ordine DECIMAL(10, 2) NOT NULL
    private String statoOrdine; // Stato_ordine VARCHAR(30) NOT NULL DEFAULT 'IN_ATTESA'
    
    // Dati Spedizione congelati
    private String spedizioneNomeCognome; // Spedizione_Nome_Cognome VARCHAR(100) NOT NULL
    private String spedizioneVia; // Spedizione_Via VARCHAR(100) NOT NULL
    private String spedizioneNumeroCivico; // Spedizione_Numero_civico VARCHAR(10) NOT NULL
    private String spedizioneCap; // Spedizione_Cap VARCHAR(10) NOT NULL
    private String spedizioneCitta; // Spedizione_Citta VARCHAR(50) NOT NULL
    private String spedizioneProvincia; // Spedizione_Provincia CHAR(2) NOT NULL
    private String spedizioneTelefono; // Spedizione_Telefono VARCHAR(20) NOT NULL
    
    // Dati Pagamento congelati
    private String pagamentoCircuito; // Pagamento_Circuito VARCHAR(50) NOT NULL
    private String pagamentoNumeroCartaOscurato; // Pagamento_Numero_Carta_Oscurato VARCHAR(20) NOT NULL

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