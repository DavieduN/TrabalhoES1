package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class Logradouro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idLogradouro;
    private String nomeLogradouro;
    private TipoLogradouro tipoLogradouro;

    public Logradouro() {
        this.tipoLogradouro = new TipoLogradouro();
    }

    public int getIdLogradouro() { return idLogradouro; }
    public void setIdLogradouro(int idLogradouro) { this.idLogradouro = idLogradouro; }

    public String getNomeLogradouro() { return nomeLogradouro; }
    public void setNomeLogradouro(String nomeLogradouro) { this.nomeLogradouro = nomeLogradouro; }

    public TipoLogradouro getTipoLogradouro() { return tipoLogradouro; }
    public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    @Override
    public String toString() {
        String tipo = (tipoLogradouro != null) ? tipoLogradouro.getNomeTipoLogradouro() : "";
        return tipo + " " + nomeLogradouro;
    }
}