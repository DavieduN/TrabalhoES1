package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.TipoLogradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoLogradouroDAO {

    public void inserir(Connection con, TipoLogradouro tipo) throws Exception {
        String sql = "INSERT INTO TipoLogradouro (nomeTipoLogradouro) VALUES (?) RETURNING idTipoLogradouro";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tipo.getNomeTipoLogradouro());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tipo.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
            }
        }
    }

    public TipoLogradouro buscarPorId(int id) throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM TipoLogradouro WHERE idTipoLogradouro = ?";
        TipoLogradouro tipo = null;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tipo = new TipoLogradouro();
                tipo.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
                tipo.setNomeTipoLogradouro(rs.getString("nomeTipoLogradouro"));
            }
        } finally {
            con.close();
        }
        return tipo;
    }

    public List<TipoLogradouro> buscarTodos() throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = "SELECT * FROM TipoLogradouro";
        List<TipoLogradouro> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoLogradouro tipo = new TipoLogradouro();
                tipo.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
                tipo.setNomeTipoLogradouro(rs.getString("nomeTipoLogradouro"));
                lista.add(tipo);
            }
        } finally {
            con.close();
        }
        return lista;
    }
}