package unioeste.geral.endereco.http;

import org.json.JSONObject;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/enderecos/cadastrar")
public class CadastrarEnderecoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        try {
            JSONObject json = new JSONObject(sb.toString());
            Endereco novoEnd = converterJsonParaObjeto(json);
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            Endereco enderecoSalvo = servicos.cadastrarEndereco(novoEnd);
            out.print(new JSONObject(enderecoSalvo));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private Endereco converterJsonParaObjeto(JSONObject json) {
        Endereco e = new Endereco();
        e.setCep(json.optString("cep"));

        if (json.has("idEndereco")) e.setIdEndereco(json.getInt("idEndereco"));

        if (json.has("cidade")) {
            JSONObject jsonCid = json.getJSONObject("cidade");
            Cidade c = new Cidade();
            if (jsonCid.has("idCidade")) c.setIdCidade(jsonCid.getInt("idCidade"));
            c.setNomeCidade(jsonCid.optString("nomeCidade"));
            if (jsonCid.has("unidadeFederativa")) {
                JSONObject jsonUf = jsonCid.getJSONObject("unidadeFederativa");
                UnidadeFederativa uf = new UnidadeFederativa();
                uf.setSiglaUF(jsonUf.optString("siglaUF"));
                c.setUnidadeFederativa(uf);
            }
            e.setCidade(c);
        }

        if (json.has("bairro")) {
            JSONObject jsonBairro = json.getJSONObject("bairro");
            Bairro b = new Bairro();
            if (jsonBairro.has("idBairro")) b.setIdBairro(jsonBairro.getInt("idBairro"));
            b.setNomeBairro(jsonBairro.optString("nomeBairro"));
            e.setBairro(b);
        }

        if (json.has("logradouro")) {
            JSONObject jsonLog = json.getJSONObject("logradouro");
            Logradouro l = new Logradouro();
            if (jsonLog.has("idLogradouro")) l.setIdLogradouro(jsonLog.getInt("idLogradouro"));
            l.setNomeLogradouro(jsonLog.optString("nomeLogradouro"));

            if (jsonLog.has("tipoLogradouro")) {
                JSONObject jsonTipo = jsonLog.getJSONObject("tipoLogradouro");
                TipoLogradouro t = new TipoLogradouro();
                if (jsonTipo.has("idTipoLogradouro")) t.setIdTipoLogradouro(jsonTipo.getInt("idTipoLogradouro"));
                t.setNomeTipoLogradouro(jsonTipo.optString("nomeTipoLogradouro"));
                l.setTipoLogradouro(t);
            }
            e.setLogradouro(l);
        }

        return e;
    }
}