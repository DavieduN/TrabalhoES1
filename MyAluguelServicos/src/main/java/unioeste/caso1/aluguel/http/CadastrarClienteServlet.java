package unioeste.caso1.aluguel.http;

import org.json.JSONArray;
import org.json.JSONObject;
import unioeste.caso1.aluguel.bo.Cliente;
import unioeste.caso1.aluguel.manager.UCClienteServicos;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.pessoa.bo.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clientes/cadastrar")
public class CadastrarClienteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        try {
            JSONObject json = new JSONObject(sb.toString());
            Cliente novoCliente = converterJsonParaCliente(json);

            UCClienteServicos servicos = new UCClienteServicos();
            Cliente clienteSalvo = servicos.cadastrarCliente(novoCliente);
            
            out.print(new JSONObject(clienteSalvo));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            JSONObject erro = new JSONObject();
            erro.put("erro", e.getMessage());
            out.print(erro);
        }
    }

    private Cliente converterJsonParaCliente(JSONObject json) {
        Cliente c = new Cliente();
        
        c.setIdPessoa(json.optInt("idCliente", 0));
        c.setNome(json.optString("nome"));
        c.setCpf(json.optString("cpf"));
        
        c.setNumero(json.optString("numero"));
        c.setComplemento(json.optString("complemento"));

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
            c.setEndereco(end);
        }

        if (json.has("telefones")) {
            JSONArray arrayTels = json.getJSONArray("telefones");
            List<Telefone> listaTels = new ArrayList<>();
            
            for (int i = 0; i < arrayTels.length(); i++) {
                JSONObject jsonTel = arrayTels.getJSONObject(i);
                Telefone t = new Telefone();
                t.setNumero(jsonTel.optString("numero"));
                
                if (jsonTel.has("ddd")) {
                    JSONObject jsonDdd = jsonTel.getJSONObject("ddd");
                    DDD ddd = new DDD();
                    ddd.setIdDdd(jsonDdd.optInt("idDdd", 0));
                    ddd.setDdd(jsonDdd.optInt("ddd", 0));
                    t.setDdd(ddd);
                }

                if (jsonTel.has("ddi")) {
                    JSONObject jsonDdi = jsonTel.getJSONObject("ddi");
                    DDI ddi = new DDI();
                    ddi.setIdDdi(jsonDdi.optInt("idDdi", 0));
                    ddi.setDdi(jsonDdi.optInt("ddi", 0));
                    t.setDdi(ddi);
                }
                listaTels.add(t);
            }
            c.setTelefones(listaTels);
        }

        if (json.has("emails")) {
            JSONArray arrayEmails = json.getJSONArray("emails");
            List<Email> listaEmails = new ArrayList<>();
            
            for (int i = 0; i < arrayEmails.length(); i++) {
                JSONObject jsonEmail = arrayEmails.getJSONObject(i);
                Email email = new Email();
                email.setEnderecoEmail(jsonEmail.optString("enderecoEmail"));
                listaEmails.add(email);
            }
            c.setEmails(listaEmails);
        }

        return c;
    }
}