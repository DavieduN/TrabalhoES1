package unioeste.geral.pessoa.dao;

import unioeste.geral.pessoa.bo.PessoaFisica;
import java.sql.Connection;
import java.util.List;

public interface PessoaFisicaDAO<T extends PessoaFisica> {
    void inserir(Connection con, T pessoa) throws Exception;
    T buscarPorId(Connection con, int id) throws Exception;
    T buscarPorCpf(Connection con, String cpf) throws Exception;
    List<T> buscarPorNome(Connection con, String nome) throws Exception;
}