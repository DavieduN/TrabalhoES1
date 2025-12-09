package unioeste.caso2.servico.bo;

import java.io.Serializable;

public class TipoServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTipoServico;
    private String nomeTipoServico;

    public TipoServico() {}

    public TipoServico(int id, String nome) {
        this.idTipoServico = id;
        this.nomeTipoServico = nome;
    }

    public int getIdTipoServico() { return idTipoServico; }
    public void setIdTipoServico(int idTipoServico) { this.idTipoServico = idTipoServico; }

    public String getNomeTipoServico() { return nomeTipoServico; }
    public void setNomeTipoServico(String nomeTipoServico) { this.nomeTipoServico = nomeTipoServico; }

    @Override
    public String toString() {
        return nomeTipoServico;
    }
}