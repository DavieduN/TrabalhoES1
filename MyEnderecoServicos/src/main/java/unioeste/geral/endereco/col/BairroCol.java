package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.dao.BairroDAO;
import java.util.List;

public class BairroCol {
    private final BairroDAO dao;
    public BairroCol() { this.dao = new BairroDAO(); }

    public List<Bairro> buscarTodos() throws Exception {
        return dao.buscarTodos();
    }
}