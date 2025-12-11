package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class Teste01_IntegracaoExterna {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    @Order(1)
    @DisplayName("Deve buscar dados de Foz do Iguaçu corretamente via ViaCEP")
    public void validarFozDoIguacu() {
        System.out.println(">>> [Teste 01.1] Validando Foz do Iguaçu (" + ContextoTestes.CEP_FOZ + ")...");
        try {
            // Correção: Retorna Endereco direto
            Endereco end = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);

            assertNotNull(end, "O endereço retornado não deveria ser nulo");
            assertNotNull(end.getCidade(), "A cidade não deveria ser nula");
            assertNotNull(end.getBairro(), "O bairro não deveria ser nulo");
            assertNotNull(end.getLogradouro(), "O logradouro não deveria ser nulo");

            // Validações de conteúdo esperado
            assertEquals(ContextoTestes.EXPECTED_CIDADE_FOZ, end.getCidade().getNomeCidade());
            assertEquals(ContextoTestes.EXPECTED_UF_FOZ, end.getCidade().getUnidadeFederativa().getSiglaUF());
            assertEquals(ContextoTestes.EXPECTED_BAIRRO_FOZ, end.getBairro().getNomeBairro());

            // Validações de Logradouro e Tipo (tratamento de string)
            assertEquals(ContextoTestes.EXPECTED_TIPO_FOZ, end.getLogradouro().getTipoLogradouro().getNomeTipoLogradouro());
            assertEquals(ContextoTestes.EXPECTED_LOG_FOZ, end.getLogradouro().getNomeLogradouro());

        } catch (Exception e) {
            fail("Foz do Iguaçu falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar dados de São Paulo corretamente")
    public void validarSaoPaulo() {
        System.out.println(">>> [Teste 01.2] Validando São Paulo (" + ContextoTestes.CEP_SAO_PAULO + ")...");
        try {
            Endereco end = servicos.obterEnderecoExterno(ContextoTestes.CEP_SAO_PAULO);

            assertEquals(ContextoTestes.EXPECTED_CIDADE_SP, end.getCidade().getNomeCidade());
            assertEquals(ContextoTestes.EXPECTED_UF_SP, end.getCidade().getUnidadeFederativa().getSiglaUF());
            assertEquals(ContextoTestes.EXPECTED_BAIRRO_SP, end.getBairro().getNomeBairro());

        } catch (Exception e) {
            fail("São Paulo falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Deve aceitar CEP com formatação (hífen)")
    public void validarCepComFormatacao() {
        System.out.println(">>> [Teste 01.3] Validando CEP com máscara (85867-900)...");
        try {
            // Testa se o regex \\D funciona
            Endereco end = servicos.obterEnderecoExterno("85867-900");
            assertNotNull(end);
            assertEquals(ContextoTestes.EXPECTED_CIDADE_FOZ, end.getCidade().getNomeCidade());
        } catch (Exception e) {
            fail("Falha ao processar CEP com máscara: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Deve rejeitar CEPs inválidos ou inexistentes")
    public void validarTratamentoDeErros() {
        System.out.println(">>> [Teste 01.4] Validando Erros de CEP...");

        // 1. CEP Inexistente (Formato válido, mas não existe no correio)
        Exception exInexistente = assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("99999999");
        });
        assertTrue(exInexistente.getMessage().contains("não encontrado"), "Deveria acusar que não foi encontrado");

        // 2. CEP Formato Inválido (Letras)
        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("ABCDEFGH");
        });

        // 3. CEP Tamanho Inválido (Números a mais ou a menos)
        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("123");
        });

        // 4. CEP Nulo
        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno(null);
        });

        // 5. CEP Vazio
        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("");
        });

        // 6. SQL Injection / Malicioso (Embora ViaCEP seja HTTP, valida se a limpeza funciona)
        // O método deve limpar caracteres não numéricos e validar tamanho 8.
        // Se sobrar caracteres suficientes para formar 8 digitos, ele tenta buscar, se não, erro.
        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("85867-900' OR 1=1"); // Vai limpar e ficar > 8 ou inválido
        });
    }
}