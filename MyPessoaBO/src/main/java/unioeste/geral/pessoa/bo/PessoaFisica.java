package unioeste.geral.pessoa.bo;

import java.io.Serializable;

public abstract class PessoaFisica extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String cpf;

    public PessoaFisica() {}

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}