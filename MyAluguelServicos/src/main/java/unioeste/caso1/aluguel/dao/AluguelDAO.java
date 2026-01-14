package unioeste.caso1.aluguel.dao;

import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.bo.Cliente;
import unioeste.caso1.aluguel.bo.Equipamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AluguelDAO {

    private final String SQL_SELECT_COMPLETO = 
        "SELECT a.*, c.nome as nomeCliente, e.nomeEquipamento, e.valorDiaria " +
        "FROM Aluguel a " +
        "INNER JOIN Cliente c ON a.idCliente = c.idCliente " +
        "INNER JOIN Equipamento e ON a.idEquipamento = e.idEquipamento ";

    public void registrar(Connection con, Aluguel aluguel) throws Exception {
        String sql = "INSERT INTO Aluguel (dataPedido, dataLocacao, dataDevolucao, valorTotalLocacao, idCliente, idEquipamento) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING nroAluguel";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(aluguel.getDataPedido()));
            stmt.setDate(2, Date.valueOf(aluguel.getDataLocacao()));
            stmt.setDate(3, Date.valueOf(aluguel.getDataDevolucao()));
            stmt.setDouble(4, aluguel.getValorTotalLocacao());
            stmt.setInt(5, aluguel.getCliente().getIdPessoa());
            stmt.setInt(6, aluguel.getEquipamento().getIdEquipamento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    aluguel.setNroAluguel(rs.getInt("nroAluguel"));
                }
            }
        }
    }

    public List<Aluguel> buscarTodos(Connection con) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "ORDER BY a.dataPedido DESC";
        List<Aluguel> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarAluguel(rs));
                }
            }
        }
        return lista;
    }

    public Aluguel buscarPorNumero(Connection con, int nroAluguel) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE a.nroAluguel = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nroAluguel);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarAluguel(rs);
                }
            }
        }
        return null;
    }

    private Aluguel montarAluguel(ResultSet rs) throws Exception {
        Aluguel a = new Aluguel();
        a.setNroAluguel(rs.getInt("nroAluguel")); 
        a.setDataPedido(rs.getDate("dataPedido").toLocalDate());
        a.setDataLocacao(rs.getDate("dataLocacao").toLocalDate());
        a.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
        a.setValorTotalLocacao(rs.getDouble("valorTotalLocacao"));

        Cliente c = new Cliente();
        c.setIdPessoa(rs.getInt("idCliente"));
        c.setNome(rs.getString("nomeCliente"));
        a.setCliente(c);

        Equipamento e = new Equipamento();
        e.setIdEquipamento(rs.getInt("idEquipamento"));
        e.setNomeEquipamento(rs.getString("nomeEquipamento"));
        e.setValorDiaria(rs.getDouble("valorDiaria"));
        a.setEquipamento(e);

        return a;
    }
}