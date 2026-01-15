package unioeste.caso2.servico.dao;

import unioeste.caso2.servico.bo.Atendente;
import unioeste.geral.endereco.bo.Endereco;
import unioeste.geral.pessoa.dao.PessoaFisicaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AtendenteDAO implements PessoaFisicaDAO<Atendente> {

    @Override
    public void inserir(Connection con, Atendente atendente) throws Exception {
        String sql = "INSERT INTO Atendente (nome, cpf, idEndereco, numero, complemento) VALUES (?, ?, ?, ?, ?) RETURNING idAtendente";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, atendente.getNome());
            stmt.setString(2, atendente.getCpf());
            stmt.setLong(3, atendente.getEndereco().getIdEndereco());
            stmt.setString(4, atendente.getNumero());
            stmt.setString(5, atendente.getComplemento());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    atendente.setIdPessoa(rs.getInt("idAtendente"));
                }
            }
        }
    }

    @Override
    public Atendente buscarPorId(Connection con, int id) throws Exception {
        String sql = "SELECT * FROM Atendente WHERE idAtendente = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarAtendente(rs);
            }
        }
        return null;
    }

    @Override
    public Atendente buscarPorCpf(Connection con, String cpf) throws Exception {
        String sql = "SELECT * FROM Atendente WHERE cpf = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarAtendente(rs);
            }
        }
        return null;
    }

    @Override
    public List<Atendente> buscarPorNome(Connection con, String nome) throws Exception {
        String sql = "SELECT * FROM Atendente WHERE UPPER(nome) LIKE UPPER(?)";
        List<Atendente> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(montarAtendente(rs));
            }
        }
        return lista;
    }

    private Atendente montarAtendente(ResultSet rs) throws Exception {
        Atendente a = new Atendente();
        a.setIdPessoa(rs.getInt("idAtendente"));
        a.setNome(rs.getString("nome"));
        a.setCpf(rs.getString("cpf"));
        a.setNumero(rs.getString("numero"));
        a.setComplemento(rs.getString("complemento"));
        
        Endereco end = new Endereco();
        end.setIdEndereco(rs.getInt("idEndereco"));
        a.setEndereco(end);
        
        return a;
    }
}