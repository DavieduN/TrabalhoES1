package unioeste.geral.pessoa.bo;

import unioeste.geral.endereco.bo.Endereco;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int idPessoa;
    protected String nome;

    protected Endereco endereco;
    protected String numero;
    protected String complemento;

    protected List<Telefone> telefones;
    protected List<Email> emails;

    public Pessoa() {
        this.endereco = new Endereco();
        this.telefones = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public int getIdPessoa() { return idPessoa; }
    public void setIdPessoa(int idPessoa) { this.idPessoa = idPessoa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public List<Telefone> getTelefones() { return telefones; }
    public void setTelefones(List<Telefone> telefones) { this.telefones = telefones; }

    public List<Email> getEmails() { return emails; }
    public void setEmails(List<Email> emails) { this.emails = emails; }

    public void adicionarTelefone(Telefone t) {
        this.telefones.add(t);
    }
    public void adicionarEmail(Email e) {
        this.emails.add(e);
    }
}