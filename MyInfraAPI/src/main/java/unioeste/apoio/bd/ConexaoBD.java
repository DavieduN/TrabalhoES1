package unioeste.apoio.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexaoBD {

    public Connection getConexao() throws Exception {
        // esse try catch é só pros testes rodarem sem precisar ficar configurando separado
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/endereco");
            return ds.getConnection();

        } catch (NamingException e) {
            try {
                Class.forName("org.postgresql.Driver");
                // se rodar fora do docker mudar para localhost
                String url = "jdbc:postgresql://db:5432/trabalho_es";
                String user = "postgres";
                String pass = "senha_secreta";

                return DriverManager.getConnection(url, user, pass);

            } catch (Exception ex) {
                throw new Exception("FATAL: Não foi possível obter conexão via JNDI (Tomcat) nem JDBC Direto (Testes). " +
                        "Verifique se o banco está acessível em 'db:5432'. Erro original: " + e.getMessage());
            }
        }
    }

    public static void fecharConexao(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
        }

        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
        }

        try {
            if (conn != null) conn.close();
        } catch (Exception e) {
        }
    }
}