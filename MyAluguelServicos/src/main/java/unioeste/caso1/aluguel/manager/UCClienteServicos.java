package unioeste.caso1.aluguel.manager;

import unioeste.caso1.aluguel.bo.Cliente;
import unioeste.caso1.aluguel.dao.ClienteDAO;
import unioeste.caso1.aluguel.dao.EmailClienteDAO;
import unioeste.caso1.aluguel.dao.TelefoneClienteDAO;
import unioeste.geral.pessoa.manager.UCPessoaFisicaServicos;

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

    public Cliente buscarClientePorCpf(String cpf) throws Exception {
        return pessoaService.buscarPorCpf(cpf);
    }
}