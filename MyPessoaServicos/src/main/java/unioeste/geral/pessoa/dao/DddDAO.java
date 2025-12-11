package unioeste.geral.pessoa.dao;

import unioeste.geral.pessoa.bo.DDD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DddDAO {

    public DDD buscarPorNumero(Connection con, int numeroDdd) throws Exception {
        String sql = "SELECT * FROM DDD WHERE ddd = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, numeroDdd);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarDDD(rs);
            }
        }
        return null;
    }

    public DDD buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM DDD WHERE idDdd = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarDDD(rs);
            }
        }
        return null;
    }

    private DDD montarDDD(ResultSet rs) throws Exception {
        DDD ddd = new DDD();
        ddd.setIdDdd(rs.getInt("idDdd"));
        ddd.setDdd(rs.getInt("ddd"));
        return ddd;
    }
}