package unioeste.geral.pessoa.dao;

import unioeste.geral.pessoa.bo.Telefone;
import java.sql.Connection;
import java.util.List;

public interface TelefoneDAO {
    void inserir(Connection con, Telefone telefone, int idPessoa) throws Exception;
    List<Telefone> buscarPorPessoa(Connection con, int idPessoa) throws Exception;
}