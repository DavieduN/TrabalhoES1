package unioeste.caso2.servico.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdemServico;
    private LocalDate dataEmissao;
    private String descricaoProblema;
    private double valorTotal;

    private Cliente cliente;
    private Atendente atendente;

    private List<Servico> listaServicos;

    public OrdemServico() {
        this.cliente = new Cliente();
        this.atendente = new Atendente();
        this.listaServicos = new ArrayList<>();
        this.dataEmissao = LocalDate.now();
    }

    public OrdemServico(int idOrdemServico, LocalDate dataEmissao, String descricaoProblema,
                        double valorTotal, Cliente cliente, Atendente atendente) {
        this.idOrdemServico = idOrdemServico;
        this.dataEmissao = dataEmissao;
        this.descricaoProblema = descricaoProblema;
        this.valorTotal = valorTotal;
        this.cliente = cliente;
        this.atendente = atendente;
        this.listaServicos = new ArrayList<>();
    }


    public void adicionarServico(Servico s) {
        this.listaServicos.add(s);
    }
    public void removerServico(Servico s) {
        this.listaServicos.remove(s);
    }

    public int getIdOrdemServico() { return idOrdemServico; }
    public void setIdOrdemServico(int idOrdemServico) { this.idOrdemServico = idOrdemServico; }

    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }

    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Atendente getAtendente() { return atendente; }
    public void setAtendente(Atendente atendente) { this.atendente = atendente; }

    public List<Servico> getListaServicos() { return listaServicos; }
    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }
}
