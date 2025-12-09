package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class Endereco implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEndereco;
    private String cep;

    private Cidade cidade;
    private Bairro bairro;
    private Logradouro logradouro;

    public Endereco() {
        this.cidade = new Cidade();
        this.bairro = new Bairro();
        this.logradouro = new Logradouro();
    }

    public int getIdEndereco() { return idEndereco; }
    public void setIdEndereco(int idEndereco) { this.idEndereco = idEndereco; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Cidade getCidade() { return cidade; }
    public void setCidade(Cidade cidade) { this.cidade = cidade; }

    public Bairro getBairro() { return bairro; }
    public void setBairro(Bairro bairro) { this.bairro = bairro; }

    public Logradouro getLogradouro() { return logradouro; }
    public void setLogradouro(Logradouro logradouro) { this.logradouro = logradouro; }

    @Override
    public String toString() {
        return logradouro + ", " + bairro + ", " + cidade + " - CEP: " + cep;
    }
}