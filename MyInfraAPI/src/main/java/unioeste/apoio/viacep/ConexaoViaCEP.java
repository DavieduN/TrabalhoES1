package unioeste.apoio.viacep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexaoViaCEP {

    public String obterConteudoBruto(String cep) throws Exception {
        String cepLimpo = cep.replaceAll("\\D", "");

        String urlChamada = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
        URL url = new URL(urlChamada);

        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setConnectTimeout(5000);

        if (conexao.getResponseCode() != 200) {
            throw new Exception("Erro HTTP: " + conexao.getResponseCode());
        }

        BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "UTF-8"));
        StringBuilder resposta = new StringBuilder();
        String linha;

        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();

        return resposta.toString();
    }
}