package model.bean;

import java.io.Serializable;

public class DatiSpedizioneBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idSpedizione;   
    private int idUtente;      
    private String telefono;  
    private String numeroCivico;  
    private String via;         
    private String cap;         
    private String citta;       
    private String provincia;    


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