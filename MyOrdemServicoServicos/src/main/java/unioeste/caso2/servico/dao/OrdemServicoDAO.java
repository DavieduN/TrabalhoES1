package unioeste.caso2.servico.dao;

import unioeste.caso2.servico.bo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {

    private final String SQL_SELECT_HEADER = 
        "SELECT os.*, c.nome as nomeCliente, a.nome as nomeAtendente " +
        "FROM OrdemServico os " +
        "INNER JOIN Cliente c ON os.idCliente = c.idCliente " +
        "INNER JOIN Atendente a ON os.idAtendente = a.idAtendente ";

    public void registrar(Connection con, OrdemServico os) throws Exception {
        String sql = "INSERT INTO OrdemServico (dataEmissao, valorTotal, descricaoProblema, idCliente, idAtendente) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING nroOrdemServico";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(os.getDataEmissao()));
            stmt.setDouble(2, os.getValorTotal());
            stmt.setString(3, os.getDescricaoProblema());
            stmt.setInt(4, os.getCliente().getIdPessoa());
            stmt.setInt(5, os.getAtendente().getIdPessoa());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    os.setNroOrdemServico(rs.getInt("nroOrdemServico"));
                }
            }
        }
    }

    public List<OrdemServico> buscarTodas(Connection con) throws Exception {
        String sql = SQL_SELECT_HEADER + "ORDER BY os.dataEmissao DESC";
        List<OrdemServico> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarOrdemServico(rs));
            }
        }
        return lista;
    }

    public OrdemServico buscarPorNumero(Connection con, int nro) throws Exception {
        String sql = SQL_SELECT_HEADER + "WHERE os.nroOrdemServico = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarOrdemServico(rs);
            }
        }
        return null;
    }

    private OrdemServico montarOrdemServico(ResultSet rs) throws Exception {
        OrdemServico os = new OrdemServico();
        os.setNroOrdemServico(rs.getInt("nroOrdemServico"));
        os.setDataEmissao(rs.getDate("dataEmissao").toLocalDate());
        os.setValorTotal(rs.getDouble("valorTotal"));
        os.setDescricaoProblema(rs.getString("descricaoProblema"));

        Cliente c = new Cliente();
        c.setIdPessoa(rs.getInt("idCliente"));
        c.setNome(rs.getString("nomeCliente"));
        os.setCliente(c);

        Atendente a = new Atendente();
        a.setIdPessoa(rs.getInt("idAtendente"));
        a.setNome(rs.getString("nomeAtendente"));
        os.setAtendente(a);

        return os;
    }
}