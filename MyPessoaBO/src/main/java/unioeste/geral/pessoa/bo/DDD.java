package unioeste.geral.pessoa.bo;

import java.io.Serializable;

public class DDD implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idDdd;
    private int ddd;

    public DDD(){}
    public DDD(int idDdd, int ddd){
        this.idDdd = idDdd;
        this.ddd = ddd;
    }

    public int getDdd() { return ddd; }
    public void setDdd(int ddd) { this.ddd = ddd; }

    public int getIdDdd() { return idDdd; }
    public void setIdDdd(int idDdd) { this.idDdd = idDdd; }

    @Override
    public String toString() {
        return "(" + ddd + ")";
    }
}
