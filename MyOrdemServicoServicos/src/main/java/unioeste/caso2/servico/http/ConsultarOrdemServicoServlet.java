package unioeste.caso2.servico.http;

import org.json.JSONArray;
import unioeste.caso2.servico.manager.UCOrdemServicoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ordem-servico/consultar")
public class ConsultarOrdemServicoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        try (PrintWriter out = resp.getWriter()) {
            UCOrdemServicoServicos servicos = new UCOrdemServicoServicos();
            out.print(new JSONArray(servicos.consultarTodas()));
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}