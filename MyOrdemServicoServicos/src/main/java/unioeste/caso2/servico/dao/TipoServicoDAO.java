package unioeste.caso2.servico.dao;

import unioeste.caso2.servico.bo.TipoServico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoServicoDAO {

    public void inserir(Connection con, TipoServico tipo) throws Exception {
        String sql = "INSERT INTO TipoServico (nomeTipoServico) VALUES (?) RETURNING idTipoServico";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tipo.getNomeTipoServico());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipo.setIdTipoServico(rs.getInt("idTipoServico"));
                }
            }
        }
    }

    public TipoServico buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM TipoServico WHERE idTipoServico = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarTipo(rs);
            }
        }
        return null;
    }

    public TipoServico buscarPorNome(Connection con, String nome) throws Exception {
        String sql = "SELECT * FROM TipoServico WHERE UPPER(nomeTipoServico) = UPPER(?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarTipo(rs);
            }
        }
        return null;
    }

    public List<TipoServico> buscarTodos(Connection con) throws Exception {
        String sql = "SELECT * FROM TipoServico ORDER BY nomeTipoServico";
        List<TipoServico> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarTipo(rs));
            }
        }
        return lista;
    }

    private TipoServico montarTipo(ResultSet rs) throws Exception {
        TipoServico t = new TipoServico();
        t.setIdTipoServico(rs.getInt("idTipoServico"));
        t.setNomeTipoServico(rs.getString("nomeTipoServico"));
        return t;
    }
}