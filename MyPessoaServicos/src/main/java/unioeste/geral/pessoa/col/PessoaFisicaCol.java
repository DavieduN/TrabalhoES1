package unioeste.geral.pessoa.col;

import unioeste.geral.endereco.util.TextoUtil;
import unioeste.geral.pessoa.bo.PessoaFisica;
import unioeste.geral.pessoa.dao.PessoaFisicaDAO;

import java.sql.Connection;
import java.util.List;

public class PessoaFisicaCol<T extends PessoaFisica> {

    private final PessoaFisicaDAO<T> daoEspecifico;

    public PessoaFisicaCol(PessoaFisicaDAO<T> daoEspecifico) {
        this.daoEspecifico = daoEspecifico;
    }

    public T buscarPorId(Connection con, int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido para busca.");
        }
        return daoEspecifico.buscarPorId(con, id);
    }

    public T buscarPorCpf(Connection con, String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("CPF é obrigatório para a busca.");
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11) {
            throw new Exception("CPF informado tem tamanho inválido (deve ter 11 dígitos).");
        }

        return daoEspecifico.buscarPorCpf(con, cpfLimpo);
    }

    public List<T> buscarPorNome(Connection con, String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Nome é obrigatório para a busca.");
        }
        return daoEspecifico.buscarPorNome(con, nome);
    }

    public T obterOuCadastrar(Connection con, T pessoa) throws Exception {
        if (pessoa == null) {
            throw new Exception("Dados da pessoa são obrigatórios.");
        }

        if (pessoa.getCpf() == null) {
            throw new Exception("CPF é obrigatório.");
        }
        String cpfLimpo = pessoa.getCpf().replaceAll("\\D", "");
        pessoa.setCpf(cpfLimpo);

        if (cpfLimpo.length() != 11) {
            throw new Exception("CPF inválido: Deve conter 11 dígitos.");
        }

        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório.");
        }
        TextoUtil.validarNome(pessoa.getNome(), "Nome da Pessoa");
        pessoa.setNome(TextoUtil.formatarNome(pessoa.getNome()));

        T existente = daoEspecifico.buscarPorCpf(con, cpfLimpo);
        if (existente != null) {
            return existente;
        }

        if (pessoa.getEndereco() == null) {
            throw new Exception("Endereço é obrigatório para o cadastro.");
        }

        if (pessoa.getEndereco().getIdEndereco() <= 0) {
            throw new Exception("Erro de orquestração: O endereço da pessoa deve ser salvo previamente.");
        }
        daoEspecifico.inserir(con, pessoa);

        return pessoa;
    }
}