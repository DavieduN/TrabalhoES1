package unioeste.geral.endereco.col;

import org.json.JSONObject;
import unioeste.apoio.bd.ConexaoBD;
import unioeste.apoio.viacep.ConexaoViaCEP;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.dao.EnderecoEspecificoDAO;
import unioeste.geral.endereco.exception.EnderecoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EnderecoEspecificoCol {

    private final EnderecoEspecificoDAO dao;
    private final EnderecoCol enderecoCol;
    private final ConexaoViaCEP conexaoViaCEP;

    public EnderecoEspecificoCol() {
        this.dao = new EnderecoEspecificoDAO();
        this.enderecoCol = new EnderecoCol();
        this.conexaoViaCEP = new ConexaoViaCEP();
    }

    public void cadastrarEnderecoEspecifico(EnderecoEspecifico endEsp) throws Exception {
        if (endEsp == null)
            throw new EnderecoException("Endereço inválido.");
        if (endEsp.getNumero() == null || endEsp.getNumero().trim().isEmpty())
            throw new EnderecoException("O número do endereço é obrigatório.");

        Connection con = null;
        try {
            con = ConexaoBD.getConexao();
            con.setAutoCommit(false);
            Endereco endereco = endEsp.getEndereco();
            enderecoCol.cadastrarEndereco(con, endereco);
            dao.inserir(con, endEsp);
            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<EnderecoEspecifico> buscarDoBancoPorCep(String cep) throws Exception {
        if (cep == null) throw new EnderecoException("CEP vazio na busca.");
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) throw new EnderecoException("CEP inválido na busca.");
        return dao.buscarPorCep(cepLimpo);
    }

    public EnderecoEspecifico buscarPorId(EnderecoEspecifico endEsp) throws Exception {
        if (endEsp == null) {
            throw new EnderecoException("Endereço para buscar não foi informado.");
        }
        if (endEsp.getIdEnderecoEspecifico() <= 0) {
            throw new EnderecoException("ID do endereço é inválido para consulta.");
        }

        EnderecoEspecifico encontrado = dao.buscarPorId(endEsp.getIdEnderecoEspecifico());

        if (encontrado == null) {
            throw new EnderecoException("Endereço com o ID informado não foi encontrado.");
        }

        return encontrado;
    }

    public EnderecoEspecifico consultarCepExterno(String cep) throws Exception {
        if (cep == null || !cep.matches("\\d{5}-?\\d{3}"))
            throw new EnderecoException("Formato de CEP inválido.");

        String jsonBruto = conexaoViaCEP.obterConteudoBruto(cep);

        JSONObject json = new JSONObject(jsonBruto);
        if (json.has("erro"))
            throw new EnderecoException("CEP não encontrado na base externa.");

        Endereco endereco = new Endereco();
        endereco.setCep(json.getString("cep"));

        Cidade c = new Cidade();
        c.setNomeCidade(json.getString("localidade"));
        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSiglaUF(json.getString("uf"));
        uf.setNomeUF(json.getString("uf"));
        c.setUnidadeFederativa(uf);
        endereco.setCidade(c);

        Bairro b = new Bairro();
        b.setNomeBairro(json.optString("bairro", "Bairro não informado"));
        endereco.setBairro(b);

        String logradouroFull = json.getString("logradouro");
        Logradouro l = new Logradouro();
        TipoLogradouro t = new TipoLogradouro();

        if (logradouroFull.contains(" ")) {
            int espaco = logradouroFull.indexOf(" ");
            t.setNomeTipoLogradouro(logradouroFull.substring(0, espaco));
            l.setNomeLogradouro(logradouroFull.substring(espaco + 1));
        } else {
            t.setNomeTipoLogradouro("Rua");
            l.setNomeLogradouro(logradouroFull);
        }
        l.setTipoLogradouro(t);
        endereco.setLogradouro(l);

        EnderecoEspecifico endEsp = new EnderecoEspecifico();
        endEsp.setEndereco(endereco);

        return endEsp;
    }
}