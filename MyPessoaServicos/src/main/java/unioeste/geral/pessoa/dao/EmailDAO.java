package unioeste.geral.pessoa.dao;

import unioeste.geral.pessoa.bo.Email;
import java.sql.Connection;
import java.util.List;

public interface EmailDAO {
    void inserir(Connection con, Email email, int idPessoa) throws Exception;
    List<Email> buscarPorPessoa(Connection con, int idPessoa) throws Exception;
}