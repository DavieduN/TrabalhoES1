package unioeste.caso1.aluguel.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.bo.Cliente;
import unioeste.caso1.aluguel.bo.Equipamento;
import unioeste.caso1.aluguel.col.AluguelCol;
import unioeste.caso1.aluguel.col.EquipamentoCol;

import java.sql.Connection;
import java.util.List;

public class UCAluguelServicos {

    private final AluguelCol aluguelCol;
    private final EquipamentoCol equipamentoCol;
    private final UCClienteServicos clienteService;
    private final ConexaoBD conexaoBD;

    public UCAluguelServicos() {
        this.aluguelCol = new AluguelCol();
        this.equipamentoCol = new EquipamentoCol();
        this.clienteService = new UCClienteServicos();
        this.conexaoBD = new ConexaoBD();
    }

    public Aluguel registrarAluguel(Aluguel aluguel) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            con.setAutoCommit(false);

            Cliente c = clienteService.buscarClientePorId(aluguel.getCliente().getIdPessoa());
            if (c == null) {
                throw new Exception("Cliente não encontrado.");
            }
            aluguel.setCliente(c);

            Equipamento e = equipamentoCol.buscarPorId(con, aluguel.getEquipamento().getIdEquipamento());
            if (e == null) {
                throw new Exception("Equipamento não encontrado.");
            }
            aluguel.setEquipamento(e);

            aluguelCol.registrarAluguel(con, aluguel);

            con.commit();
            return aluguel;

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (Exception ex) {}
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public List<Aluguel> consultarAlugueis() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return aluguelCol.consultarAlugueis(con);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public Aluguel buscarAluguelPorNumero(int nroAluguel) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            return aluguelCol.buscarPorNumero(con, nroAluguel);
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
}