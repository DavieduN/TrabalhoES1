package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.geral.endereco.bo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class EnderecoEspecificoDAO {
    private final String SQL_SELECT_COMPLETO =
            "SELECT ee.idEnderecoEspecifico, ee.numero, ee.complemento, " +
            "       e.idEndereco, e.cep, " +
            "       c.idCidade, c.nomeCidade, c.siglaUF, u.nomeUF, " +
            "       b.idBairro, b.nomeBairro, " +
            "       l.idLogradouro, l.nomeLogradouro, l.idTipoLogradouro, t.nomeTipoLogradouro " +
            "FROM EnderecoEspecifico ee " +
            "INNER JOIN Endereco e ON ee.idEndereco = e.idEndereco " +
            "INNER JOIN Cidade c ON e.idCidade = c.idCidade " +
            "INNER JOIN UnidadeFederativa u ON c.siglaUF = u.siglaUF " +
            "INNER JOIN Bairro b ON e.idBairro = b.idBairro " +
            "INNER JOIN Logradouro l ON e.idLogradouro = l.idLogradouro " +
            "INNER JOIN TipoLogradouro t ON l.idTipoLogradouro = t.idTipoLogradouro ";

    private EnderecoEspecifico montarEnderecoEspecifico(ResultSet rs) throws Exception {
        EnderecoEspecifico endEsp = new EnderecoEspecifico();
        endEsp.setIdEnderecoEspecifico(rs.getInt("idEnderecoEspecifico"));
        endEsp.setNumero(rs.getString("numero"));
        endEsp.setComplemento(rs.getString("complemento"));

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

        endEsp.setEndereco(endereco);

        return endEsp;
    }

    public void inserir(Connection con, EnderecoEspecifico endEsp) throws Exception {
        String sql = "INSERT INTO EnderecoEspecifico (numero, complemento, idEndereco) VALUES (?, ?, ?) RETURNING idEnderecoEspecifico";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, endEsp.getNumero());
            stmt.setString(2, endEsp.getComplemento());
            stmt.setInt(3, endEsp.getEndereco().getIdEndereco());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) endEsp.setIdEnderecoEspecifico(rs.getInt("idEnderecoEspecifico"));
        }
    }

    public EnderecoEspecifico buscarPorId(int id) throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = SQL_SELECT_COMPLETO + "WHERE ee.idEnderecoEspecifico = ?";
        EnderecoEspecifico endEsp = null;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) endEsp = montarEnderecoEspecifico(rs);
        } finally {
            con.close();
        }
        return endEsp;
    }

    public List<EnderecoEspecifico> buscarPorCep(String cep) throws Exception {
        Connection con = ConexaoBD.getConexao();
        String sql = SQL_SELECT_COMPLETO + "WHERE e.cep = ?";
        List<EnderecoEspecifico> lista = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cep);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(montarEnderecoEspecifico(rs));
            }
        } finally {
            con.close();
        }
        return lista;
    }
}