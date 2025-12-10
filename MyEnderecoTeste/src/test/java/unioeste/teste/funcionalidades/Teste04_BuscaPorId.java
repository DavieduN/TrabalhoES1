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

    // --- TESTE DE ENDEREÇOS ESPECÍFICOS ---

    @Test
    @Order(1)
    public void buscarTodosEnderecosGerados() {
        System.out.println(">>> [Teste 04] Validando busca de " + ContextoTestes.idsEnderecosGerados.size() + " endereços...");
        
        if (ContextoTestes.idsEnderecosGerados.isEmpty()) fail("Banco vazio. Teste 02 falhou.");

        for (Integer id : ContextoTestes.idsEnderecosGerados) {
            try {
                // Usa auxiliar para criar objeto filtro (regra do prof)
                EnderecoEspecifico filtro = criarFiltroEndereco(id);
                EnderecoEspecifico res = servicos.obterEnderecoPorID(filtro);

                assertNotNull(res, "Endereço ID " + id + " não encontrado");
                assertEquals(id, res.getIdEnderecoEspecifico());
                
                // Valida integridade do JOIN (Endereço -> Cidade -> UF)
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
        // Tenta buscar ID que não existe
        try {
            EnderecoEspecifico filtro = criarFiltroEndereco(999999);
            EnderecoEspecifico res = servicos.obterEnderecoPorID(filtro);
            // Dependendo da implementação do DAO, retorna null ou lança erro. Ambos aceitáveis aqui.
            assertNull(res, "Deveria retornar nulo para ID inexistente");
        } catch (Exception e) {
            // Se lançar exceção, ok também.
        }
    }

    @Test
    @Order(3)
    public void buscarEnderecoIdNegativo() {
        // Tenta buscar ID negativo (Input malicioso/erro de int)
        try {
            EnderecoEspecifico filtro = criarFiltroEndereco(-1);
            servicos.obterEnderecoPorID(filtro);
            // Se chegou aqui e retornou null, ok.
        } catch (Exception e) {
            // Se lançou erro, ok.
        }
    }

    // --- TESTE DE CIDADES ---

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
                // Valida o Lazy Load ou Eager Load da UF
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