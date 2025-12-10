package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import static org.junit.jupiter.api.Assertions.*;

@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Teste04_BuscaPorId {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }


    @Test
    @Order(1)
    public void buscarTodosEnderecosGerados() {
        System.out.println(">>> [Teste 04] Validando busca de " + ContextoTestes.idsEnderecosGerados.size() + " endereços...");
        
        if (ContextoTestes.idsEnderecosGerados.isEmpty()) fail("Banco vazio. Teste 02 falhou.");

        for (Integer id : ContextoTestes.idsEnderecosGerados) {
            try {
                EnderecoEspecifico filtro = criarFiltroEndereco(id);
                EnderecoEspecifico res = servicos.obterEnderecoPorID(filtro);

                assertNotNull(res, "Endereço ID " + id + " não encontrado");
                assertEquals(id, res.getIdEnderecoEspecifico());
                
                assertNotNull(res.getEndereco().getCidade().getNomeCidade());
                assertNotNull(res.getEndereco().getCidade().getUnidadeFederativa().getSiglaUF());

            } catch (Exception e) {
                fail("Falha ao recuperar ID " + id + ": " + e.getMessage());
            }
        }
        System.out.println("   + Sucesso: Todos os endereços recuperados com integridade.");
    }

    @Test
    @Order(2)
    public void buscarEnderecoIdInvalido() {
        try {
            EnderecoEspecifico filtro = criarFiltroEndereco(999999);
            EnderecoEspecifico res = servicos.obterEnderecoPorID(filtro);
            assertNull(res, "Deveria retornar nulo para ID inexistente");
        } catch (Exception e) {
        }
    }

    @Test
    @Order(3)
    public void buscarEnderecoIdNegativo() {
        try {
            EnderecoEspecifico filtro = criarFiltroEndereco(-1);
            servicos.obterEnderecoPorID(filtro);
        } catch (Exception e) {
        }
    }


    @Test
    @Order(4)
    public void buscarCidadesGeradas() {
        System.out.println(">>> [Teste 04] Validando busca de Cidades...");
        
        for (Integer id : ContextoTestes.idsCidadesGeradas) {
            try {
                Cidade filtro = new Cidade();
                filtro.setIdCidade(id);
                
                Cidade c = servicos.obterCidade(filtro);
                assertNotNull(c);
                assertEquals(id, c.getIdCidade());
                assertNotNull(c.getNomeCidade());
                assertNotNull(c.getUnidadeFederativa().getSiglaUF(), "A cidade deve trazer a UF preenchida");
                
            } catch (Exception e) {
                fail("Erro ao buscar cidade: " + e.getMessage());
            }
        }
    }

    private EnderecoEspecifico criarFiltroEndereco(int id) {
        EnderecoEspecifico filtro = new EnderecoEspecifico();
        filtro.setIdEnderecoEspecifico(id);
        return filtro;
    }
}