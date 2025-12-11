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
            Endereco end = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);

            assertNotNull(end, "O endereço retornado não deveria ser nulo");
            assertNotNull(end.getCidade(), "A cidade não deveria ser nula");
            assertNotNull(end.getBairro(), "O bairro não deveria ser nulo");
            assertNotNull(end.getLogradouro(), "O logradouro não deveria ser nulo");

            assertEquals(ContextoTestes.EXPECTED_CIDADE_FOZ, end.getCidade().getNomeCidade());
            assertEquals(ContextoTestes.EXPECTED_UF_FOZ, end.getCidade().getUnidadeFederativa().getSiglaUF());
            assertEquals(ContextoTestes.EXPECTED_BAIRRO_FOZ, end.getBairro().getNomeBairro());

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

        Exception exInexistente = assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("99999999");
        });
        assertTrue(exInexistente.getMessage().contains("não encontrado"), "Deveria acusar que não foi encontrado");

        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("ABCDEFGH");
        });

        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("123");
        });

        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno(null);
        });

        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("");
        });

        assertThrows(EnderecoException.class, () -> {
            servicos.obterEnderecoExterno("85867-900' OR 1=1");
        });
    }
}