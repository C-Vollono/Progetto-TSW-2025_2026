package model.bean;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
 
    private static final long serialVersionUID = 1L;

    private int idProdotto; 
    private String marca;  
    private String modello;
    private String tipo; 
    private int quantita; 
    private String descrizione; 
    private double prezzo;  
    private int idMicro; 
    private String urlImmagine; 
    private int iva; 
    
    private int valutazione;

    public ProdottoBean() {
    }
    
    // Getter e Setter
    public int getIdProdotto() {return idProdotto;}
    public void setIdProdotto(int idProdotto) {this.idProdotto = idProdotto;}

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public String getModello() {return modello;}
    public void setModello(String modello) {this.modello = modello;}

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public int getQuantita() {return quantita;}
    public void setQuantita(int quantita) {this.quantita = quantita;}

    public String getDescrizione() {return descrizione;}
    public void setDescrizione(String descrizione) {this.descrizione = descrizione;}

    public double getPrezzo() {return prezzo;}
    public void setPrezzo(double prezzo) {this.prezzo = prezzo;}

    public int getIdMicro() {return idMicro;}
    public void setIdMicro(int idMicro) {this.idMicro = idMicro;}

    public String getUrlImmagine() {return urlImmagine;}
    public void setUrlImmagine(String urlImmagine) {this.urlImmagine = urlImmagine;}

    public int getIva() {return iva;}
    public void setIva(int iva) {this.iva = iva;}
    
    public int getValutazione() {return valutazione;}
    public void setValutazione(int valutazione) {this.valutazione = valutazione;}
    
    // Metodo equals per fare l'uguaglianza tra oggetti Prodotto tramite ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProdottoBean other = (ProdottoBean) obj;
        return this.idProdotto == other.idProdotto;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idProdotto);
    }
    
    // Per il Debugging
    @Override
    public String toString() {
        return "Prodotto [idProdotto=" + idProdotto + ", marca=" + marca + ", modello=" + modello 
                + ", tipo=" + tipo + ", quantita=" + quantita + ", descrizione=" + descrizione 
                + ", prezzo=" + prezzo + ", idMicro=" + idMicro + ", urlImmagine=" + urlImmagine 
                + ", iva=" + iva + "]";
    }
}