package unioeste.caso1.aluguel.dao;

import unioeste.caso1.aluguel.bo.Equipamento;
import unioeste.caso1.aluguel.bo.TipoEquipamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO {

    private final String SQL_SELECT_COMPLETO =
            "SELECT e.*, t.nomeTipoEquipamento " +
                    "FROM Equipamento e " +
                    "INNER JOIN TipoEquipamento t ON e.idTipoEquipamento = t.idTipoEquipamento ";

    public void inserir(Connection con, Equipamento equip) throws Exception {
        String sql = "INSERT INTO Equipamento (nomeEquipamento, valorDiaria, idTipoEquipamento) VALUES (?, ?, ?) RETURNING idEquipamento";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, equip.getNomeEquipamento());
            stmt.setDouble(2, equip.getValorDiaria());
            stmt.setInt(3, equip.getTipoEquipamento().getIdTipoEquipamento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    equip.setIdEquipamento(rs.getInt("idEquipamento"));
                }
            }
        }
    }

    public Equipamento buscarPorId(Connection con, int id) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE e.idEquipamento = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarEquipamento(rs);
            }
        }
        return null;
    }

    public List<Equipamento> buscarTodos(Connection con) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "ORDER BY e.nomeEquipamento";
        List<Equipamento> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarEquipamento(rs));
            }
        }
        return lista;
    }

    private Equipamento montarEquipamento(ResultSet rs) throws Exception {
        Equipamento e = new Equipamento();
        e.setIdEquipamento(rs.getInt("idEquipamento"));
        e.setNomeEquipamento(rs.getString("nomeEquipamento"));
        e.setValorDiaria(rs.getDouble("valorDiaria"));

        TipoEquipamento t = new TipoEquipamento();
        t.setIdTipoEquipamento(rs.getInt("idTipoEquipamento"));
        t.setNomeTipoEquipamento(rs.getString("nomeTipoEquipamento"));

        e.setTipoEquipamento(t);
        return e;
    }
}