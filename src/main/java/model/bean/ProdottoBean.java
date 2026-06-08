package model.bean;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
    // caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
    private static final long serialVersionUID = 1L;

    private int idProdotto; // ID_prodotto INT AUTO_INCREMENT PRIMARY KEY
    private String marca; //  Marca VARCHAR(50) NOT NULL 
    private String modello; // Modello VARCHAR(100) NOT NULL
    private String tipo; // Tipo VARCHAR(50)
    private int quantita; // Quantita INT NOT NULL DEFAULT 0
    private String descrizione; // Descrizione TEXT
    private double prezzo; // Prezzo DECIMAL(10, 2) NOT NULL  
    private int idMicro; // ID_micro INT NOT NULL
    private String urlImmagine; // Url_Immagine VARCHAR(255) NOT NULL
    private int iva; // <--- Aliquota fissa 22% default MySql

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