package unioeste.caso2.servico.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.caso2.servico.bo.TipoServico;
import unioeste.caso2.servico.col.TipoServicoCol;

import java.sql.Connection;
import java.util.List;

public class UCTipoServicoServicos {

    private final TipoServicoCol tipoCol;
    private final ConexaoBD conexaoBD;

    public UCTipoServicoServicos() {
        this.tipoCol = new TipoServicoCol();
        this.conexaoBD = new ConexaoBD();
    }

    public void cadastrarTipo(TipoServico tipo) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            tipoCol.obterOuCadastrar(con, tipo);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public List<TipoServico> listarTipos() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return tipoCol.listarTodos(con);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
}