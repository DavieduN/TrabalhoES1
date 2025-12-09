package unioeste.geral.pessoa.bo;

import java.io.Serializable;

public class DDI implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idDdi;
    private int ddi;

    public DDI(){}
    public DDI(int idDdi, int ddi){
        this.idDdi = idDdi;
        this.ddi = ddi;
    }

    public int getDdi() { return ddi; }
    public void setDdi(int ddi) { this.ddi = ddi; }

    public int getIdDdi() { return idDdi; }
    public void setIdDdi(int idDdi) { this.idDdi = idDdi; }

    @Override
    public String toString() {
        return "+" + ddi;
    }
}
