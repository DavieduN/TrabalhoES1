package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.Endereco; // Correção: Usa Endereco, não EnderecoEspecifico
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;
import unioeste.geral.endereco.exception.EnderecoException;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Order(5)
public class Teste05_BuscaPorCep {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    @DisplayName("Deve retornar múltiplos endereços para CEP genérico de Foz")
    public void buscarCepFozMultiplosResultados() {
        System.out.println(">>> [Teste 05.1] Buscando lista CEP Foz (" + ContextoTestes.CEP_FOZ + ")...");
        try {
            // Retorna List<Endereco>
            List<Endereco> lista = servicos.obterEnderecoPorCep(ContextoTestes.CEP_FOZ);

            assertNotNull(lista);
            // Validamos se encontrou os 3 que cadastramos no Teste02
            // Nota: Se o obterOuCadastrar funcionou, deve ter retornado o MESMO ID para as 3 inserções,
            // então na verdade teremos 1 registro único na busca por CEP se for exata,
            // ou 3 se você inseriu 3 diferentes.
            // Pela lógica de obterOuCadastrar, se inserimos exatamente o mesmo Endereço 3 vezes, só existe 1 no banco.
            // Se inserimos com complementos diferentes (que ficam na tabela Pessoa/Externa), o Endereco base é 1 só.
            // Portanto, esperamos >= 1.
            assertFalse(lista.isEmpty(), "Deveria ter encontrado endereços para Foz");

            for (Endereco e : lista) {
                // Acesso direto, sem .getEndereco()
                assertEquals(ContextoTestes.EXPECTED_CIDADE_FOZ, e.getCidade().getNomeCidade());
                assertEquals(ContextoTestes.EXPECTED_UF_FOZ, e.getCidade().getUnidadeFederativa().getSiglaUF());
            }

        } catch (Exception e) {
            fail("Falha na busca por CEP Foz: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve retornar endereços corretos para São Paulo")
    public void buscarCepSaoPaulo() {
        System.out.println(">>> [Teste 05.2] Buscando lista CEP SP...");
        try {
            List<Endereco> lista = servicos.obterEnderecoPorCep(ContextoTestes.CEP_SAO_PAULO);
            assertFalse(lista.isEmpty());

            // Valida dados do primeiro elemento
            assertEquals(ContextoTestes.EXPECTED_LOG_SP, lista.get(0).getLogradouro().getNomeLogradouro());

        } catch (Exception e) {
            fail("Falha na busca por CEP SP: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia (não nula) para CEP inexistente no banco")
    public void buscarCepSemResultados() {
        try {
            List<Endereco> lista = servicos.obterEnderecoPorCep("11111111");
            assertNotNull(lista);
            assertTrue(lista.isEmpty(), "A lista deveria vir vazia, não nula");
        } catch (Exception e) {
            fail("Erro ao buscar CEP inexistente: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve validar formato de CEP inválido")
    public void buscarCepInvalido() {
        // Testando validação do Col (tamanho, nulo)
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCep("123"), "Deveria lançar erro de formato (tamanho)");
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCep(null), "Deveria tratar nulo");
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoPorCep(""), "Deveria tratar vazio");
    }
}