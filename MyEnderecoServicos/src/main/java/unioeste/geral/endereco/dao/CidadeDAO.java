package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.UnidadeFederativa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {
    private final String SQL_SELECT_COMPLETO =
        "SELECT c.idCidade, c.nomeCidade, c.siglaUF, u.nomeUF " +
        "FROM Cidade c " +
        "INNER JOIN UnidadeFederativa u ON c.siglaUF = u.siglaUF ";

    private Cidade montarCidade(ResultSet rs) throws Exception {
        Cidade c = new Cidade();
        c.setIdCidade(rs.getInt("idCidade"));
        c.setNomeCidade(rs.getString("nomeCidade"));

        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSiglaUF(rs.getString("siglaUF"));
        uf.setNomeUF(rs.getString("nomeUF"));

        c.setUnidadeFederativa(uf);
        return c;
    }

    public void inserir(Connection con, Cidade cidade) throws Exception {
        String sql = "INSERT INTO Cidade (nomeCidade, siglaUF) VALUES (?, ?) RETURNING idCidade";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cidade.getNomeCidade());
            stmt.setString(2, cidade.getUnidadeFederativa().getSiglaUF());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cidade.setIdCidade(rs.getInt("idCidade"));
            }
        }
    }

    public Cidade buscarPorId(Connection con, int id) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE c.idCidade = ?";
        Cidade cidade = null;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) cidade = montarCidade(rs);
            }
        }
        return cidade;
    }

    public Cidade buscarPorNomeSigla(Connection con, String nomeCidade, String siglaUF) throws Exception {
        String sql = SQL_SELECT_COMPLETO + " WHERE UPPER(c.nomeCidade) = UPPER(?) AND UPPER(c.siglaUF) = UPPER(?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nomeCidade);
            stmt.setString(2, siglaUF);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarCidade(rs);
            }
        }
        return null;
    }
}