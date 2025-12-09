package unioeste.geral.endereco.bo;

import java.io.Serializable;

public class EnderecoEspecifico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idEnderecoEspecifico;
    private String numero;
    private String complemento;
    private Endereco endereco;

    public EnderecoEspecifico() {
        this.endereco = new Endereco();
    }

    public int getIdEnderecoEspecifico() { return idEnderecoEspecifico; }
    public void setIdEnderecoEspecifico(int idEnderecoEspecifico) { this.idEnderecoEspecifico = idEnderecoEspecifico; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return endereco.toString() + ", Nº " + numero + (complemento != null ? " (" + complemento + ")" : "");
    }
}