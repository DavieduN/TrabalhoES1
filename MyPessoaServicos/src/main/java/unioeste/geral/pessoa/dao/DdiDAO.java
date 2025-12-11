package unioeste.geral.pessoa.dao;

import unioeste.geral.pessoa.bo.DDI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DdiDAO {

    public DDI buscarPorNumero(Connection con, int numeroDdi) throws Exception {
        String sql = "SELECT * FROM DDI WHERE ddi = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, numeroDdi);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarDDI(rs);
                }
            }
        }
        return null;
    }

    public DDI buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM DDI WHERE idDdi = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarDDI(rs);
                }
            }
        }
        return null;
    }

    private DDI montarDDI(ResultSet rs) throws Exception {
        DDI ddi = new DDI();
        ddi.setIdDdi(rs.getInt("idDdi"));
        ddi.setDdi(rs.getInt("ddi"));
        return ddi;
    }
}