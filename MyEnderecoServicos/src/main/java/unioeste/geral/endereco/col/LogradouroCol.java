package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.dao.LogradouroDAO;
import java.util.List;

public class LogradouroCol {
    private final LogradouroDAO dao;
    public LogradouroCol() { this.dao = new LogradouroDAO(); }

    public List<Logradouro> buscarTodos() throws Exception {
        return dao.buscarTodos();
    }
}