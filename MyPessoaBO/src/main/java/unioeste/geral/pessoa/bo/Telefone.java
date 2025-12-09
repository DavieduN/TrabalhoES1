package unioeste.geral.pessoa.bo;

import java.io.Serializable;

public class Telefone implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTelefone;
    private String numero;
    private DDI ddi;
    private DDD ddd;

    public Telefone() {
        this.ddi = new DDI();
        this.ddd = new DDD();
    }

    public int getIdTelefone() { return idTelefone; }
    public void setIdTelefone(int idTelefone) { this.idTelefone = idTelefone; }

    public DDI getDdi() { return ddi; }
    public void setDdi(DDI ddi) { this.ddi = ddi; }

    public DDD getDdd() { return ddd; }
    public void setDdd(DDD ddd) { this.ddd = ddd; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    @Override
    public String toString() {
        return ddi.toString() + " " + ddd.toString() + " " + numero;
    }
}