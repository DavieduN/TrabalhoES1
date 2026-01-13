package unioeste.caso1.aluguel.http;

import org.json.JSONArray;
import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.manager.UCAluguelServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/aluguel/consultar")
public class ConsultarAluguelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try (PrintWriter out = resp.getWriter()) {
            UCAluguelServicos servicos = new UCAluguelServicos();
            List<Aluguel> lista = servicos.consultarAlugueis();
            out.print(new JSONArray(lista));
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }
}