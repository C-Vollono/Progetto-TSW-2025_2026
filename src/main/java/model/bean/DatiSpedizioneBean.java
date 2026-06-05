package model.bean;

import java.io.Serializable;

public class DatiSpedizioneBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idSpedizione;    // ID_spedizione INT AUTO_INCREMENT PRIMARY KEY
    private int idUtente;         // ID_Utente INT NOT NULL (Foreign Key)
    private String telefono;      // Telefono VARCHAR(20)
    private String numeroCivico;  // Numero_civico VARCHAR(10)
    private String via;           // Via VARCHAR(100)
    private String cap;           // Cap VARCHAR(10)
    private String citta;         // Citta VARCHAR(50)
    private String provincia;     // Provincia CHAR(2)


    public DatiSpedizioneBean() {}

    // Getter e Setter
    public int getIdSpedizione() { return idSpedizione; }
    public void setIdSpedizione(int idSpedizione) { this.idSpedizione = idSpedizione; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNumeroCivico() { return numeroCivico; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    @Override
    public String toString() {
        return "DatiSpedizioneBean [idSpedizione=" + idSpedizione + ", idUtente=" + idUtente + ", citta=" + citta
                + ", provincia=" + provincia + "]";
    }
}