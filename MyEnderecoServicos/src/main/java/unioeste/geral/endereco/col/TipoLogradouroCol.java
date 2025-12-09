package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.dao.TipoLogradouroDAO;
import java.util.List;

public class TipoLogradouroCol {
    private final TipoLogradouroDAO dao;
    public TipoLogradouroCol() { this.dao = new TipoLogradouroDAO(); }

    public List<TipoLogradouro> buscarTodos() throws Exception {
        return dao.buscarTodos();
    }
}