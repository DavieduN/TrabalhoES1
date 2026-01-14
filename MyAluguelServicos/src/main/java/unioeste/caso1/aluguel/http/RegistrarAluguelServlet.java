package unioeste.caso1.aluguel.http;

import org.json.JSONObject;
import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.bo.Cliente;
import unioeste.caso1.aluguel.bo.Equipamento;
import unioeste.caso1.aluguel.manager.UCAluguelServicos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/aluguel/registrar")
public class RegistrarAluguelServlet extends HttpServlet {

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
            Aluguel aluguel = converterJsonParaAluguel(json);

            UCAluguelServicos servicos = new UCAluguelServicos();
            Aluguel salvo = servicos.registrarAluguel(aluguel);

            out.print(new JSONObject(salvo));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private Aluguel converterJsonParaAluguel(JSONObject json) {
        Aluguel a = new Aluguel();
        a.setNroAluguel(json.optInt("nroAluguel", 0));

        if (json.has("dataLocacao")) a.setDataLocacao(LocalDate.parse(json.getString("dataLocacao")));
        if (json.has("dataDevolucao")) a.setDataDevolucao(LocalDate.parse(json.getString("dataDevolucao")));

        if (json.has("cliente")) {
            Cliente c = new Cliente();
            c.setIdPessoa(json.getJSONObject("cliente").optInt("idCliente", json.getJSONObject("cliente").optInt("idPessoa", 0)));
            a.setCliente(c);
        }

        if (json.has("equipamento")) {
            Equipamento e = new Equipamento();
            e.setIdEquipamento(json.getJSONObject("equipamento").optInt("idEquipamento", 0));
            a.setEquipamento(e);
        }

        return a;
    }
}