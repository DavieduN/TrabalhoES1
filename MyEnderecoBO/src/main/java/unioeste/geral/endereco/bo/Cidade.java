package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class Cidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idCidade;
    private String nomeCidade;
    private UnidadeFederativa unidadeFederativa;

    public Cidade() {
        this.unidadeFederativa = new UnidadeFederativa();
    }

    public int getIdCidade() { return idCidade; }
    public void setIdCidade(int idCidade) { this.idCidade = idCidade; }

    public String getNomeCidade() { return nomeCidade; }
    public void setNomeCidade(String nomeCidade) { this.nomeCidade = nomeCidade; }

    public UnidadeFederativa getUnidadeFederativa() { return unidadeFederativa; }
    public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    @Override
    public String toString() { return nomeCidade + " - " + (unidadeFederativa != null ? unidadeFederativa.getSiglaUF() : ""); }
}