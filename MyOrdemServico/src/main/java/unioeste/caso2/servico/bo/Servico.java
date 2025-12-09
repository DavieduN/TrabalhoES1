package unioeste.caso2.servico.bo;

import java.io.Serializable;

public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idServico;
    private String nomeServico;
    private double valorServico;
    private TipoServico tipoServico;

    public Servico() {
        this.tipoServico = new TipoServico();
    }

    public Servico(int idServico, String nomeServico, double valorServico, TipoServico tipoServico) {
        this.idServico = idServico;
        this.nomeServico = nomeServico;
        this.valorServico = valorServico;
        this.tipoServico = tipoServico;
    }

    public int getIdServico() { return idServico; }
    public void setIdServico(int idServico) { this.idServico = idServico; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public double getValorServico() { return valorServico; }
    public void setValorServico(double valorServico) { this.valorServico = valorServico; }

    public TipoServico getTipoServico() { return tipoServico; }
    public void setTipoServico(TipoServico tipoServico) { this.tipoServico = tipoServico; }

    @Override
    public String toString() {
        return nomeServico + " (R$ " + valorServico + ")";
    }
}