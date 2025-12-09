package unioeste.geral.endereco.http;

import org.json.JSONObject;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/enderecos/externo/*")
public class ObterEnderecoExternoServlet extends HttpServlet {
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
            EnderecoEspecifico end = servicos.obterEnderecoExterno(cep);

            out.print(new JSONObject(end));

        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}