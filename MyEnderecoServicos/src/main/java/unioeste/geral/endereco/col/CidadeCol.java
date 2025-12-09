package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.dao.CidadeDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import java.util.List;

public class CidadeCol {
        final private CidadeDAO dao;
    public CidadeCol() {
        this.dao = new CidadeDAO();
    }

    public Cidade buscarPorId(Cidade cidade) throws Exception {
        if (cidade == null) {
            throw new EnderecoException("Cidade para buscar não foi informada.");
        }
        if (cidade.getIdCidade() <= 0) {
            throw new EnderecoException("ID da cidade inválido para consulta.");
        }

        Cidade encontrada = dao.buscarPorId(cidade.getIdCidade());

        if (encontrada == null) {
            throw new EnderecoException("Cidade não encontrada com o ID informado.");
        }

        return encontrada;
    }

    public List<Cidade> buscarTodas() throws Exception {
        return dao.buscarTodos();
    }
}