package unioeste.geral.endereco.http;

import org.json.JSONArray;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/cidades")
public class ObterCidadesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            out.print(new JSONArray(servicos.consultarCidades()));
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }
}