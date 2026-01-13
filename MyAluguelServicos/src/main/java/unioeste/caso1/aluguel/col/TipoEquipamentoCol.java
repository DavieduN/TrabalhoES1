package unioeste.caso1.aluguel.col;

import unioeste.caso1.aluguel.bo.TipoEquipamento;
import unioeste.caso1.aluguel.dao.TipoEquipamentoDAO;
import java.sql.Connection;
import java.util.List;

public class TipoEquipamentoCol {

    private final TipoEquipamentoDAO dao;

    public TipoEquipamentoCol() {
        this.dao = new TipoEquipamentoDAO();
    }

    public TipoEquipamento obterOuCadastrar(Connection con, TipoEquipamento tipo) throws Exception {
        if (tipo == null) {
            throw new Exception("Tipo de equipamento não pode ser nulo.");
        }

        if (tipo.getIdTipoEquipamento() > 0) {
            return validarExistencia(con, tipo);
        }

        if (tipo.getNomeTipoEquipamento() == null || tipo.getNomeTipoEquipamento().trim().isEmpty()) {
            throw new Exception("Nome do tipo de equipamento é obrigatório.");
        }

        TipoEquipamento existente = dao.buscarPorNome(con, tipo.getNomeTipoEquipamento().trim());

        if (existente != null) {
            return existente;
        }

        dao.inserir(con, tipo);
        return tipo;
    }

    public List<TipoEquipamento> listarTodos(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }

    public TipoEquipamento validarExistencia(Connection con, TipoEquipamento tipo) throws Exception {
        if (tipo == null) throw new Exception("Tipo de equipamento obrigatório.");

        if (tipo.getIdTipoEquipamento() <= 0 && tipo.getNomeTipoEquipamento() != null) {
            TipoEquipamento existente = dao.buscarPorNome(con, tipo.getNomeTipoEquipamento().trim());
            if (existente != null) return existente;
        }

        if (tipo.getIdTipoEquipamento() > 0) {
            TipoEquipamento existente = dao.buscarPorId(con, tipo.getIdTipoEquipamento());
            if (existente == null) {
                throw new Exception("Tipo de equipamento não encontrado para o ID: " + tipo.getIdTipoEquipamento());
            }
            return existente;
        }

        throw new Exception("Tipo de Equipamento inválido (Informe ID ou Nome existente).");
    }
}