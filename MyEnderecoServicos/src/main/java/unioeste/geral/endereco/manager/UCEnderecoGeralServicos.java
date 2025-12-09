package unioeste.geral.endereco.manager;

import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.col.*;
import unioeste.geral.endereco.exception.EnderecoException;
import java.util.List;

public class UCEnderecoGeralServicos {

    private final EnderecoEspecificoCol enderecoEspecificoCol;
    private final CidadeCol cidadeCol;
    private final UfCol ufCol;
    private final BairroCol bairroCol;
    private final TipoLogradouroCol tipoLogradouroCol;
    private final LogradouroCol logradouroCol; // Adicionado

    public UCEnderecoGeralServicos() {
        this.enderecoEspecificoCol = new EnderecoEspecificoCol();
        this.cidadeCol = new CidadeCol();
        this.ufCol = new UfCol();
        this.bairroCol = new BairroCol();
        this.tipoLogradouroCol = new TipoLogradouroCol();
        this.logradouroCol = new LogradouroCol();
    }

    // --- Especificados ---

    public EnderecoEspecifico cadastrarEndereco(EnderecoEspecifico endereco) throws EnderecoException, Exception {
        enderecoEspecificoCol.cadastrarEnderecoEspecifico(endereco);
        return endereco;
    }

    public List<EnderecoEspecifico> obterEnderecoPorCEP(String cep) throws Exception {
        return enderecoEspecificoCol.buscarDoBancoPorCep(cep);
    }

    public EnderecoEspecifico obterEnderecoPorID(EnderecoEspecifico endereco) throws Exception {
        return enderecoEspecificoCol.buscarPorId(endereco);
    }

    public EnderecoEspecifico obterEnderecoExterno(String siteAPesquisar) throws Exception {
        return enderecoEspecificoCol.consultarCepExterno(siteAPesquisar);
    }

    public Cidade obterCidade(Cidade cidade) throws Exception {
        return cidadeCol.buscarPorId(cidade);
    }

    // --- Auxiliares ---

    public List<UnidadeFederativa> consultarUFs() throws Exception {
        return ufCol.buscarTodas();
    }

    public List<Cidade> consultarCidades() throws Exception {
        return cidadeCol.buscarTodas();
    }

    public List<Bairro> consultarBairros() throws Exception {
        return bairroCol.buscarTodos();
    }

    public List<TipoLogradouro> consultarTiposLogradouro() throws Exception {
        return tipoLogradouroCol.buscarTodos();
    }

    public List<Logradouro> consultarLogradouros() throws Exception {
        return logradouroCol.buscarTodos();
    }
}