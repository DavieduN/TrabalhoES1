package unioeste.caso2.servico.manager;

import unioeste.apoio.bd.ConexaoBD;
import unioeste.caso2.servico.bo.Atendente;
import unioeste.caso2.servico.bo.Cliente;
import unioeste.caso2.servico.bo.ItemServico;
import unioeste.caso2.servico.bo.OrdemServico;
import unioeste.caso2.servico.col.ItemServicoCol;
import unioeste.caso2.servico.col.OrdemServicoCol;

import unioeste.caso2.servico.manager.UCClienteServicos;
import unioeste.caso2.servico.manager.UCAtendenteServicos;

import java.sql.Connection;
import java.util.List;

public class UCOrdemServicoServicos {

    private final OrdemServicoCol osCol;
    private final ItemServicoCol itemCol;
    
    private final UCClienteServicos clienteService;
    private final UCAtendenteServicos atendenteService;
    
    private final ConexaoBD conexaoBD;

    public UCOrdemServicoServicos() {
        this.osCol = new OrdemServicoCol();
        this.itemCol = new ItemServicoCol();
        this.clienteService = new UCClienteServicos();
        this.atendenteService = new UCAtendenteServicos();
        this.conexaoBD = new ConexaoBD();
    }

    public OrdemServico registrarOS(OrdemServico os) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            con.setAutoCommit(false); 

            Cliente c = clienteService.buscarClientePorId(con, os.getCliente().getIdPessoa()); 
            if (c == null) throw new Exception("Cliente não encontrado.");
            os.setCliente(c);

            Atendente a = atendenteService.buscarAtendentePorId(con, os.getAtendente().getIdPessoa());
            if (a == null) throw new Exception("Atendente não encontrado.");
            os.setAtendente(a);

            osCol.registrarOS(con, os);

            if (os.getListaItens() != null) {
                for (ItemServico item : os.getListaItens()) {
                    itemCol.cadastrarItem(con, item, os.getNroOrdemServico());
                }
            }

            con.commit();
            return os;

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (Exception ex) {}
            throw e;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
    
    public List<OrdemServico> consultarTodas() throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            List<OrdemServico> lista = osCol.consultarTodas(con);
            for (OrdemServico os : lista) {
                carregarCliente(con, os);
                carregarItens(con, os);
            }
            return lista;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    public OrdemServico buscarPorNumero(int nro) throws Exception {
        Connection con = null;
        try {
            con = conexaoBD.getConexao();
            OrdemServico os = osCol.buscarPorNumero(con, nro);
            
            if (os != null) {
                os.setListaItens(itemCol.buscarPorOS(con, nro));
                
                Cliente c = clienteService.buscarClientePorId(con, os.getCliente().getIdPessoa());
                os.setCliente(c);
                
                Atendente a = atendenteService.buscarAtendentePorId(con, os.getAtendente().getIdPessoa());
                os.setAtendente(a);
            }
            return os;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }
}