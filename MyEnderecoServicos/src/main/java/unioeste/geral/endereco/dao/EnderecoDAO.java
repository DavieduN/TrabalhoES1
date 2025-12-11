package unioeste.geral.endereco.dao;

import unioeste.geral.endereco.bo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    private final String SQL_SELECT_COMPLETO =
            "SELECT e.idEndereco, e.cep, " +
                    "       c.idCidade, c.nomeCidade, c.siglaUF, u.nomeUF, " +
                    "       b.idBairro, b.nomeBairro, " +
                    "       l.idLogradouro, l.nomeLogradouro, l.idTipoLogradouro, t.nomeTipoLogradouro " +
                    "FROM Endereco e " +
                    "INNER JOIN Cidade c ON e.idCidade = c.idCidade " +
                    "INNER JOIN UnidadeFederativa u ON c.siglaUF = u.siglaUF " +
                    "INNER JOIN Bairro b ON e.idBairro = b.idBairro " +
                    "INNER JOIN Logradouro l ON e.idLogradouro = l.idLogradouro " +
                    "INNER JOIN TipoLogradouro t ON l.idTipoLogradouro = t.idTipoLogradouro ";

    private Endereco montarEndereco(ResultSet rs) throws Exception {
        Endereco endereco = new Endereco();
        endereco.setIdEndereco(rs.getInt("idEndereco"));
        endereco.setCep(rs.getString("cep"));

        Cidade c = new Cidade();
        c.setIdCidade(rs.getInt("idCidade"));
        c.setNomeCidade(rs.getString("nomeCidade"));

        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSiglaUF(rs.getString("siglaUF"));
        uf.setNomeUF(rs.getString("nomeUF"));
        c.setUnidadeFederativa(uf);
        endereco.setCidade(c);

        Bairro b = new Bairro();
        b.setIdBairro(rs.getInt("idBairro"));
        b.setNomeBairro(rs.getString("nomeBairro"));
        endereco.setBairro(b);

        Logradouro l = new Logradouro();
        l.setIdLogradouro(rs.getInt("idLogradouro"));
        l.setNomeLogradouro(rs.getString("nomeLogradouro"));

        TipoLogradouro t = new TipoLogradouro();
        t.setIdTipoLogradouro(rs.getInt("idTipoLogradouro"));
        t.setNomeTipoLogradouro(rs.getString("nomeTipoLogradouro"));
        l.setTipoLogradouro(t);
        endereco.setLogradouro(l);

        return endereco;
    }

    public void inserir(Connection con, Endereco endereco) throws Exception {
        String sql = "INSERT INTO Endereco (cep, idCidade, idBairro, idLogradouro) VALUES (?, ?, ?, ?) RETURNING idEndereco";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, endereco.getCep());
            stmt.setInt(2, endereco.getCidade().getIdCidade());
            stmt.setInt(3, endereco.getBairro().getIdBairro());
            stmt.setInt(4, endereco.getLogradouro().getIdLogradouro());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                endereco.setIdEndereco(rs.getInt("idEndereco"));
            }
        }
    }

    public Endereco buscarPorId(Connection con, int id) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE e.idEndereco = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarEndereco(rs);
            }
        }
        return null;
    }

    public List<Endereco> buscarPorCep(Connection con, String cep) throws Exception {
        String sql = SQL_SELECT_COMPLETO + "WHERE e.cep = ?";
        List<Endereco> enderecos = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cep);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) enderecos.add(montarEndereco(rs));
            }
        }
        return enderecos;
    }

    public Endereco buscarPorCombinacaoUnica(Connection con, Endereco endereco) throws Exception {
        String sql = SQL_SELECT_COMPLETO +
                "WHERE e.cep = ? AND e.idCidade = ? AND e.idBairro = ? AND e.idLogradouro = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, endereco.getCep());
            stmt.setInt(2, endereco.getCidade().getIdCidade());
            stmt.setInt(3, endereco.getBairro().getIdBairro());
            stmt.setInt(4, endereco.getLogradouro().getIdLogradouro());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return montarEndereco(rs);
            }
        }
        return null;
    }
}