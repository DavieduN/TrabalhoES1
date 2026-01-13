package unioeste.caso1.aluguel.http;

import org.json.JSONObject;
import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.manager.UCAluguelServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/aluguel/buscar-numero")
public class BuscarAluguelPorNumeroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        PrintWriter out = resp.getWriter();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);

        try {
            JSONObject json = new JSONObject(sb.toString());
            int nro = json.optInt("nroAluguel", 0);

            UCAluguelServicos servicos = new UCAluguelServicos();
            Aluguel encontrado = servicos.buscarAluguelPorNumero(nro);

            if (encontrado != null) {
                out.print(new JSONObject(encontrado));
            } else {
                resp.setStatus(404);
                out.print("{\"mensagem\": \"Aluguel não encontrado.\"}");
            }

        } catch (Exception e) {
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }
}