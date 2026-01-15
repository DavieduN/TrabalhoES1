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
                carregarAtendente(con, os);
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
                carregarCliente(con, os);
                carregarAtendente(con, os);
                carregarItens(con, os);
            }
            return os;
        } finally {
            ConexaoBD.fecharConexao(con, null, null);
        }
    }

    private void carregarCliente(Connection con, OrdemServico os) {
        if (os.getCliente() != null && os.getCliente().getIdPessoa() > 0) {
            try {
                Cliente c = clienteService.buscarClientePorId(con, os.getCliente().getIdPessoa());
                if (c != null) {
                    os.setCliente(c);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar cliente na OS " + os.getNroOrdemServico() + ": " + e.getMessage());
            }
        }
    }

    private void carregarAtendente(Connection con, OrdemServico os) {
        if (os.getAtendente() != null && os.getAtendente().getIdPessoa() > 0) {
            try {
                Atendente a = atendenteService.buscarAtendentePorId(con, os.getAtendente().getIdPessoa());
                if (a != null) {
                    os.setAtendente(a);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar atendente na OS " + os.getNroOrdemServico() + ": " + e.getMessage());
            }
        }
    }

    private void carregarItens(Connection con, OrdemServico os) {
        try {
            os.setListaItens(itemCol.buscarPorOS(con, os.getNroOrdemServico()));
        } catch (Exception e) {
            System.err.println("Erro ao carregar itens na OS " + os.getNroOrdemServico() + ": " + e.getMessage());
        }
    }
}