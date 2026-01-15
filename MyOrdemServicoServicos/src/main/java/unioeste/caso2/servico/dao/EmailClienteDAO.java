package unioeste.caso2.servico.dao;

import unioeste.geral.pessoa.bo.Email;
import unioeste.geral.pessoa.dao.EmailDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmailClienteDAO implements EmailDAO {

    @Override
    public void inserir(Connection con, Email email, int idPessoa) throws Exception {
        String sql = "INSERT INTO EmailCliente (enderecoEmail, idCliente) VALUES (?, ?) RETURNING idEmail";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email.getEnderecoEmail());
            stmt.setInt(2, idPessoa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    email.setIdEmail(rs.getInt("idEmail"));
                }
            }
        }
    }

    @Override
    public List<Email> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        String sql = "SELECT * FROM EmailCliente WHERE idCliente = ?";
        List<Email> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Email email = new Email();
                    email.setIdEmail(rs.getInt("idEmail"));
                    email.setEnderecoEmail(rs.getString("enderecoEmail"));
                    lista.add(email);
                }
            }
        }
        return lista;
    }
}