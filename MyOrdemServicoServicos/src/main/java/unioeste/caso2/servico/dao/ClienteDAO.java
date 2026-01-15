package unioeste.caso2.servico.dao;

import unioeste.caso2.servico.bo.Cliente;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.pessoa.dao.PessoaFisicaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements PessoaFisicaDAO<Cliente> {

    @Override
    public void inserir(Connection con, Cliente cliente) throws Exception {
        String sql = "INSERT INTO Cliente (nome, cpf, idEndereco, numero, complemento) VALUES (?, ?, ?, ?, ?) RETURNING idCliente";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setLong(3, cliente.getEndereco().getIdEndereco());
            stmt.setString(4, cliente.getNumero());
            stmt.setString(5, cliente.getComplemento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente.setIdPessoa(rs.getInt("idCliente"));
                }
            }
        }
    }

    @Override
    public Cliente buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarCliente(rs);
            }
        }
        return null;
    }

    @Override
    public Cliente buscarPorCpf(Connection con, String cpf) throws Exception {
        String sql = "SELECT * FROM Cliente WHERE cpf = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarCliente(rs);
            }
        }
        return null;
    }

    @Override
    public List<Cliente> buscarPorNome(Connection con, String nome) throws Exception {
        String sql = "SELECT * FROM Cliente WHERE UPPER(nome) LIKE UPPER(?)";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarCliente(rs));
            }
        }
        return lista;
    }

    private Cliente montarCliente(ResultSet rs) throws Exception {
        Cliente c = new Cliente();
        c.setIdPessoa(rs.getInt("idCliente"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setNumero(rs.getString("numero"));
        c.setComplemento(rs.getString("complemento"));
        
        Endereco end = new Endereco();
        end.setIdEndereco(rs.getInt("idEndereco"));
        c.setEndereco(end);
        
        return c;
    }
}