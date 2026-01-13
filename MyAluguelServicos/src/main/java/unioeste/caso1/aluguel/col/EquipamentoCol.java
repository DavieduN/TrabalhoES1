package unioeste.caso1.aluguel.col;

import unioeste.caso1.aluguel.bo.Equipamento;
import unioeste.caso1.aluguel.bo.TipoEquipamento;
import unioeste.caso1.aluguel.dao.EquipamentoDAO;

import java.sql.Connection;
import java.util.List;

public class EquipamentoCol {

    private final EquipamentoDAO dao;
    private final TipoEquipamentoCol tipoCol;

    public EquipamentoCol() {
        this.dao = new EquipamentoDAO();
        this.tipoCol = new TipoEquipamentoCol();
    }

    public void cadastrar(Connection con, Equipamento equipamento) throws Exception {
        if (equipamento == null) {
            throw new Exception("Dados do equipamento são obrigatórios.");
        }
        if (equipamento.getNomeEquipamento() == null || equipamento.getNomeEquipamento().trim().isEmpty()) {
            throw new Exception("Nome do equipamento é obrigatório.");
        }
        if (equipamento.getValorDiaria() <= 0) {
            throw new Exception("O valor da diária deve ser positivo.");
        }

        if (equipamento.getTipoEquipamento() == null) {
            throw new Exception("O tipo de equipamento deve ser informado.");
        }

        TipoEquipamento tipoValidado = tipoCol.validarExistencia(con, equipamento.getTipoEquipamento());
        equipamento.setTipoEquipamento(tipoValidado);

        dao.inserir(con, equipamento);
    }

    public List<Equipamento> listarDisponiveis(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }

    public Equipamento buscarPorId(Connection con, int id) throws Exception {
        if (id <= 0) throw new Exception("ID inválido.");
        return dao.buscarPorId(con, id);
    }
}