package unioeste.geral.pessoa.bo;

import unioeste.geral.endereco.bo.EnderecoEspecifico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int idPessoa;
    protected String nome;

    protected EnderecoEspecifico endereco;
    protected List<Telefone> telefones;
    protected List<Email> emails;

    public Pessoa() {
        this.endereco = new EnderecoEspecifico();
        this.telefones = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public int getIdPessoa() { return idPessoa; }
    public void setIdPessoa(int idPessoa) { this.idPessoa = idPessoa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public EnderecoEspecifico getEndereco() { return endereco; }
    public void setEndereco(EnderecoEspecifico endereco) { this.endereco = endereco; }

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