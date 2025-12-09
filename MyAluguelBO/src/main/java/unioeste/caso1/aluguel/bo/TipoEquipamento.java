package unioeste.caso1.aluguel.bo;

import java.io.Serializable;

public class TipoEquipamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTipoEquipamento;
    private String nomeTipoEquipamento;

    public TipoEquipamento() {}

    public TipoEquipamento(int id, String nome) {
        this.idTipoEquipamento = id;
        this.nomeTipoEquipamento = nome;
    }

    public int getIdTipoEquipamento() { return idTipoEquipamento; }
    public void setIdTipoEquipamento(int idTipoEquipamento) { this.idTipoEquipamento = idTipoEquipamento; }

    public String getNomeTipoEquipamento() { return nomeTipoEquipamento; }
    public void setNomeTipoEquipamento(String nomeTipoEquipamento) { this.nomeTipoEquipamento = nomeTipoEquipamento; }

    @Override
    public String toString() {
        return nomeTipoEquipamento;
    }
}