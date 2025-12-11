package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.UnidadeFederativa;
import unioeste.geral.endereco.dao.UfDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import java.util.List;
import java.sql.Connection;

public class UfCol {
    private final UfDAO dao;

    public UfCol() {
        this.dao = new UfDAO();
    }

    public List<UnidadeFederativa> buscarTodas(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }

    public UnidadeFederativa validarExistencia(Connection con, UnidadeFederativa unidadeFederativa) throws Exception {
        if (unidadeFederativa == null)
            throw new EnderecoException("Unidade Federativa é obrigatória para o cadastro.");
        if (unidadeFederativa.getSiglaUF() == null)
            throw new EnderecoException("Informe a sigla da Unidade Federativa para o cadastro.");

        UnidadeFederativa uf = dao.buscarPorSigla(con, unidadeFederativa.getSiglaUF());

        if (uf == null)
            throw new EnderecoException("Unidade Federativa inválida.");

        return uf;
    }
}