package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.dao.*;
import unioeste.geral.endereco.exception.EnderecoException;
import java.sql.Connection;

public class EnderecoCol {
    final private UfDAO ufDAO;
    final private EnderecoDAO enderecoDAO;
    final private CidadeDAO cidadeDAO;
    final private BairroDAO bairroDAO;
    final private LogradouroDAO logradouroDAO;
    final private TipoLogradouroDAO tipoLogradouroDAO;

    public EnderecoCol() {
        this.ufDAO = new UfDAO();
        this.enderecoDAO = new EnderecoDAO();
        this.cidadeDAO = new CidadeDAO();
        this.bairroDAO = new BairroDAO();
        this.logradouroDAO = new LogradouroDAO();
        this.tipoLogradouroDAO = new TipoLogradouroDAO();
    }

    public void cadastrarEndereco(Connection con, Endereco endereco) throws EnderecoException, Exception {
        if (endereco == null)
            throw new EnderecoException("Endereço não informado.");
        if (endereco.getCidade() == null)
            throw new EnderecoException("Cidade não informada.");
        if (endereco.getCidade().getUnidadeFederativa() == null)
            throw new EnderecoException("UF não informada.");
        if (endereco.getBairro() == null)
            throw new EnderecoException("Bairro não informado.");
        if (endereco.getLogradouro() == null)
            throw new EnderecoException("Logradouro não informado.");
        
        String cepLimpo = endereco.getCep().replaceAll("\\D", "");
        if (cepLimpo.length() != 8) throw new EnderecoException("CEP inválido (" + endereco.getCep() + ").");
        endereco.setCep(cepLimpo); 

        UnidadeFederativa uf = endereco.getCidade().getUnidadeFederativa();
        UnidadeFederativa ufBanco = ufDAO.buscarPorSigla(con, uf.getSiglaUF());
        
        if (ufBanco == null){
            if (uf.getNomeUF() == null) uf.setNomeUF(uf.getSiglaUF());
            ufDAO.inserir(con, uf);
        }

        Cidade cid = endereco.getCidade();
        if (cid.getIdCidade() == 0) {
            Cidade cidBanco = cidadeDAO.buscarPorNomeSigla(con, cid.getNomeCidade(), uf.getSiglaUF());
            if (cidBanco != null) cid.setIdCidade(cidBanco.getIdCidade());
            else cidadeDAO.inserir(con, cid);
        }

        Bairro bairro = endereco.getBairro();
        if (bairro.getIdBairro() == 0) {
            Bairro baiBanco = bairroDAO.buscarPorNome(con, bairro.getNomeBairro());
            if (baiBanco != null) bairro.setIdBairro(baiBanco.getIdBairro());
            else bairroDAO.inserir(con, bairro);
        }

        TipoLogradouro tipo = endereco.getLogradouro().getTipoLogradouro();
        if (tipo != null && tipo.getIdTipoLogradouro() == 0) {
            TipoLogradouro tipoBanco = tipoLogradouroDAO.buscarPorNome(con, tipo.getNomeTipoLogradouro());
            if (tipoBanco != null) tipo.setIdTipoLogradouro(tipoBanco.getIdTipoLogradouro());
            else tipoLogradouroDAO.inserir(con, tipo);
        }

        Logradouro logradouro = endereco.getLogradouro();
        if (logradouro.getIdLogradouro() == 0) {
            int idTipo = logradouro.getTipoLogradouro().getIdTipoLogradouro();
            Logradouro logBanco = logradouroDAO.buscarPorNomeETipo(con, logradouro.getNomeLogradouro(), idTipo);
            if (logBanco != null) logradouro.setIdLogradouro(logBanco.getIdLogradouro());
            else logradouroDAO.inserir(con, logradouro);
        }

        enderecoDAO.inserir(con, endereco);
    }
}