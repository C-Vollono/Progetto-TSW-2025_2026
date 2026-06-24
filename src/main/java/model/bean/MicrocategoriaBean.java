package model.bean;

import java.io.Serializable;

public class MicrocategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;

    private int idMicro;       
    private String nomeMicro; 
    private int idMacro;   

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