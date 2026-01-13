package unioeste.caso1.aluguel.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.caso1.aluguel.bo.Equipamento;
import unioeste.caso1.aluguel.bo.TipoEquipamento;
import unioeste.caso1.aluguel.col.EquipamentoCol;
import unioeste.caso1.aluguel.col.TipoEquipamentoCol;

import java.sql.Connection;
import java.util.List;

public class UCEquipamentoServicos {

    private final EquipamentoCol equipamentoCol;
    private final TipoEquipamentoCol tipoCol;
    private final ConexaoBD conexaoBD;

    public UCEquipamentoServicos() {
        this.equipamentoCol = new EquipamentoCol();
        this.tipoCol = new TipoEquipamentoCol();
        this.conexaoBD = new ConexaoBD();
    }

    public void cadastrarTipo(TipoEquipamento tipo) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            tipoCol.obterOuCadastrar(con, tipo);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public List<TipoEquipamento> listarTipos() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return tipoCol.listarTodos(con);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public void cadastrarEquipamento(Equipamento equipamento) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            con.setAutoCommit(false);

            equipamentoCol.cadastrar(con, equipamento);

            con.commit();
        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (Exception ex) {}
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public List<Equipamento> listarEquipamentos() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return equipamentoCol.listarDisponiveis(con);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
}