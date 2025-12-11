package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Logradouro;
import unioeste.geral.endereco.bo.TipoLogradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogradouroDAO {

    private final String SQL_SELECT_COMPLETO =
            "SELECT l.idLogradouro, l.nomeLogradouro, l.idTipoLogradouro, t.nomeTipoLogradouro " +
                    "FROM Logradouro l " +
                    "INNER JOIN TipoLogradouro t ON l.idTipoLogradouro = t.idTipoLogradouro ";

    private Logradouro montarLogradouro(ResultSet rs) throws Exception {
        Logradouro l = new Logradouro();
        l.setIdLogradouro(rs.getInt("idLogradouro"));
        l.setNomeLogradouro(rs.getString("nomeLogradouro"));

        TipoLogradouro t = new TipoLogradouro();
        t.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
        t.setNomeTipoLogradouro(rs.getString("nomeTipoLogradouro"));

        l.setTipoLogradouro(t);
        return l;
    }

    public void inserir(Connection con, Logradouro logradouro) throws Exception {
        String sql = "INSERT INTO Logradouro (nomeLogradouro, idTipoLogradouro) VALUES (?, ?) RETURNING idLogradouro";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, logradouro.getNomeLogradouro());
            stmt.setInt(2, logradouro.getTipoLogradouro().getIdTipoLogradouro());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    logradouro.setIdLogradouro(rs.getInt("idLogradouro"));
                }
            }
        }
    }

    public Logradouro buscarPorId(Connection con, int id) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE l.idLogradouro = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarLogradouro(rs);
            }
        }
        return null;
    }

    public Logradouro buscarPorNomeETipo(Connection con, String nome, int idTipo) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE UPPER(l.nomeLogradouro) = UPPER(?) AND l.idTipoLogradouro = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, idTipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarLogradouro(rs);
            }
        }
        return null;
    }
}