package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class TipoLogradouro implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idTipoLogradouro;
    private String nomeTipoLogradouro;

    public TipoLogradouro() {
    }

    public TipoLogradouro(int id, String nome) {
        this.idTipoLogradouro = id;
        this.nomeTipoLogradouro = nome;
    }

    public int getIdTipoLogradouro() {
        return idTipoLogradouro;
    }

    public void setIdTipoLogradouro(int idTipoLogradouro) {
        this.idTipoLogradouro = idTipoLogradouro;
    }

    public String getNomeTipoLogradouro() {
        return nomeTipoLogradouro;
    }

    public void setNomeTipoLogradouro(String nomeTipoLogradouro) {
        this.nomeTipoLogradouro = nomeTipoLogradouro;
    }

    @Override
    public String toString() {
        return nomeTipoLogradouro;
    }
}