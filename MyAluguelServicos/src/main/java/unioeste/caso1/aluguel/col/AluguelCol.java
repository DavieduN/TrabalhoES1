package unioeste.caso1.aluguel.col;

import unioeste.caso1.aluguel.bo.Aluguel;
import unioeste.caso1.aluguel.dao.AluguelDAO;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AluguelCol {

    private final AluguelDAO dao;

    public AluguelCol() {
        this.dao = new AluguelDAO();
    }

    public void registrarAluguel(Connection con, Aluguel aluguel) throws Exception {
        validarAluguel(aluguel);
        calcularValorTotal(aluguel);

        if (aluguel.getDataPedido() == null) {
            aluguel.setDataPedido(LocalDate.now());
        }

        dao.registrar(con, aluguel);
    }

    public Aluguel buscarPorNumero(Connection con, int nroAluguel) throws Exception {
        if (nroAluguel <= 0) {
            throw new Exception("Número do aluguel inválido para busca.");
        }

        Aluguel encontrado = dao.buscarPorNumero(con, nroAluguel);
        return encontrado;
    }

    public List<Aluguel> consultarAlugueis(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }

    private void validarAluguel(Aluguel a) throws Exception {
        if (a == null) throw new Exception("Dados do aluguel inválidos.");

        if (a.getCliente() == null || a.getCliente().getIdPessoa() <= 0) {
            throw new Exception("Cliente é obrigatório para registrar o aluguel.");
        }
        if (a.getEquipamento() == null || a.getEquipamento().getIdEquipamento() <= 0) {
            throw new Exception("Equipamento é obrigatório.");
        }

        if (a.getDataLocacao() == null || a.getDataDevolucao() == null) {
            throw new Exception("Datas de retirada e devolução são obrigatórias.");
        }

        if (a.getDataDevolucao().isBefore(a.getDataLocacao())) {
            throw new Exception("A data de devolução não pode ser anterior à data de retirada.");
        }
    }

    private void calcularValorTotal(Aluguel a) {
        long dias = ChronoUnit.DAYS.between(a.getDataLocacao(), a.getDataDevolucao());

        if (dias < 1) dias = 1;

        double total = dias * a.getEquipamento().getValorDiaria();
        a.setValorTotalLocacao(total);
    }
}