package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.UnidadeFederativa;
import unioeste.geral.endereco.dao.UfDAO;
import java.util.List;

public class UfCol {
    private final UfDAO dao;

    public UfCol() {
        this.dao = new UfDAO();
    }

    public List<UnidadeFederativa> buscarTodas() throws Exception {
        return dao.buscarTodos();
    }
}