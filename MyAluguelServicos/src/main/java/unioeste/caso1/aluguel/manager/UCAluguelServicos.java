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

            Cliente c = clienteService.buscarClientePorId(con, aluguel.getCliente().getIdPessoa());
            if (c == null) throw new Exception("Cliente não encontrado.");
            aluguel.setCliente(c);

            Equipamento e = equipamentoCol.buscarPorId(con, aluguel.getEquipamento().getIdEquipamento());
            if (e == null) throw new Exception("Equipamento não encontrado.");
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
            List<Aluguel> lista = aluguelCol.consultarAlugueis(con);

            for (Aluguel a : lista) {
                carregarClienteCompleto(con, a);
                carregarEquipamentoCompleto(con, a);
            }

            return lista;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public Aluguel buscarAluguelPorNumero(int nroAluguel) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            Aluguel aluguel = aluguelCol.buscarPorNumero(con, nroAluguel);

            if (aluguel != null) {
                carregarClienteCompleto(con, aluguel);
                carregarEquipamentoCompleto(con, aluguel);
            }

            return aluguel;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    private void carregarClienteCompleto(Connection con, Aluguel a) {
        if (a.getCliente() != null && a.getCliente().getIdPessoa() > 0) {
            try {
                Cliente completo = clienteService.buscarClientePorId(con, a.getCliente().getIdPessoa());
                if (completo != null) {
                    a.setCliente(completo);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar cliente ID " + a.getCliente().getIdPessoa() + ": " + e.getMessage());
            }
        }
    }

    private void carregarEquipamentoCompleto(Connection con, Aluguel a) {
        if (a.getEquipamento() != null && a.getEquipamento().getIdEquipamento() > 0) {
            try {
                Equipamento completo = equipamentoCol.buscarPorId(con, a.getEquipamento().getIdEquipamento());
                if (completo != null) {
                    a.setEquipamento(completo);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar equipamento ID " + a.getEquipamento().getIdEquipamento() + ": " + e.getMessage());
            }
        }
    }
}