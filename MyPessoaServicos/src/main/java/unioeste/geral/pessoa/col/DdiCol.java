package unioeste.geral.pessoa.col;

import unioeste.geral.pessoa.bo.DDI;
import unioeste.geral.pessoa.dao.DdiDAO;
import java.sql.Connection;

public class DdiCol {
    private final DdiDAO dao;

    public DdiCol() { this.dao = new DdiDAO(); }

    public DDI validarExistencia(Connection con, DDI ddi) throws Exception {
        if (ddi == null) throw new Exception("DDI é obrigatório.");

        if (ddi.getIdDdi() > 0 && ddi.getDdi() > 0) {
            DDI existente = dao.buscarPorId(con, ddi.getIdDdi());

            if (existente == null) {
                throw new Exception("DDI não encontrado com o ID informado: " + ddi.getIdDdi());
            }

            if (existente.getDdi() != ddi.getDdi()) {
                throw new Exception("Inconsistência: O ID informado (" + ddi.getIdDdi() +
                                    ") refere-se ao DDI " + existente.getDdi() +
                                    ", mas foi informado o número " + ddi.getDdi());
            }
            return existente;
        }

        if (ddi.getDdi() > 0) {
            DDI existente = dao.buscarPorNumero(con, ddi.getDdi());
            if (existente == null) {
                throw new Exception("DDI não cadastrado no sistema: " + ddi.getDdi());
            }
            return existente;
        }

        if (ddi.getIdDdi() > 0) {
            DDI existente = dao.buscarPorId(con, ddi.getIdDdi());
            if (existente == null) {
                throw new Exception("DDI inválido/inexistente para o ID: " + ddi.getIdDdi());
            }
            return existente;
        }

        throw new Exception("Informe o ID ou o Número do DDI.");
    }
}