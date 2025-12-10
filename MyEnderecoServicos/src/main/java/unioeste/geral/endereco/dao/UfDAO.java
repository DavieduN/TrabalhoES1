package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.UnidadeFederativa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UfDAO {

    public void inserir(Connection con, UnidadeFederativa uf) throws Exception {
        String sql = "INSERT INTO UnidadeFederativa (siglaUF, nomeUF) VALUES (?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, uf.getSiglaUF());
            stmt.setString(2, uf.getNomeUF());
            stmt.executeUpdate();
        }
    }

    public UnidadeFederativa buscarPorSigla(String sigla) throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM UnidadeFederativa WHERE siglaUF = ?";
        UnidadeFederativa uf = null;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sigla);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                uf = new UnidadeFederativa();
                uf.setSiglaUF(rs.getString("siglaUF"));
                uf.setNomeUF(rs.getString("nomeUF"));
            }
        } finally {
            con.close();
        }
        return uf;
    }

    public UnidadeFederativa buscarPorSigla(Connection con, String sigla) throws Exception {
        String sql = "SELECT * FROM UnidadeFederativa WHERE siglaUF = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sigla);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new UnidadeFederativa(rs.getString("siglaUF"), rs.getString("nomeUF"));
        }
        return null;
    }

    public List<UnidadeFederativa> buscarTodos() throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM UnidadeFederativa ORDER BY siglaUF";
        List<UnidadeFederativa> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UnidadeFederativa uf = new UnidadeFederativa();
                uf.setSiglaUF(rs.getString("siglaUF"));
                uf.setNomeUF(rs.getString("nomeUF"));
                lista.add(uf);
            }
        } finally {
            con.close();
        }
        return lista;
    }
}