package unioeste.caso2.servico.dao;

import unioeste.geral.pessoa.bo.DDD;
import unioeste.geral.pessoa.bo.DDI;
import unioeste.geral.pessoa.bo.Telefone;
import unioeste.geral.pessoa.dao.TelefoneDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TelefoneAtendenteDAO implements TelefoneDAO {

    @Override
    public void inserir(Connection con, Telefone telefone, int idPessoa) throws Exception {
        String sql = "INSERT INTO TelefoneAtendente (numero, idDdi, idDdd, idAtendente) VALUES (?, ?, ?, ?) RETURNING idTelefone";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, telefone.getNumero());
            stmt.setInt(2, telefone.getDdi().getIdDdi());
            stmt.setInt(3, telefone.getDdd().getIdDdd());
            stmt.setInt(4, idPessoa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    telefone.setIdTelefone(rs.getInt("idTelefone"));
                }
            }
        }
    }

    @Override
    public List<Telefone> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        String sql = "SELECT t.*, d1.ddi, d2.ddd " +
                     "FROM TelefoneAtendente t " +
                     "INNER JOIN DDI d1 ON t.idDdi = d1.idDdi " +
                     "INNER JOIN DDD d2 ON t.idDdd = d2.idDdd " +
                     "WHERE t.idAtendente = ?";
                     
        List<Telefone> lista = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Telefone t = new Telefone();
                    t.setIdTelefone(rs.getInt("idTelefone"));
                    t.setNumero(rs.getString("numero"));
                    
                    DDI ddi = new DDI();
                    ddi.setIdDdi(rs.getInt("idDdi"));
                    ddi.setDdi(rs.getInt("ddi"));
                    t.setDdi(ddi);
                    
                    DDD ddd = new DDD();
                    ddd.setIdDdd(rs.getInt("idDdd"));
                    ddd.setDdd(rs.getInt("ddd"));
                    t.setDdd(ddd);
                    
                    lista.add(t);
                }
            }
        }
        return lista;
    }
}