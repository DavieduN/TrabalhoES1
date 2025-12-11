package unioeste.geral.pessoa.col;

import unioeste.geral.pessoa.bo.Telefone;
import unioeste.geral.pessoa.dao.TelefoneDAO;

import java.sql.Connection;
import java.util.List;

public class TelefoneCol {

    private final TelefoneDAO dao;

    public TelefoneCol() {
        this.dao = new TelefoneDAO();
    }

    public void salvarTelefones(Connection con, List<Telefone> telefones, int idPessoa) throws Exception {
        if (telefones == null || telefones.isEmpty()) {
            return;
        }

        if (idPessoa <= 0) {
            throw new Exception("ID de pessoa inválido para vincular telefone.");
        }

        for (Telefone t : telefones) {
            validarTelefone(t);
            dao.inserir(con, t, idPessoa);
        }
    }

    public List<Telefone> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        if (idPessoa <= 0) {
            throw new Exception("ID de pessoa inválido para busca.");
        }
        return dao.buscarPorPessoa(con, idPessoa);
    }

    private void validarTelefone(Telefone t) throws Exception {
        if (t == null) {
            throw new Exception("Objeto telefone não pode ser nulo.");
        }

        if (t.getNumero() == null || t.getNumero().trim().isEmpty()) {
            throw new Exception("Número do telefone é obrigatório.");
        }

        String numLimpo = t.getNumero().replaceAll("\\D", "");
        if (numLimpo.length() < 8 || numLimpo.length() > 9) {
            throw new Exception("Número de telefone com tamanho inválido (deve ter 8 ou 9 dígitos): " + t.getNumero());
        }
        t.setNumero(numLimpo);

        if (t.getDdi() == null || t.getDdi().getIdDdi() <= 0) {
            throw new Exception("Erro de orquestração: O telefone deve possuir um DDI validado (ID > 0) antes de salvar.");
        }

        if (t.getDdd() == null || t.getDdd().getIdDdd() <= 0) {
            throw new Exception("Erro de orquestração: O telefone deve possuir um DDD validado (ID > 0) antes de salvar.");
        }
    }
}