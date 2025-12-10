package unioeste.geral.pessoa.bo;

import unioeste.geral.endereco.bo.EnderecoEspecifico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class PessoaFisica implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int idPessoaFisica;
    protected String cpf;
    protected Pessoa pessoa;

    public PessoaFisica() {}

    public int getIdPessoaFisica() { return idPessoaFisica; }
    public void setIdPessoaFisica(int idPessoaFisica) { this.idPessoaFisica = idPessoaFisica; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }

}