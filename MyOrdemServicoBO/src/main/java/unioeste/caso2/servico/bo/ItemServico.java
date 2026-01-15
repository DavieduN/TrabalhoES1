package unioeste.caso2.servico.bo;

import java.io.Serializable;

public class ItemServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idItemServico;
    private double valorServico;
    private TipoServico tipoServico;

    public ItemServico() {
        this.tipoServico = new TipoServico();
    }

    public ItemServico(int idItemServico, double valorServico, TipoServico tipoServico) {
        this.idItemServico = idItemServico;
        this.valorServico = valorServico;
        this.tipoServico = tipoServico;
    }

    public int getIdItemServico() { return idItemServico; }
    public void setIdItemServico(int idItemServico) { this.idItemServico = idItemServico; }

    public double getValorServico() { return valorServico; }
    public void setValorServico(double valorServico) { this.valorServico = valorServico; }

    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }

    @Override
    public String toString() {
        return "(R$ " + valorServico + ")";
    }
}