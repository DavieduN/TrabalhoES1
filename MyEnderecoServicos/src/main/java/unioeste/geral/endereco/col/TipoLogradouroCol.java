package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.TipoLogradouro;
import unioeste.geral.endereco.dao.TipoLogradouroDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import java.sql.Connection;
import java.util.List;

public class TipoLogradouroCol {

    private final TipoLogradouroDAO dao;

    public TipoLogradouroCol() {
        this.dao = new TipoLogradouroDAO();
    }

    public List<TipoLogradouro> buscarTodos(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }

    public TipoLogradouro validarExistencia(Connection con, TipoLogradouro tipoLogradouro) throws Exception {
        if (tipoLogradouro == null)
            throw new EnderecoException("Tipo Logradouro é obrigatório para o cadastro.");

        if (tipoLogradouro.getIdTipoLogradouro() > 0){
            TipoLogradouro tipo = dao.buscarPorId(con, tipoLogradouro.getIdTipoLogradouro());

            if (tipo == null) {
                throw new EnderecoException("Tipo Logradouro não encontrado com o ID informado: " +
                        tipoLogradouro.getIdTipoLogradouro());
            } else if (tipoLogradouro.getNomeTipoLogradouro() != null &&
                    !tipoLogradouro.getNomeTipoLogradouro().trim().isEmpty() &&
                    !tipo.getNomeTipoLogradouro().trim().equalsIgnoreCase(tipoLogradouro.getNomeTipoLogradouro().trim())) {
                throw new EnderecoException("Nome do Tipo Logradouro difere do cadastro.");
            }
            return tipo;
        }

        if (tipoLogradouro.getNomeTipoLogradouro() == null || tipoLogradouro.getNomeTipoLogradouro().trim().isEmpty())
            throw new EnderecoException("Informe o nome do Tipo Logradouro para o cadastro.");

        TipoLogradouro tipo = dao.buscarPorNome(con, tipoLogradouro.getNomeTipoLogradouro());

        if (tipo == null)
            throw new EnderecoException("Tipo Logradouro inválido ou inexistente: " + tipoLogradouro.getNomeTipoLogradouro());

        return tipo;
    }
}