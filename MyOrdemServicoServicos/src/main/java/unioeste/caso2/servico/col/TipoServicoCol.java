package unioeste.caso2.servico.col;

import unioeste.caso2.servico.bo.TipoServico;
import unioeste.caso2.servico.dao.TipoServicoDAO;
import java.sql.Connection;
import java.util.List;

public class TipoServicoCol {

    private final TipoServicoDAO dao;

    public TipoServicoCol() {
        this.dao = new TipoServicoDAO();
    }

    public TipoServico obterOuCadastrar(Connection con, TipoServico tipo) throws Exception {
        if (tipo == null) throw new Exception("Tipo de serviço não pode ser nulo.");
        
        if (tipo.getIdTipoServico() > 0) {
            return validarExistencia(con, tipo);
        }

        if (tipo.getNomeTipoServico() == null || tipo.getNomeTipoServico().trim().isEmpty()) {
            throw new Exception("Nome do tipo de serviço é obrigatório.");
        }

        TipoServico existente = dao.buscarPorNome(con, tipo.getNomeTipoServico().trim());
        if (existente != null) {
            return existente;
        }

        dao.inserir(con, tipo);
        return tipo;
    }

    public List<TipoServico> listarTodos(Connection con) throws Exception {
        return dao.buscarTodos(con);
    }
    
    public TipoServico validarExistencia(Connection con, TipoServico tipo) throws Exception {
        if (tipo == null) throw new Exception("Tipo de serviço obrigatório.");
        
        if (tipo.getIdTipoServico() > 0) {
            TipoServico existente = dao.buscarPorId(con, tipo.getIdTipoServico());
            if (existente == null) {
                throw new Exception("Tipo de serviço não encontrado para o ID: " + tipo.getIdTipoServico());
            }
            return existente;
        }
        throw new Exception("Tipo de serviço inválido.");
    }
}