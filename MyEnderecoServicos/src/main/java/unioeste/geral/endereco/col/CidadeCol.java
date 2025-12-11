package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.dao.CidadeDAO;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.util.TextoUtil;
import java.sql.Connection;

public class CidadeCol {
    final private CidadeDAO dao;

    public CidadeCol() {
        this.dao = new CidadeDAO();
    }

    public Cidade buscarPorId(Connection con, Cidade cidade) throws Exception {
        if (cidade == null) {
            throw new EnderecoException("Cidade para buscar não foi informada.");
        }
        if (cidade.getIdCidade() <= 0) {
            throw new EnderecoException("ID da cidade inválido para consulta.");
        }

        Cidade encontrada = dao.buscarPorId(con, cidade.getIdCidade());

        if (encontrada == null) {
            throw new EnderecoException("Cidade não encontrada com o ID informado.");
        }

        return encontrada;
    }

    public Cidade obterOuCadastrar(Connection con, Cidade cidade) throws Exception {
        if (cidade == null) {
            throw new EnderecoException("Cidade é obrigatória para o cadastro.");
        }

        if (cidade.getIdCidade() > 0) {
            Cidade encontrada = dao.buscarPorId(con, cidade.getIdCidade());
            if (encontrada == null) {
                throw new EnderecoException("Cidade não encontrada com o ID informado: " + cidade.getIdCidade());
            }
            else if (cidade.getNomeCidade() != null &&
                    !cidade.getNomeCidade().trim().isEmpty() &&
                    !cidade.getNomeCidade().trim().equalsIgnoreCase(encontrada.getNomeCidade().trim())) {
                throw new EnderecoException("Nome da cidade encontrada difere da informada: " + cidade.getNomeCidade() +
                        " != " + encontrada.getNomeCidade());
            }
            return encontrada;
        }

        TextoUtil.validarNome(cidade.getNomeCidade(), "Nome da cidade");
        String nomeFormatado = TextoUtil.formatarNome(cidade.getNomeCidade());
        cidade.setNomeCidade(nomeFormatado);

        Cidade encontrada = dao.buscarPorNomeSigla(
                con,
                nomeFormatado,
                cidade.getUnidadeFederativa().getSiglaUF()
        );

        if (encontrada != null) return encontrada;

        dao.inserir(con, cidade);
        return cidade;
    }
}