package unioeste.caso2.servico.http;

import org.json.JSONArray;
import unioeste.caso2.servico.manager.UCTipoServicoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tipos-servico")
public class ListarTiposServicoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        try (PrintWriter out = resp.getWriter()) {
            UCTipoServicoServicos servicos = new UCTipoServicoServicos();
            out.print(new JSONArray(servicos.listarTipos()));
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}