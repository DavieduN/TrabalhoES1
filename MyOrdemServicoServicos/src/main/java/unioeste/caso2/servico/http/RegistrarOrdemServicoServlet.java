package unioeste.caso2.servico.http;

import org.json.JSONArray;
import org.json.JSONObject;
import unioeste.caso2.servico.bo.*;
import unioeste.caso2.servico.manager.UCOrdemServicoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet("/ordem-servico/registrar")
public class RegistrarOrdemServicoServlet extends HttpServlet {

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
            OrdemServico os = converterJsonParaOS(json);
            
            UCOrdemServicoServicos servicos = new UCOrdemServicoServicos();
            OrdemServico salva = servicos.registrarOS(os);
            
            out.print(new JSONObject(salva));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private OrdemServico converterJsonParaOS(JSONObject json) {
        OrdemServico os = new OrdemServico();
        os.setNroOrdemServico(json.optInt("nroOrdemServico", 0));
        os.setDescricaoProblema(json.optString("descricaoProblema"));
        
        if (json.has("dataEmissao")) os.setDataEmissao(LocalDate.parse(json.getString("dataEmissao")));
        
        if (json.has("cliente")) {
            Cliente c = new Cliente();
            c.setIdPessoa(json.getJSONObject("cliente").optInt("idCliente", json.getJSONObject("cliente").optInt("idPessoa", 0)));
            os.setCliente(c);
        }
        
        if (json.has("atendente")) {
            Atendente a = new Atendente();
            a.setIdPessoa(json.getJSONObject("atendente").optInt("idAtendente", json.getJSONObject("atendente").optInt("idPessoa", 0)));
            os.setAtendente(a);
        }
        
        if (json.has("listaItens")) {
            JSONArray arr = json.getJSONArray("listaItens");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jItem = arr.getJSONObject(i);
                ItemServico item = new ItemServico();
                item.setValorServico(jItem.optDouble("valorServico"));
                
                if (jItem.has("tipoServico")) {
                    TipoServico t = new TipoServico();
                    t.setIdTipoServico(jItem.getJSONObject("tipoServico").optInt("idTipoServico"));
                    t.setNomeTipoServico(jItem.getJSONObject("tipoServico").optString("nomeTipoServico"));
                    item.setTipoServico(t);
                }
                os.adicionarItem(item);
            }
        }
        
        return os;
    }
}