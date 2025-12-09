package unioeste.geral.endereco.dao;

import unioeste.apoio.bd.ConexaoBD; // Sua classe de infra
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.UnidadeFederativa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {

    // Método INSERIR
    public void inserir(Cidade cidade) throws Exception {
        // 1. Abrir conexão
        Connection conexao = ConexaoBD.getConexao();

        // 2. Criar o SQL (Note o RETURNING idCidade para pegar o ID gerado pelo Postgres)
        String sql = "INSERT INTO Cidade (nomeCidade, siglaUF) VALUES (?, ?) RETURNING idCidade";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);

            // 3. Preencher os ? com dados do objeto
            stmt.setString(1, cidade.getNomeCidade());

            // ATENÇÃO: Pegamos a PK de dentro do objeto UF que está dentro de Cidade
            stmt.setString(2, cidade.getUnidadeFederativa().getSiglaUF());

            // 4. Executar e pegar o ID gerado
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idGerado = rs.getInt("idCidade");
                cidade.setIdCidade(idGerado); // Atualiza o objeto Java com o ID do banco
            }

            // 5. Fechar tudo
            rs.close();
            stmt.close();
            conexao.close();

        } catch (SQLException e) {
            throw new Exception("Erro ao inserir cidade: " + e.getMessage());
        }
    }

    // Método BUSCAR POR ID
    public Cidade buscarPorId(int id) throws Exception {
        Connection conexao = ConexaoBD.getConexao();
        String sql = "SELECT * FROM Cidade WHERE idCidade = ?";

        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            Cidade cidade = null;

            if (rs.next()) {
                cidade = new Cidade();
                cidade.setIdCidade(rs.getInt("idCidade"));
                cidade.setNomeCidade(rs.getString("nomeCidade"));

                // Aqui temos um "problema": O banco só devolve a Sigla (String),
                // mas nosso objeto Cidade quer um objeto UnidadeFederativa.
                UnidadeFederativa uf = new UnidadeFederativa();
                uf.setSiglaUF(rs.getString("siglaUF"));
                // (Opcional: Poderíamos chamar UfDAO.buscar(sigla) aqui para trazer o nome da UF também)

                cidade.setUnidadeFederativa(uf);
            }

            conexao.close();
            return cidade;

        } catch (SQLException e) {
            throw new Exception("Erro ao buscar cidade: " + e.getMessage());
        }
    }
}