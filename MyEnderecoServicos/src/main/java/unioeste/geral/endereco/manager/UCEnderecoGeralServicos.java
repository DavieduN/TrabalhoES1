package unioeste.geral.endereco.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.col.*;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

public class UCEnderecoGeralServicos {
    private final EnderecoCol enderecoCol;
    private final CidadeCol cidadeCol;
    private final UfCol ufCol;
    private final BairroCol bairroCol;
    private final TipoLogradouroCol tipoLogradouroCol;
    private final LogradouroCol logradouroCol;

    private final ConexaoBD conexaoBD;

    public UCEnderecoGeralServicos() {
        this.enderecoCol = new EnderecoCol();
        this.cidadeCol = new CidadeCol();
        this.ufCol = new UfCol();
        this.bairroCol = new BairroCol();
        this.tipoLogradouroCol = new TipoLogradouroCol();
        this.logradouroCol = new LogradouroCol();

        this.conexaoBD = new ConexaoBD();
    }

    // --- Helper ---

    private void validarEstruturaEndereco(Endereco endereco) throws EnderecoException {
        if (endereco == null) {
            throw new EnderecoException("O objeto endereço não pode ser nulo para cadastro.");
        }
        if (endereco.getCidade() == null) throw new EnderecoException("Objeto Cidade é obrigatório.");
        if (endereco.getCidade().getUnidadeFederativa() == null) throw new EnderecoException("Objeto UF é obrigatório.");
        if (endereco.getBairro() == null) throw new EnderecoException("Objeto Bairro é obrigatório.");
        if (endereco.getLogradouro() == null) throw new EnderecoException("Objeto Logradouro é obrigatório.");
        if (endereco.getLogradouro().getTipoLogradouro() == null) throw new EnderecoException("Objeto Tipo de Logradouro é obrigatório.");
    }

    private Endereco orquestrarCadastro(Connection con, Endereco endereco) throws Exception {
        UnidadeFederativa uf = ufCol.validarExistencia(con, endereco.getCidade().getUnidadeFederativa());
        endereco.getCidade().setUnidadeFederativa(uf);

        Cidade cidadeSalva = cidadeCol.obterOuCadastrar(con, endereco.getCidade());
        endereco.setCidade(cidadeSalva);

        Bairro bairroSalvo = bairroCol.obterOuCadastrar(con, endereco.getBairro());
        endereco.setBairro(bairroSalvo);

        TipoLogradouro tipo = tipoLogradouroCol.validarExistencia(con, endereco.getLogradouro().getTipoLogradouro());
        endereco.getLogradouro().setTipoLogradouro(tipo);

        Logradouro logSalvo = logradouroCol.obterOuCadastrar(con, endereco.getLogradouro());
        endereco.setLogradouro(logSalvo);

        return enderecoCol.obterOuCadastrar(con, endereco);
    }

    // --- Especificados ---

    public Endereco cadastrarEndereco(Connection con, Endereco endereco) throws Exception {
        validarEstruturaEndereco(endereco);
        return orquestrarCadastro(con, endereco);
    }

    public Endereco cadastrarEndereco(Endereco endereco) throws Exception {
        validarEstruturaEndereco(endereco);

        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            con.setAutoCommit(false);

            Endereco salvo = orquestrarCadastro(con, endereco);

            con.commit();
            return salvo;

        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (Exception rollbackEx) { rollbackEx.printStackTrace(); }
            }
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }


    public List<Endereco> obterEnderecoPorCep(String cep) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return enderecoCol.buscarPorCep(con, cep);
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public Endereco obterEnderecoPorID(Endereco filtro) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return enderecoCol.buscarPorID(con, filtro);
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public Cidade obterCidadePorID(Cidade filtro) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return cidadeCol.buscarPorId(con, filtro);
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public Endereco obterEnderecoExterno(String cep) throws Exception {
        return enderecoCol.consultarCepExterno(cep);
    }

    // --- Auxiliares ---

    public List<UnidadeFederativa> consultarUFs() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return ufCol.buscarTodas(con);
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public List<TipoLogradouro> consultarTiposLogradouro() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return tipoLogradouroCol.buscarTodos(con);
        } catch (Exception e) {
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

}