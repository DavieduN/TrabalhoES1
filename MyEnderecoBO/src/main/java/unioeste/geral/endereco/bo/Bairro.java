package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class Bairro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idBairro;
    private String nomeBairro;

    public Bairro() {}

    public int getIdBairro() { return idBairro; }
    public void setIdBairro(int idBairro) { this.idBairro = idBairro; }

    public String getNomeBairro() { return nomeBairro; }
    public void setNomeBairro(String nomeBairro) { this.nomeBairro = nomeBairro; }

    @Override
    public String toString() { return nomeBairro; }
}