package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.TipoLogradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoLogradouroDAO {

    private TipoLogradouro montarTipoLogradouro(ResultSet rs) throws Exception{
        TipoLogradouro tipo = new TipoLogradouro();
        tipo.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
        tipo.setNomeTipoLogradouro(rs.getString("nomeTipoLogradouro"));
        return tipo;
    }

    public TipoLogradouro buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM TipoLogradouro WHERE idTipoLogradouro = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarTipoLogradouro(rs);
                }
            }
        }
        return null;
    }

    public TipoLogradouro buscarPorNome(Connection con, String nome) throws Exception {
        String sql = "SELECT * FROM TipoLogradouro WHERE UPPER(nomeTipoLogradouro) = UPPER(?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarTipoLogradouro(rs);
                }
            }
        }
        return null;
    }

    public List<TipoLogradouro> buscarTodos(Connection con) throws Exception {
        String sql = "SELECT * FROM TipoLogradouro ORDER BY nomeTipoLogradouro";
        List<TipoLogradouro> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) lista.add(montarTipoLogradouro(rs));
            }
        }
        return lista;
    }
}