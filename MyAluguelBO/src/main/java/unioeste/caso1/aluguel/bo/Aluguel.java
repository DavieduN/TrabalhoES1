package unioeste.caso1.aluguel.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aluguel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idAluguel;
    private int nroAluguel;

    private Cliente cliente;
    private Equipamento equipamento;

    private LocalDate dataPedido;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucao;

    private double valorTotalLocacao;

    public Aluguel() {
        this.cliente = new Cliente();
        this.equipamento = new Equipamento();
    }

    public Aluguel(int idAluguel, int nroAluguel, Cliente cliente, Equipamento equipamento,
                   LocalDate dataPedido, LocalDate dataLocacao, LocalDate dataDevolucao,
                   double valorTotalLocacao) {
        this.idAluguel = idAluguel;
        this.nroAluguel = nroAluguel;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.dataPedido = dataPedido;
        this.dataLocacao = dataLocacao;
        this.dataDevolucao = dataDevolucao;
        this.valorTotalLocacao = valorTotalLocacao;
    }

    public int getIdAluguel() { return idAluguel; }
    public void setIdAluguel(int idAluguel) { this.idAluguel = idAluguel; }

    public int getNroAluguel() { return nroAluguel; }
    public void setNroAluguel(int nroAluguel) { this.nroAluguel = nroAluguel; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public double getValorTotalLocacao() { return valorTotalLocacao; }
    public void setValorTotalLocacao(double valorTotalLocacao) {
        this.valorTotalLocacao = valorTotalLocacao;
    }
}
