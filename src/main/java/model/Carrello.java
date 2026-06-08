package model; // Lascialo nel package model subito sopra bean per pulizia MVC

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import model.bean.ProdottoBean; // Importa il tuo fagiolino ufficiale

public class Carrello implements Serializable {
    private static final long serialVersionUID = 1L;

    // La chiave è l'oggetto ProdottoBean, il valore è la quantità scelta nel carrello
    private Map<ProdottoBean, Integer> elementi;

    public Carrello() {
        this.elementi = new LinkedHashMap<>();
    }

    public Map<ProdottoBean, Integer> getElementi() {
        return elementi;
    }

    /**
     * Aggiunge un prodotto al carrello o ne incrementa la quantità se già presente.
     */
    public void aggiungiProdotto(ProdottoBean prodotto, int quantitaRichiesta) {
        ProdottoBean esistente = trovaInCarrello(prodotto.getIdProdotto());
        
        if (esistente != null) {
            int nuovaQuantita = elementi.get(esistente) + quantitaRichiesta;
            // CONTROLLO DI SICUREZZA: Usiamo getQuantita() che è il tuo stock nel DB
            if (nuovaQuantita <= prodotto.getQuantita()) {
                elementi.put(esistente, nuovaQuantita);
            } else {
                elementi.put(esistente, prodotto.getQuantita());
            }
        } else {
            elementi.put(prodotto, quantitaRichiesta);
        }
    }

    /**
     * Modifica direttamente la quantità di un prodotto esistente.
     */
    public void modificaQuantita(int idProdotto, int nuovaQuantita) {
        ProdottoBean esistente = trovaInCarrello(idProdotto);
        if (esistente != null && nuovaQuantita > 0) {
            // CONTROLLO DI SICUREZZA: Usiamo getQuantita() del tuo fagiolino
            if (nuovaQuantita <= esistente.getQuantita()) {
                elementi.put(esistente, nuovaQuantita);
            } else {
                elementi.put(esistente, esistente.getQuantita());
            }
        }
    }

    /**
     * Rimuove un prodotto dal carrello.
     */
    public void rimuoviProdotto(int idProdotto) {
        ProdottoBean esistente = trovaInCarrello(idProdotto);
        if (esistente != null) {
            elementi.remove(esistente);
        }
    }

    /**
     * Calcola il prezzo totale del carrello (somma di prezzo * quantità)
     */
    public double getPrezzoTotale() {
        double totale = 0;
        for (Map.Entry<ProdottoBean, Integer> entry : elementi.entrySet()) {
            totale += entry.getKey().getPrezzo() * entry.getValue();
        }
        return totale;
    }

    /**
     * Metodo di utility interno per trovare un prodotto nella mappa confrontando l'ID
     */
    private ProdottoBean trovaInCarrello(int idProdotto) {
        for (ProdottoBean p : elementi.keySet()) {
            if (p.getIdProdotto() == idProdotto) {
                return p;
            }
        }
        return null;
    }
}