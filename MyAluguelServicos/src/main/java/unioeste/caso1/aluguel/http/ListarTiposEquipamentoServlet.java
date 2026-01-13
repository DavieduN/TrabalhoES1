package unioeste.caso1.aluguel.http;

import org.json.JSONArray;
import unioeste.caso1.aluguel.manager.UCEquipamentoServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/tipos-equipamento")
public class ListarTiposEquipamentoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter out = resp.getWriter()) {
            UCEquipamentoServicos servicos = new UCEquipamentoServicos();
            out.print(new JSONArray(servicos.listarTipos()));
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}