package unioeste.caso2.servico.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int nroOrdemServico;
    private LocalDate dataEmissao;
    private String descricaoProblema;
    private double valorTotal;

    private Cliente cliente;
    private Atendente atendente;

    private List<ItemServico> listaItens;

    public OrdemServico() {
        this.cliente = new Cliente();
        this.atendente = new Atendente();
        this.listaItens = new ArrayList<>();
        this.dataEmissao = LocalDate.now();
    }

    public OrdemServico(int nroOrdemServico, LocalDate dataEmissao, String descricaoProblema,
                        double valorTotal, Cliente cliente, Atendente atendente) {
        this.nroOrdemServico = nroOrdemServico;
        this.dataEmissao = dataEmissao;
        this.descricaoProblema = descricaoProblema;
        this.valorTotal = valorTotal;
        this.cliente = cliente;
        this.atendente = atendente;
        this.listaItens = new ArrayList<>();
    }

    public void adicionarItem(ItemServico item) {
        this.listaItens.add(item);
    }
    
    public void removerItem(ItemServico item) {
        this.listaItens.remove(item);
    }

    public int getNroOrdemServico() { return nroOrdemServico; }
    public void setNroOrdemServico(int nroOrdemServico) { this.nroOrdemServico = nroOrdemServico; }

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

    public List<ItemServico> getListaItens() { return listaItens; }
    public void setListaItens(List<ItemServico> listaItens) {
        this.listaItens = listaItens;
    }
}
