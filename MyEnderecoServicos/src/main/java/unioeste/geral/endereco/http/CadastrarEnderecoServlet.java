package unioeste.geral.endereco.http;

import org.json.JSONObject;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/endereco/cadastrar")
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
            EnderecoEspecifico novoEnd = converterJsonParaObjeto(json);
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            servicos.cadastrarEndereco(novoEnd);
            out.print(new JSONObject(novoEnd));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private EnderecoEspecifico converterJsonParaObjeto(JSONObject json) {
        EnderecoEspecifico ee = new EnderecoEspecifico();
        ee.setNumero(json.optString("numero"));
        ee.setComplemento(json.optString("complemento"));

        if (json.has("endereco")) {
            JSONObject jsonEnd = json.getJSONObject("endereco");
            Endereco e = new Endereco();
            e.setCep(jsonEnd.optString("cep"));
            if (jsonEnd.has("idEndereco")) e.setIdEndereco(jsonEnd.getInt("idEndereco"));
            if (jsonEnd.has("cidade")) {
                JSONObject jsonCid = jsonEnd.getJSONObject("cidade");
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

            if (jsonEnd.has("bairro")) {
                JSONObject jsonBairro = jsonEnd.getJSONObject("bairro");
                Bairro b = new Bairro();
                if (jsonBairro.has("idBairro")) b.setIdBairro(jsonBairro.getInt("idBairro"));
                b.setNomeBairro(jsonBairro.optString("nomeBairro"));
                e.setBairro(b);
            }

            if (jsonEnd.has("logradouro")) {
                JSONObject jsonLog = jsonEnd.getJSONObject("logradouro");
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
            ee.setEndereco(e);
        }
        return ee;
    }
}