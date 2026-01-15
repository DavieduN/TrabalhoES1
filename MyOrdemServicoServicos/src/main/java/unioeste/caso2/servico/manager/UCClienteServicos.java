package unioeste.caso2.servico.manager;

import unioeste.caso2.servico.bo.Cliente;
import unioeste.caso2.servico.dao.ClienteDAO;
import unioeste.caso2.servico.dao.EmailClienteDAO;
import unioeste.caso2.servico.dao.TelefoneClienteDAO;
import unioeste.geral.pessoa.manager.UCPessoaFisicaServicos;

import java.sql.Connection;
import java.util.List;

public class UCClienteServicos {

    private final UCPessoaFisicaServicos<Cliente> pessoaService;

    public UCClienteServicos() {
        this.pessoaService = new UCPessoaFisicaServicos<>(
            new ClienteDAO(),
            new TelefoneClienteDAO(),
            new EmailClienteDAO()
        );
    }

    public Cliente cadastrarCliente(Cliente cliente) throws Exception {
        return pessoaService.cadastrarPessoa(cliente);
    }

    public Cliente buscarClientePorId(int id) throws Exception {
        return pessoaService.buscarPorId(id);
    }
    
    public Cliente buscarClientePorId(Connection con, int id) throws Exception {
        return pessoaService.buscarPorId(con, id);
    }

    public Cliente buscarClientePorCpf(String cpf) throws Exception {
        return pessoaService.buscarPorCpf(cpf);
    }
}