package unioeste.caso2.servico.manager;

import unioeste.caso2.servico.bo.Atendente;
import unioeste.caso2.servico.dao.AtendenteDAO;
import unioeste.caso2.servico.dao.EmailAtendenteDAO;
import unioeste.caso2.servico.dao.TelefoneAtendenteDAO;
import unioeste.geral.pessoa.manager.UCPessoaFisicaServicos;

import java.sql.Connection;

public class UCAtendenteServicos {

    private final UCPessoaFisicaServicos<Atendente> pessoaService;

    public UCAtendenteServicos() {
        this.pessoaService = new UCPessoaFisicaServicos<>(
            new AtendenteDAO(),
            new TelefoneAtendenteDAO(),
            new EmailAtendenteDAO()
        );
    }

    public Atendente cadastrarAtendente(Atendente atendente) throws Exception {
        return pessoaService.cadastrarPessoa(atendente);
    }

    public Atendente buscarAtendentePorId(int id) throws Exception {
        return pessoaService.buscarPorId(id);
    }
    
    public Atendente buscarAtendentePorId(Connection con, int id) throws Exception {
        return pessoaService.buscarPorId(con, id);
    }

    public Atendente buscarAtendentePorCpf(String cpf) throws Exception {
        return pessoaService.buscarPorCpf(cpf);
    }
}