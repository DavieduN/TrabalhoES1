package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.Bairro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {

    public Bairro inserir(Connection con, Bairro bairro) throws Exception {
        String sql = "INSERT INTO Bairro (nomeBairro) VALUES (?) RETURNING idBairro";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, bairro.getNomeBairro());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bairro.setIdBairro(rs.getInt("idBairro"));
                }
            }
        }
        return bairro;
    }

    public Bairro buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM Bairro WHERE idBairro = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bairro bairro = new Bairro();
                    bairro.setIdBairro(rs.getInt("idBairro"));
                    bairro.setNomeBairro(rs.getString("nomeBairro"));
                    return bairro;
                }
            }
        }
        return null;
    }

    public Bairro buscarPorNome(Connection con, String nomeBairro) throws Exception {
        String sql = "SELECT * FROM Bairro WHERE UPPER(nomeBairro) = UPPER(?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nomeBairro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bairro b = new Bairro();
                    b.setIdBairro(rs.getInt("idBairro"));
                    b.setNomeBairro(rs.getString("nomeBairro"));
                    return b;
                }
            }
        }
        return null;
    }
}