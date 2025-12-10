package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1) // Garante que este rode primeiro
public class Teste01_IntegracaoExterna {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    @Order(1)
    public void validarFozDoIguacu() {
        System.out.println(">>> [Teste 01] Validando Foz do Iguaçu (" + ContextoTestes.CEP_FOZ + ")...");
        try {
            EnderecoEspecifico res = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);
            Endereco end = res.getEndereco();

            assertNotNull(res);
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
    public void validarSaoPaulo() {
        System.out.println(">>> [Teste 01] Validando São Paulo (" + ContextoTestes.CEP_SAO_PAULO + ")...");
        try {
            EnderecoEspecifico res = servicos.obterEnderecoExterno(ContextoTestes.CEP_SAO_PAULO);
            Endereco end = res.getEndereco();

            assertEquals(ContextoTestes.EXPECTED_CIDADE_SP, end.getCidade().getNomeCidade());
            assertEquals(ContextoTestes.EXPECTED_UF_SP, end.getCidade().getUnidadeFederativa().getSiglaUF());
            
            // Valida apenas se não é nulo, pois ViaCEP pode oscilar o nome exato do bairro
            assertNotNull(end.getBairro().getNomeBairro()); 

        } catch (Exception e) {
            fail("São Paulo falhou: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void validarTratamentoDeErros() {
        System.out.println(">>> [Teste 01] Validando Erros de CEP...");
        // CEP Inexistente
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoExterno("99999999"));
        // Formato Inválido
        assertThrows(EnderecoException.class, () -> servicos.obterEnderecoExterno("ABC"));
    }
}