package unioeste.caso2.servico.col;

import unioeste.caso2.servico.bo.ItemServico;
import unioeste.caso2.servico.bo.TipoServico;
import unioeste.caso2.servico.dao.ItemServicoDAO;
import java.sql.Connection;
import java.util.List;

public class ItemServicoCol {

    private final ItemServicoDAO dao;
    private final TipoServicoCol tipoCol;

    public ItemServicoCol() {
        this.dao = new ItemServicoDAO();
        this.tipoCol = new TipoServicoCol();
    }

    public void cadastrarItem(Connection con, ItemServico item, int nroOrdemServico) throws Exception {
        if (item == null) throw new Exception("Item de serviço inválido.");
        
        if (item.getValorServico() <= 0) {
            throw new Exception("O valor do serviço deve ser positivo.");
        }

        TipoServico tipoValidado = tipoCol.validarExistencia(con, item.getTipoServico());
        item.setTipoServico(tipoValidado);

        if (nroOrdemServico <= 0) {
            throw new Exception("Erro de integridade: Item sem vínculo com OS.");
        }

        dao.inserir(con, item, nroOrdemServico);
    }

    public List<ItemServico> buscarPorOS(Connection con, int nroOrdemServico) throws Exception {
        return dao.buscarPorOrdemServico(con, nroOrdemServico);
    }
}