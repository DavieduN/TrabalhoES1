package unioeste.geral.pessoa.col;

import unioeste.geral.pessoa.bo.DDD;
import unioeste.geral.pessoa.dao.DddDAO;
import java.sql.Connection;

public class DddCol {
    private final DddDAO dao;

    public DddCol() { this.dao = new DddDAO(); }

    public DDD validarExistencia(Connection con, DDD ddd) throws Exception {
        if (ddd == null) throw new Exception("DDD é obrigatório.");

        if (ddd.getIdDdd() > 0 && ddd.getDdd() > 0) {
            DDD existente = dao.buscarPorId(con, ddd.getIdDdd());

            if (existente == null) {
                throw new Exception("DDD não encontrado com o ID informado: " + ddd.getIdDdd());
            }
            if (existente.getDdd() != ddd.getDdd()) {
                throw new Exception("Inconsistência no DDD: ID e Número não correspondem.");
            }
            return existente;
        }

        if (ddd.getDdd() > 0) {
            DDD existente = dao.buscarPorNumero(con, ddd.getDdd());
            if (existente == null) {
                throw new Exception("DDD não cadastrado no sistema: " + ddd.getDdd());
            }
            return existente;
        }

        if (ddd.getIdDdd() > 0) {
            DDD existente = dao.buscarPorId(con, ddd.getIdDdd());
            if (existente == null) {
                throw new Exception("DDD inválido/inexistente para o ID: " + ddd.getIdDdd());
            }
            return existente;
        }

        throw new Exception("Informe o ID ou o Número do DDD.");
    }
}