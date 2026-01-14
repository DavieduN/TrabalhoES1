package unioeste.caso2.servico.bo;

import java.io.Serializable;

public class ItemServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idServico;
    private double valorServico;
    private TipoServico tipoServico;

    public ItemServico() {
        this.tipoServico = new TipoServico();
    }

    public ItemServico(int idServico, double valorServico, TipoServico tipoServico) {
        this.idServico = idServico;
        this.valorServico = valorServico;
        this.tipoServico = tipoServico;
    }

    public int getIdServico() { return idServico; }
    public void setIdServico(int idServico) { this.idServico = idServico; }

    public double getValorServico() { return valorServico; }
    public void setValorServico(double valorServico) { this.valorServico = valorServico; }

    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }

    @Override
    public String toString() {
        return "(R$ " + valorServico + ")";
    }
}