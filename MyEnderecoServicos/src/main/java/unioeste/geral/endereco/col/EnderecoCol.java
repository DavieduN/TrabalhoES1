package unioeste.geral.endereco.col;

import org.json.JSONObject;
import java.sql.Connection;
import java.util.List;

import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.dao.*;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.apoio.viacep.ConexaoViaCEP;

public class EnderecoCol {

    final private EnderecoDAO dao;
    private final ConexaoViaCEP conexaoViaCEP;

    public EnderecoCol() {
        this.dao = new EnderecoDAO();
        this.conexaoViaCEP = new ConexaoViaCEP();
    }

    public Endereco buscarPorID(Connection con, Endereco endereco) throws Exception {
        if (endereco == null) {
            throw new EnderecoException("Endereço para buscar não foi informado.");
        }
        if (endereco.getIdEndereco() <= 0) {
            throw new EnderecoException("ID do endereço inválido para consulta.");
        }

        Endereco encontrado = dao.buscarPorId(con, endereco.getIdEndereco());

        if (encontrado == null) {
            throw new EnderecoException("Endereço não encontrado com o ID informado.");
        }

        return encontrado;
    }

    public List<Endereco> buscarPorCep(Connection con, String cep) throws Exception {
        if (cep == null || cep.trim().isEmpty()) {
            throw new EnderecoException("CEP obrigatório.");
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) throw new EnderecoException("CEP inválido na busca.");

        return dao.buscarPorCep(con, cepLimpo);
    }

    public Endereco consultarCepExterno(String cep) throws Exception {
        if (cep == null || cep.trim().isEmpty()) {
            throw new EnderecoException("CEP obrigatório.");
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) throw new EnderecoException("CEP inválido na busca.");

        String jsonBruto = conexaoViaCEP.obterConteudoBruto(cepLimpo);

        JSONObject json = new JSONObject(jsonBruto);
        if (json.has("erro"))
            throw new EnderecoException("CEP não encontrado na base externa.");

        Endereco endereco = new Endereco();
        endereco.setCep(json.getString("cep"));

        Cidade c = new Cidade();
        c.setNomeCidade(json.getString("localidade"));
        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSiglaUF(json.getString("uf"));
        uf.setNomeUF(json.optString("estado", json.getString("uf")));
        c.setUnidadeFederativa(uf);
        endereco.setCidade(c);

        Bairro b = new Bairro();
        b.setNomeBairro(json.optString("bairro", ""));
        endereco.setBairro(b);

        String logradouroFull = json.optString("logradouro", "").trim();

        Logradouro l = new Logradouro();
        TipoLogradouro t = new TipoLogradouro();

        if (logradouroFull.isEmpty()){
            t.setNomeTipoLogradouro("");
            l.setNomeLogradouro("");
        }
        else if (logradouroFull.contains(" ")){
            int espaco = logradouroFull.indexOf(" ");
            t.setNomeTipoLogradouro(logradouroFull.substring(0, espaco));
            l.setNomeLogradouro(logradouroFull.substring(espaco + 1));
        }
        else{
            t.setNomeTipoLogradouro("Rua");
            l.setNomeLogradouro(logradouroFull);
        }

        l.setTipoLogradouro(t);
        endereco.setLogradouro(l);

        return endereco;
    }

    public Endereco obterOuCadastrar(Connection con, Endereco endereco) throws Exception {
        if (endereco == null) {
            throw new EnderecoException("Endereço é obrigatório.");
        }
        if (endereco.getCep() != null) {
            String cepLimpo = endereco.getCep().replaceAll("\\D", "");
            endereco.setCep(cepLimpo);
        }
        if (endereco.getCep() == null || endereco.getCep().length() != 8) {
            throw new EnderecoException("CEP do endereço é inválido (deve conter 8 dígitos numéricos).");
        }
        if (endereco.getCidade() == null || endereco.getCidade().getIdCidade() <= 0) {
            throw new EnderecoException("Erro interno: Cidade sem ID ao cadastrar endereço.");
        }
        if (endereco.getBairro() == null || endereco.getBairro().getIdBairro() <= 0) {
            throw new EnderecoException("Erro interno: Bairro sem ID ao cadastrar endereço.");
        }
        if (endereco.getLogradouro() == null || endereco.getLogradouro().getIdLogradouro() <= 0) {
            throw new EnderecoException("Erro interno: Logradouro sem ID ao cadastrar endereço.");
        }

        Endereco existente = dao.buscarPorCombinacaoUnica(con, endereco);

        if (existente != null) {
            return existente;
        }

        dao.inserir(con, endereco);
        return endereco;
    }
}