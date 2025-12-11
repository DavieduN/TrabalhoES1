package unioeste.geral.endereco.col;

import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.dao.BairroDAO;
import unioeste.geral.endereco.util.TextoUtil;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;

public class BairroCol {
    private final BairroDAO dao;
    public BairroCol() { this.dao = new BairroDAO(); }

    public Bairro obterOuCadastrar(Connection con, Bairro bairro) throws Exception {
        if (bairro == null) {
            throw new EnderecoException("Bairro é obrigatório para o cadastro.");
        }

        if (bairro.getIdBairro() > 0) {
            Bairro encontrado = dao.buscarPorId(con, bairro.getIdBairro());
            if (encontrado == null) {
                throw new EnderecoException("Bairro não encontrado com o ID informado: " + bairro.getIdBairro());
            } else if (bairro.getNomeBairro() != null &&
                    !bairro.getNomeBairro().trim().isEmpty() &&
                    !bairro.getNomeBairro().trim().equalsIgnoreCase(encontrado.getNomeBairro().trim())) {
                throw new EnderecoException("Nome do bairro difere do cadastro existente.");
            }
            return encontrado;
        }

        TextoUtil.validarTextoAlfanumerico(bairro.getNomeBairro(), "Nome do Bairro");
        String nomeFormatado = TextoUtil.formatarNome(bairro.getNomeBairro());
        bairro.setNomeBairro(nomeFormatado);

        Bairro encontrado = dao.buscarPorNome(con, nomeFormatado);

        if (encontrado != null) return encontrado;

        dao.inserir(con, bairro);
        return bairro;
    }
}