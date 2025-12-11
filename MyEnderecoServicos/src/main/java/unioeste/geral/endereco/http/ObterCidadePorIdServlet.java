package unioeste.geral.endereco.http;

import org.json.JSONObject;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cidades/buscar-id")
public class ObterCidadePorIdServlet extends HttpServlet {

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
            Cidade cidadeFiltro = new Cidade();
            if (json.has("idCidade")) {
                cidadeFiltro.setIdCidade(json.getInt("idCidade"));
            }
            UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();
            Cidade encontrada = servicos.obterCidadePorID(cidadeFiltro);
            out.print(new JSONObject(encontrada));

        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}