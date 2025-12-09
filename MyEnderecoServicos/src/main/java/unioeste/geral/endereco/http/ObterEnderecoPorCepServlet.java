package unioeste.geral.endereco.http;

import org.json.JSONArray;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/enderecos/cep/*")
public class ObterEnderecoPorCepServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 2) {
                resp.setStatus(400);
                return;
            }
            String cep = pathInfo.substring(1);
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            out.print(new JSONArray(servicos.obterEnderecoPorCEP(cep)));
        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}