package unioeste.caso1.aluguel.dao;

import unioeste.caso1.aluguel.bo.TipoEquipamento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoEquipamentoDAO {

    public void inserir(Connection con, TipoEquipamento tipo) throws Exception {
        String sql = "INSERT INTO TipoEquipamento (nomeTipoEquipamento) VALUES (?) RETURNING idTipoEquipamento";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tipo.getNomeTipoEquipamento());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipo.setIdTipoEquipamento(rs.getInt("idTipoEquipamento"));
                }
            }
        }
    }

    public TipoEquipamento buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM TipoEquipamento WHERE idTipoEquipamento = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarTipo(rs);
            }
        }
        return null;
    }

    public TipoEquipamento buscarPorNome(Connection con, String nome) throws Exception {
        String sql = "SELECT * FROM TipoEquipamento WHERE UPPER(nomeTipoEquipamento) = UPPER(?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarTipo(rs);
            }
        }
        return null;
    }

    public List<TipoEquipamento> buscarTodos(Connection con) throws Exception {
        String sql = "SELECT * FROM TipoEquipamento ORDER BY nomeTipoEquipamento";
        List<TipoEquipamento> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarTipo(rs));
            }
        }
        return lista;
    }

    private TipoEquipamento montarTipo(ResultSet rs) throws Exception {
        TipoEquipamento t = new TipoEquipamento();
        t.setIdTipoEquipamento(rs.getInt("idTipoEquipamento"));
        t.setNomeTipoEquipamento(rs.getString("nomeTipoEquipamento"));
        return t;
    }
}