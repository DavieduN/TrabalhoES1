package unioeste.caso2.servico.dao;

import unioeste.caso2.servico.bo.ItemServico;
import unioeste.caso2.servico.bo.TipoServico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemServicoDAO {

    public void inserir(Connection con, ItemServico item, int nroOrdemServico) throws Exception {
        String sql = "INSERT INTO ItemServico (valorServico, idTipoServico, nroOrdemServico) VALUES (?, ?, ?) RETURNING idItemServico";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDouble(1, item.getValorServico());
            stmt.setInt(2, item.getTipoServico().getIdTipoServico());
            stmt.setInt(3, nroOrdemServico);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    item.setIdItemServico(rs.getInt("idItemServico"));
                }
            }
        }
    }

    public List<ItemServico> buscarPorOrdemServico(Connection con, int nroOrdemServico) throws Exception {
        String sql = "SELECT i.*, t.nomeTipoServico " +
                     "FROM ItemServico i " +
                     "INNER JOIN TipoServico t ON i.idTipoServico = t.idTipoServico " +
                     "WHERE i.nroOrdemServico = ?";
        
        List<ItemServico> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nroOrdemServico);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemServico item = new ItemServico();
                    item.setIdItemServico(rs.getInt("idItemServico"));
                    item.setValorServico(rs.getDouble("valorServico"));
                    
                    TipoServico t = new TipoServico();
                    t.setIdTipoServico(rs.getInt("idTipoServico"));
                    t.setNomeTipoServico(rs.getString("nomeTipoServico"));
                    item.setTipoServico(t);
                    
                    lista.add(item);
                }
            }
        }
        return lista;
    }
}