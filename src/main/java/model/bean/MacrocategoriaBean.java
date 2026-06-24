package model.bean;

import java.io.Serializable;

public class MacrocategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idMacro;      
    private String nomeMacro; 

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