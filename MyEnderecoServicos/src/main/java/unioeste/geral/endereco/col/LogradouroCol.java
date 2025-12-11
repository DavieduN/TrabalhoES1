package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.dao.LogradouroDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.util.TextoUtil;
import java.sql.Connection;

public class LogradouroCol {

    private final LogradouroDAO dao;

    public LogradouroCol() {
        this.dao = new LogradouroDAO();
    }

    public Logradouro obterOuCadastrar(Connection con, Logradouro logradouro) throws Exception {
        if (logradouro == null) {
            throw new EnderecoException("Logradouro é obrigatório para o cadastro.");
        }
        if (logradouro.getTipoLogradouro() == null || logradouro.getTipoLogradouro().getIdTipoLogradouro() <= 0) {
            throw new EnderecoException("Tipo de Logradouro inválido ou não informado.");
        }

        if (logradouro.getIdLogradouro() > 0) {
            Logradouro encontrado = dao.buscarPorId(con, logradouro.getIdLogradouro());

            if (encontrado == null) {
                throw new EnderecoException("Logradouro não encontrado com o ID informado: " + logradouro.getIdLogradouro());
            } else if (logradouro.getNomeLogradouro() != null &&
                    !logradouro.getNomeLogradouro().trim().isEmpty() &&
                    !logradouro.getNomeLogradouro().trim().equalsIgnoreCase(encontrado.getNomeLogradouro().trim())) {
                throw new EnderecoException("Nome do logradouro difere do cadastro existente.");
            }
            return encontrado;
        }

        if (logradouro.getNomeLogradouro() == null) {
            logradouro.setNomeLogradouro("");
        }

        if (!logradouro.getNomeLogradouro().trim().isEmpty()) {
            TextoUtil.validarTextoAlfanumerico(logradouro.getNomeLogradouro(), "Nome do Logradouro");
            String nomeFormatado = TextoUtil.formatarNomeLogradouro(logradouro.getNomeLogradouro());
            logradouro.setNomeLogradouro(nomeFormatado);
        }

        Logradouro encontrado = dao.buscarPorNomeETipo(
                con,
                logradouro.getNomeLogradouro(),
                logradouro.getTipoLogradouro().getIdTipoLogradouro()
        );

        if (encontrado != null) {
            return encontrado;
        }

        dao.inserir(con, logradouro);
        return logradouro;
    }
}