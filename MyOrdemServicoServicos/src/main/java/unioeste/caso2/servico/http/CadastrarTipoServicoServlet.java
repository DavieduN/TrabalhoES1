package unioeste.caso2.servico.http;

import org.json.JSONObject;
import unioeste.caso2.servico.bo.TipoServico;
import unioeste.caso2.servico.manager.UCTipoServicoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tipos-servico/cadastrar")
public class CadastrarTipoServicoServlet extends HttpServlet {

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
            TipoServico tipo = new TipoServico();
            tipo.setNomeTipoServico(json.optString("nomeTipoServico"));

            UCTipoServicoServicos servicos = new UCTipoServicoServicos();
            servicos.cadastrarTipo(tipo);
            
            out.print(new JSONObject(tipo));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}