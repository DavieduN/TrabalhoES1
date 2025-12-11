package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
public class Teste02_CadastroEPopulacao {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
        ContextoTestes.idsEnderecosGerados.clear();
        ContextoTestes.idsCidadesGeradas.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Deve popular base e testar o reaproveitamento de endereços (Idempotência)")
    public void popularEnderecos() {
        System.out.println(">>> [Teste 02.1] Populando banco e testando Idempotência...");
        try {
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ);
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ);
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ);

            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO);
            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO);
            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO);

            cadastrarAuxiliar(ContextoTestes.CEP_CURITIBA);
            cadastrarAuxiliar(ContextoTestes.CEP_CURITIBA);

            cadastrarAuxiliar(ContextoTestes.CEP_RIO);
            cadastrarAuxiliar(ContextoTestes.CEP_RIO);

            assertEquals(10, ContextoTestes.idsEnderecosGerados.size(), "Deveriam ter sido processadas 10 solicitações.");

            Set<Integer> idsUnicos = new HashSet<>(ContextoTestes.idsEnderecosGerados);
            assertEquals(4, idsUnicos.size(), "Deveriam existir apenas 4 endereços únicos no banco devido ao 'obterOuCadastrar'.");

            System.out.println("   + Sucesso: 10 requisições processadas, resultando em 4 registros únicos (Economia de espaço).");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Falha ao popular: " + e.getMessage());
        }
    }

    private void cadastrarAuxiliar(String cep) throws Exception {
        Endereco endExterno = servicos.obterEnderecoExterno(cep);

        Endereco salvo = servicos.cadastrarEndereco(endExterno);

        ContextoTestes.idsEnderecosGerados.add((int) salvo.getIdEndereco());

        int idCidade = salvo.getCidade().getIdCidade();
        if (!ContextoTestes.idsCidadesGeradas.contains(idCidade)) {
            ContextoTestes.idsCidadesGeradas.add(idCidade);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Deve rejeitar cadastros nulos ou incompletos")
    public void tentarCadastrosInvalidos() {
        System.out.println(">>> [Teste 02.2] Testando Validações de Entrada...");

        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(null));

        Endereco vazio = new Endereco();
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(vazio));
    }

    @Test
    @Order(3)
    @DisplayName("Deve barrar SQL Injection e caracteres inválidos via Validação Regex")
    public void tentarAtaquesMaliciosos() {
        System.out.println(">>> [Teste 02.3] Testando Segurança e SQL Injection...");
        try {
            Endereco end = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);

            String ataque = "Bairro Malicioso'; DROP TABLE Endereco; --";

            Bairro bairroFake = new Bairro();
            bairroFake.setNomeBairro(ataque);
            end.setBairro(bairroFake);

            Exception exception = assertThrows(EnderecoException.class, () -> {
                servicos.cadastrarEndereco(end);
            });

            System.out.println("   + Ataque Barrado: " + exception.getMessage());
            assertTrue(exception.getMessage().contains("caracteres inválidos"), "Deve acusar caractere inválido (ponto e vírgula)");

            Endereco endValido = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);
            Bairro bairroComplexo = new Bairro();
            bairroComplexo.setNomeBairro("Jardim D'Água-Viva");
            endValido.setBairro(bairroComplexo);

            assertDoesNotThrow(() -> servicos.cadastrarEndereco(endValido), "Deveria aceitar apóstrofos e hífens legítimos");

        } catch (Exception e) {
            fail("Erro no teste de segurança: " + e.getMessage());
        }
    }
}