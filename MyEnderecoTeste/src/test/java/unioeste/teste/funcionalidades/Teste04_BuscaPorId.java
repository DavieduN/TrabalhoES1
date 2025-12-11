package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.Cidade;
import unioeste.geral.endereco.bo.Endereco; // Ajustado de EnderecoEspecifico para Endereco
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
    @DisplayName("Deve recuperar todos os endereços gerados no Teste 02")
    public void buscarTodosEnderecosGerados() {
        System.out.println(">>> [Teste 04.1] Validando busca de " + ContextoTestes.idsEnderecosGerados.size() + " endereços...");

        if (ContextoTestes.idsEnderecosGerados.isEmpty()) {
            fail("Banco vazio. Execute o Teste 02 antes deste.");
        }

        for (Integer id : ContextoTestes.idsEnderecosGerados) {
            try {
                Endereco filtro = criarFiltroEndereco(id);
                Endereco res = servicos.obterEnderecoPorID(filtro);

                assertNotNull(res, "Endereço ID " + id + " não encontrado");
                // Ajustado para Long/Int conforme sua implementação (assumindo cast seguro aqui)
                assertEquals(id.longValue(), res.getIdEndereco());

                // Valida se os JOINs funcionaram (objetos não nulos)
                assertNotNull(res.getCidade(), "Cidade não preenchida");
                assertNotNull(res.getCidade().getNomeCidade(), "Nome da Cidade vazio");
                assertNotNull(res.getCidade().getUnidadeFederativa().getSiglaUF(), "UF vazia");
                assertNotNull(res.getBairro().getNomeBairro(), "Bairro vazio");
                assertNotNull(res.getLogradouro().getNomeLogradouro(), "Logradouro vazio");

            } catch (Exception e) {
                fail("Falha ao recuperar ID " + id + ": " + e.getMessage());
            }
        }
        System.out.println("   + Sucesso: Todos os endereços recuperados com integridade.");
    }

    @Test
    @Order(2)
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    public void buscarEnderecoIdInvalido() {
        // O Col lança EnderecoException quando não encontra, não retorna null
        assertThrows(EnderecoException.class, () -> {
            Endereco filtro = criarFiltroEndereco(999999);
            servicos.obterEnderecoPorID(filtro);
        }, "Deveria lançar exceção para ID inexistente");
    }

    @Test
    @Order(3)
    @DisplayName("Deve lançar exceção ao buscar ID negativo")
    public void buscarEnderecoIdNegativo() {
        // O Col valida ID <= 0 antes de chamar o DAO
        assertThrows(EnderecoException.class, () -> {
            Endereco filtro = criarFiltroEndereco(-1);
            servicos.obterEnderecoPorID(filtro);
        }, "Deveria lançar exceção para ID negativo");
    }

    @Test
    @Order(4)
    @DisplayName("Deve recuperar as cidades geradas por ID")
    public void buscarCidadesGeradas() {
        System.out.println(">>> [Teste 04.4] Validando busca de Cidades...");

        for (Integer id : ContextoTestes.idsCidadesGeradas) {
            try {
                Cidade filtro = new Cidade();
                filtro.setIdCidade(id);

                Cidade c = servicos.obterCidadePorID(filtro);
                assertNotNull(c);
                assertEquals(id, c.getIdCidade());
                assertNotNull(c.getNomeCidade());
                assertNotNull(c.getUnidadeFederativa().getSiglaUF(), "A cidade deve trazer a UF preenchida");

            } catch (Exception e) {
                fail("Erro ao buscar cidade ID " + id + ": " + e.getMessage());
            }
        }
    }

    // Método auxiliar ajustado para retornar Endereco
    private Endereco criarFiltroEndereco(int id) {
        Endereco filtro = new Endereco();
        filtro.setIdEndereco(id); // Setando como long/int
        return filtro;
    }
}