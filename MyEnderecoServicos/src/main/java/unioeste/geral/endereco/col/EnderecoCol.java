package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.dao.*;
import unioeste.geral.endereco.exception.EnderecoException;
import java.sql.Connection;

public class EnderecoCol {

    final private EnderecoDAO enderecoDAO;
    final private CidadeDAO cidadeDAO;
    final private BairroDAO bairroDAO;
    final private LogradouroDAO logradouroDAO;
    final private TipoLogradouroDAO tipoLogradouroDAO;

    public EnderecoCol() {
        this.enderecoDAO = new EnderecoDAO();
        this.cidadeDAO = new CidadeDAO();
        this.bairroDAO = new BairroDAO();
        this.logradouroDAO = new LogradouroDAO();
        this.tipoLogradouroDAO = new TipoLogradouroDAO();
    }

    public void cadastrarEndereco(Connection con, Endereco endereco) throws EnderecoException, Exception {
        if (endereco == null)
            throw new EnderecoException("Endereço inválido.");
        String cep = endereco.getCep().replaceAll("\\D", "");
        if (cep.length() != 8)
            throw new EnderecoException("CEP inválido. Deve conter 8 dígitos.");

        if (endereco.getCidade().getIdCidade() == 0)
            cidadeDAO.inserir(con, endereco.getCidade());

        if (endereco.getBairro().getIdBairro() == 0)
            bairroDAO.inserir(con, endereco.getBairro());

        if (endereco.getLogradouro().getIdLogradouro() == 0) {
            if (endereco.getLogradouro().getTipoLogradouro().getIdTipoLogradouro() == 0)
                tipoLogradouroDAO.inserir(con, endereco.getLogradouro().getTipoLogradouro());
            logradouroDAO.inserir(con, endereco.getLogradouro());
        }
        enderecoDAO.inserir(con, endereco);
    }
}