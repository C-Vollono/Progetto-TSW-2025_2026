package model.bean;

import java.io.Serializable;

public class MicrocategoriaBean implements Serializable {
    // Numero di versione per la serializzazione, serve per non far crashare Tomcat in 
	// caso finisca la RAM, per ricordare la classe anche se si eseguono modifiche 
	private static final long serialVersionUID = 1L;

    private int idMicro;       // ID_micro INT AUTO_INCREMENT PRIMARY KEY
    private String nomeMicro;  // Nome_micro VARCHAR(50)
    private int idMacro;       // ID_macro INT NOT NULL (Foreign Key)

    public MicrocategoriaBean() {}
    
    // Getter e Setter
    public int getIdMicro() { return idMicro; }
    public void setIdMicro(int idMicro) { this.idMicro = idMicro; }

    public String getNomeMicro() { return nomeMicro; }
    public void setNomeMicro(String nomeMicro) { this.nomeMicro = nomeMicro; }

    public int getIdMacro() { return idMacro; }
    public void setIdMacro(int idMacro) { this.idMacro = idMacro; }

    @Override
    public String toString() {
        return "MicrocategoriaBean [idMicro=" + idMicro + ", nomeMicro=" + nomeMicro + ", idMacro=" + idMacro + "]";
    }
}