package unioeste.apoio.bd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBD {

    public static Connection getConexao() throws Exception {
        Properties props = new Properties();
        try (InputStream entrada = ConexaoBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (entrada == null) {
                throw new Exception("O arquivo 'db.properties' não foi encontrado.");
            }
            props.load(entrada);
        } catch (IOException e) {
            throw new Exception("Erro ao ler arquivo de configuração: " + e.getMessage());
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        String driver = props.getProperty("db.driver");

        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver do banco não encontrado (" + driver + "): " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Erro ao conectar no Postgres: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = getConexao();
            System.out.println("Conectado com sucesso.");
            con.close();
        } catch (Exception e) {
            System.err.println("Erro:");
            e.printStackTrace();
        }
    }
}