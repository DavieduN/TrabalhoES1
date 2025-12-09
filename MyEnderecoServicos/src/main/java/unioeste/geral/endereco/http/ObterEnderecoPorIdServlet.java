package unioeste.geral.endereco.http;

import org.json.JSONObject;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/enderecos/buscar-id")
public class ObterEnderecoPorIdServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);

        try {
            JSONObject json = new JSONObject(sb.toString());
            EnderecoEspecifico filtro = new EnderecoEspecifico();
            if (json.has("idEnderecoEspecifico")) {
                filtro.setIdEnderecoEspecifico(json.getInt("idEnderecoEspecifico"));
            }
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            EnderecoEspecifico resultado = servicos.obterEnderecoPorID(filtro);

            out.print(new JSONObject(resultado));

        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}