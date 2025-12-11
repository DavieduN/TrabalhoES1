package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.UnidadeFederativa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UfDAO {

    private UnidadeFederativa montarUnidadeFederativa(ResultSet rs) throws Exception{
        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSiglaUF(rs.getString("siglaUF"));
        uf.setNomeUF(rs.getString("nomeUF"));
        return uf;
    }

    public UnidadeFederativa buscarPorSigla(Connection con, String sigla) throws Exception {
        String sql = "SELECT * FROM UnidadeFederativa WHERE UPPER(siglaUF) = UPPER(?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, sigla);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarUnidadeFederativa(rs);
            }
        }
        return null;
    }

    public List<UnidadeFederativa> buscarTodos(Connection con) throws Exception {
        String sql = "SELECT * FROM UnidadeFederativa ORDER BY siglaUF";
        List<UnidadeFederativa> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) lista.add(montarUnidadeFederativa(rs));
            }
        }
        return lista;
    }
}