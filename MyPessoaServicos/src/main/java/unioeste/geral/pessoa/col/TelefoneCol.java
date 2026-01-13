package unioeste.geral.pessoa.col;

import unioeste.geral.pessoa.bo.Telefone;
import unioeste.geral.pessoa.dao.TelefoneDAO;
import java.sql.Connection;
import java.util.List;

public class TelefoneCol {

    private final TelefoneDAO dao;

    public TelefoneCol(TelefoneDAO dao) {
        this.dao = dao;
    }

    public void salvarTelefones(Connection con, List<Telefone> telefones, int idPessoa) throws Exception {
        if (telefones == null || telefones.isEmpty()) return;
        if (idPessoa <= 0) throw new Exception("ID de pessoa inválido para vincular telefone.");

        for (Telefone t : telefones) {
            validarTelefone(t);
            dao.inserir(con, t, idPessoa);
        }
    }

    public List<Telefone> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        if (idPessoa <= 0) throw new Exception("ID de pessoa inválido.");
        return dao.buscarPorPessoa(con, idPessoa);
    }

    private void validarTelefone(Telefone t) throws Exception {
        if (t == null) throw new Exception("Telefone nulo.");
        if (t.getNumero() == null || t.getNumero().trim().isEmpty()) throw new Exception("Número obrigatório.");

        String numLimpo = t.getNumero().replaceAll("\\D", "");
        if (numLimpo.length() < 8 || numLimpo.length() > 9) {
            throw new Exception("Tamanho do telefone inválido: " + t.getNumero());
        }
        t.setNumero(numLimpo);

        if (t.getDdi() == null || t.getDdi().getIdDdi() <= 0) throw new Exception("DDI inválido.");
        if (t.getDdd() == null || t.getDdd().getIdDdd() <= 0) throw new Exception("DDD inválido.");
    }
}