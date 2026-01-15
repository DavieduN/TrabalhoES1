package unioeste.caso2.servico.http;

import org.json.JSONArray;
import org.json.JSONObject;
import unioeste.caso2.servico.bo.Atendente;
import unioeste.caso2.servico.manager.UCAtendenteServicos;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.pessoa.bo.Email;
import unioeste.geral.pessoa.bo.Telefone;
import unioeste.geral.pessoa.bo.DDD;
import unioeste.geral.pessoa.bo.DDI;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/atendentes/cadastrar")
public class CadastrarAtendenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);

        try (PrintWriter out = resp.getWriter()) {
            JSONObject json = new JSONObject(sb.toString());
            Atendente atendente = converterJsonParaAtendente(json);
            
            UCAtendenteServicos servicos = new UCAtendenteServicos();
            Atendente salvo = servicos.cadastrarAtendente(atendente);
            
            out.print(new JSONObject(salvo));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private Atendente converterJsonParaAtendente(JSONObject json) {
        Atendente a = new Atendente();
        a.setIdPessoa(json.optInt("idAtendente", json.optInt("idPessoa", 0)));
        a.setNome(json.optString("nome"));
        a.setCpf(json.optString("cpf"));
        
        a.setNumero(json.optString("numero"));
        a.setComplemento(json.optString("complemento"));

        if (json.has("endereco")) {
            JSONObject jsonEnd = json.getJSONObject("endereco");
            Endereco end = new Endereco();
            end.setCep(jsonEnd.optString("cep"));
            end.setIdEndereco(jsonEnd.optInt("idEndereco", 0));

            if (jsonEnd.has("cidade")) {
                JSONObject jsonCid = jsonEnd.getJSONObject("cidade");
                Cidade cid = new Cidade();
                cid.setIdCidade(jsonCid.optInt("idCidade", 0));
                cid.setNomeCidade(jsonCid.optString("nomeCidade"));
                
                if (jsonCid.has("unidadeFederativa")) {
                    JSONObject jsonUf = jsonCid.getJSONObject("unidadeFederativa");
                    UnidadeFederativa uf = new UnidadeFederativa();
                    uf.setSiglaUF(jsonUf.optString("siglaUF"));
                    cid.setUnidadeFederativa(uf);
                }
                end.setCidade(cid);
            }
            
            if (jsonEnd.has("bairro")) {
                JSONObject jsonBairro = jsonEnd.getJSONObject("bairro");
                Bairro b = new Bairro();
                b.setIdBairro(jsonBairro.optInt("idBairro", 0));
                b.setNomeBairro(jsonBairro.optString("nomeBairro"));
                end.setBairro(b);
            }

            if (jsonEnd.has("logradouro")) {
                JSONObject jsonLog = jsonEnd.getJSONObject("logradouro");
                Logradouro l = new Logradouro();
                l.setIdLogradouro(jsonLog.optInt("idLogradouro", 0));
                l.setNomeLogradouro(jsonLog.optString("nomeLogradouro"));
                
                if (jsonLog.has("tipoLogradouro")) {
                    JSONObject jsonTipo = jsonLog.getJSONObject("tipoLogradouro");
                    TipoLogradouro t = new TipoLogradouro();
                    t.setIdTipoLogradouro(jsonTipo.optInt("idTipoLogradouro", 0));
                    t.setNomeTipoLogradouro(jsonTipo.optString("nomeTipoLogradouro"));
                    l.setTipoLogradouro(t);
                }
                end.setLogradouro(l);
            }
            a.setEndereco(end);
        }

        if (json.has("telefones")) {
            JSONArray arr = json.getJSONArray("telefones");
            List<Telefone> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject j = arr.getJSONObject(i);
                Telefone t = new Telefone();
                t.setNumero(j.optString("numero"));
                if (j.has("ddd")) {
                    t.setDdd(new DDD());
                    t.getDdd().setDdd(j.getJSONObject("ddd").optInt("ddd", 0));
                }
                if (j.has("ddi")) {
                    t.setDdi(new DDI());
                    t.getDdi().setDdi(j.getJSONObject("ddi").optInt("ddi", 0));
                }
                lista.add(t);
            }
            a.setTelefones(lista);
        }

        if (json.has("emails")) {
            JSONArray arr = json.getJSONArray("emails");
            List<Email> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                Email e = new Email();
                e.setEnderecoEmail(arr.getJSONObject(i).optString("enderecoEmail"));
                lista.add(e);
            }
            a.setEmails(lista);
        }

        return a;
    }
}