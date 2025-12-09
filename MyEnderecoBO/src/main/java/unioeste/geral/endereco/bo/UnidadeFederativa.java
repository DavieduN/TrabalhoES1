package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class UnidadeFederativa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String siglaUF;
    private String nomeUF;

    public UnidadeFederativa() {}

    public UnidadeFederativa(String siglaUF, String nomeUF) {
        this.siglaUF = siglaUF;
        this.nomeUF = nomeUF;
    }

    public String getSiglaUF() { return siglaUF; }
    public void setSiglaUF(String siglaUF) { this.siglaUF = siglaUF; }

    public String getNomeUF() { return nomeUF; }
    public void setNomeUF(String nomeUF) { this.nomeUF = nomeUF; }

    @Override
    public String toString() { return siglaUF; }
}