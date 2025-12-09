package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.Bairro;
import unioeste.geral.endereco.bo.UnidadeFederativa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {

    public void inserir(Connection con, Bairro bairro) throws Exception {
        String sql = "INSERT INTO Bairro (nomeBairro) VALUES (?) RETURNING idBairro";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, bairro.getNomeBairro());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) bairro.setIdBairro(rs.getInt("idBairro"));
        }
    }

    public Bairro buscarPorId(int id) throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM Bairro WHERE idBairro = ?";
        Bairro bairro = null;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bairro = new Bairro();
                bairro.setIdBairro(rs.getInt("idBairro"));
                bairro.setNomeBairro(rs.getString("nomeBairro"));
            }
        } finally {
            con.close();
        }
        return bairro;
    }

    public List<Bairro> buscarTodos() throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM Bairro";
        List<Bairro> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bairro bairro = new Bairro();
                bairro.setIdBairro(rs.getInt("idBairro"));
                bairro.setNomeBairro(rs.getString("nomeBairro"));
                lista.add(bairro);
            }
        } finally {
            con.close();
        }
        return lista;
    }
}