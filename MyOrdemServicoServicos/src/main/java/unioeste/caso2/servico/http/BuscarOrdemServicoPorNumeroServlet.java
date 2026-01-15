package unioeste.caso2.servico.http;

import org.json.JSONObject;
import unioeste.caso2.servico.bo.OrdemServico;
import unioeste.caso2.servico.manager.UCOrdemServicoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ordem-servico/buscar-numero")
public class BuscarOrdemServicoPorNumeroServlet extends HttpServlet {

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
            int nro = json.optInt("nroOrdemServico", 0);
            
            UCOrdemServicoServicos servicos = new UCOrdemServicoServicos();
            OrdemServico os = servicos.buscarPorNumero(nro);

            if (os != null) {
                out.print(new JSONObject(os));
            } else {
                resp.setStatus(404);
                out.print("{\"mensagem\": \"Ordem de Serviço não encontrada.\"}");
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}