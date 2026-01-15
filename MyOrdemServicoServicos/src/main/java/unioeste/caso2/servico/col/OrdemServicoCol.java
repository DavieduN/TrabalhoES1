package unioeste.caso2.servico.col;

import unioeste.caso2.servico.bo.ItemServico;
import unioeste.caso2.servico.bo.OrdemServico;
import unioeste.caso2.servico.dao.OrdemServicoDAO;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class OrdemServicoCol {

    private final OrdemServicoDAO dao;

    public OrdemServicoCol() {
        this.dao = new OrdemServicoDAO();
    }

    public void registrarOS(Connection con, OrdemServico os) throws Exception {
        validarOS(os);

        if (os.getDataEmissao() == null) {
            os.setDataEmissao(LocalDate.now());
        }
        calcularValorTotal(os);

        dao.registrar(con, os);
    }

    private void calcularValorTotal(OrdemServico os) throws Exception {
        if (os.getListaItens() == null || os.getListaItens().isEmpty()) {
            throw new Exception("A OS deve conter pelo menos um serviço para ser registrada.");
        }

        double total = 0;
        for (ItemServico item : os.getListaItens()) {
            if (item.getValorServico() <= 0) {
                throw new Exception("Item de serviço com valor inválido (deve ser maior que zero).");
            }
            total += item.getValorServico();
        }
        
        os.setValorTotal(total);
    }

    public List<OrdemServico> consultarTodas(Connection con) throws Exception {
        return dao.buscarTodas(con);
    }

    public OrdemServico buscarPorNumero(Connection con, int nro) throws Exception {
        if (nro <= 0) throw new Exception("Número de OS inválido.");
        return dao.buscarPorNumero(con, nro);
    }

    private void validarOS(OrdemServico os) throws Exception {
        if (os == null) throw new Exception("OS nula.");
        
        if (os.getCliente() == null || os.getCliente().getIdPessoa() <= 0) {
            throw new Exception("Cliente obrigatório.");
        }
        if (os.getAtendente() == null || os.getAtendente().getIdPessoa() <= 0) {
            throw new Exception("Atendente obrigatório.");
        }
    }
}