package unioeste.geral.pessoa.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.geral.pessoa.bo.PessoaFisica;
import unioeste.geral.pessoa.bo.Telefone;
import unioeste.geral.pessoa.col.*;
import unioeste.geral.pessoa.dao.PessoaFisicaDAO;

import java.sql.Connection;
import java.util.List;

public class UCPessoaFisicaServicos<T extends PessoaFisica> {

    private final PessoaFisicaCol<T> pessoaCol;
    private final TelefoneCol telefoneCol;
    private final EmailCol emailCol;
    private final DdiCol ddiCol;
    private final DddCol dddCol;

    private final UCEnderecoGeralServicos enderecoService;
    private final ConexaoBD conexaoBD;

    public UCPessoaFisicaServicos(PessoaFisicaDAO<T> daoEspecifico) {
        this.pessoaCol = new PessoaFisicaCol<>(daoEspecifico);
        this.telefoneCol = new TelefoneCol();
        this.emailCol = new EmailCol();
        this.ddiCol = new DdiCol();
        this.dddCol = new DddCol();

        this.enderecoService = new UCEnderecoGeralServicos();
        this.conexaoBD = new ConexaoBD();
    }

    public T cadastrarPessoa(T pessoa) throws Exception {
        if (pessoa == null) throw new Exception("Dados da pessoa não informados.");
        if (pessoa.getEndereco() == null) throw new Exception("Endereço é obrigatório.");

        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            con.setAutoCommit(false);

            Endereco enderecoSalvo = enderecoService.cadastrarEndereco(con, pessoa.getEndereco());
            pessoa.setEndereco(enderecoSalvo);

            T pessoaSalva = pessoaCol.obterOuCadastrar(con, pessoa);

            if (pessoa.getTelefones() != null && !pessoa.getTelefones().isEmpty()) {
                for (Telefone t: pessoa.getTelefones()) {
                    t.setDdi(ddiCol.validarExistencia(con, t.getDdi()));
                    t.setDdd(dddCol.validarExistencia(con, t.getDdd()));
                }
                telefoneCol.salvarTelefones(con, pessoa.getTelefones(), pessoaSalva.getIdPessoa());
            }

            if (pessoa.getEmails() != null && !pessoa.getEmails().isEmpty()) {
                emailCol.salvarEmails(con, pessoa.getEmails(), pessoaSalva.getIdPessoa());
            }

            con.commit();
            return pessoaSalva;

        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception rollbackEx) { rollbackEx.printStackTrace(); }
            }
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public T buscarPorId(int id) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            T pessoa = pessoaCol.buscarPorId(con, id);
            if (pessoa != null) {
                pessoa.setTelefones(telefoneCol.buscarPorPessoa(con, id));
                pessoa.setEmails(emailCol.buscarPorPessoa(con, id));
            }
            return pessoa;
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public T buscarPorCpf(String cpf) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            T pessoa = pessoaCol.buscarPorCpf(con, cpf);

            if (pessoa != null) {
                pessoa.setTelefones(telefoneCol.buscarPorPessoa(con, pessoa.getIdPessoa()));
                pessoa.setEmails(emailCol.buscarPorPessoa(con, pessoa.getIdPessoa()));
            }
            return pessoa;
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
}