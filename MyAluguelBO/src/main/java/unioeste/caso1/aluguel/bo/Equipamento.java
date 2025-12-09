package unioeste.caso1.aluguel.bo;

import java.io.Serializable;

public class Equipamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEquipamento;
    private String nomeEquipamento;
    private double valorDiaria;

    private TipoEquipamento tipoEquipamento;

    public Equipamento() {
        this.tipoEquipamento = new TipoEquipamento();
    }

    public int getIdEquipamento() { return idEquipamento; }
    public void setIdEquipamento(int idEquipamento) { this.idEquipamento = idEquipamento; }

    public String getNomeEquipamento() { return nomeEquipamento; }
    public void setNomeEquipamento(String nomeEquipamento) { this.nomeEquipamento = nomeEquipamento; }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    public TipoEquipamento getTipoEquipamento() { return tipoEquipamento; }
    public void setTipoEquipamento(TipoEquipamento tipoEquipamento) { this.tipoEquipamento = tipoEquipamento; }

    @Override
    public String toString() {
        return nomeEquipamento + " (" + tipoEquipamento + ")";
    }
}