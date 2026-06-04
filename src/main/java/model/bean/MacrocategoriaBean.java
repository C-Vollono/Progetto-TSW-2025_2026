package model.bean;

import java.io.Serializable;

public class MacrocategoriaBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idMacro;       // ID_macro INT AUTO_INCREMENT PRIMARY KEY
    private String nomeMacro;  // Nome_macro VARCHAR(50)

    public MacrocategoriaBean() {}
    
    // Getter e Setter
    public int getIdMacro() { return idMacro; }
    public void setIdMacro(int idMacro) { this.idMacro = idMacro; }

    public String getNomeMacro() { return nomeMacro; }
    public void setNomeMacro(String nomeMacro) { this.nomeMacro = nomeMacro; }

    @Override
    public String toString() {
        return "MacrocategoriaBean [idMacro=" + idMacro + ", nomeMacro=" + nomeMacro + "]";
    }
}