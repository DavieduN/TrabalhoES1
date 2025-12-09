package unioeste.geral.endereco.http;

import org.json.JSONArray;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ufs")
public class ObterUfsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            out.print(new JSONArray(servicos.consultarUFs()));
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }
}