package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

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
    public void buscarCepFozMultiplosResultados() {
        System.out.println(">>> [Teste 05] Buscando lista CEP Foz (" + ContextoTestes.CEP_FOZ + ")...");
        try {
            List<EnderecoEspecifico> lista = servicos.obterEnderecoPorCEP(ContextoTestes.CEP_FOZ);
            
            assertNotNull(lista);
            assertTrue(lista.size() >= 3, "Deveria ter encontrado pelo menos 3 endereços para Foz");
            
            for (EnderecoEspecifico e : lista) {
                assertEquals(ContextoTestes.EXPECTED_CIDADE_FOZ, e.getEndereco().getCidade().getNomeCidade());
                assertEquals(ContextoTestes.EXPECTED_UF_FOZ, e.getEndereco().getCidade().getUnidadeFederativa().getSiglaUF());
            }
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarCepSaoPaulo() {
        try {
            List<EnderecoEspecifico> lista = servicos.obterEnderecoPorCEP(ContextoTestes.CEP_SAO_PAULO);
            assertTrue(lista.size() >= 3);
            
            assertEquals(ContextoTestes.EXPECTED_LOG_SP, lista.get(0).getEndereco().getLogradouro().getNomeLogradouro());
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void buscarCepSemResultados() {
        try {
            List<EnderecoEspecifico> lista = servicos.obterEnderecoPorCEP("11111111");
            assertNotNull(lista);
            assertTrue(lista.isEmpty(), "A lista deveria vir vazia, não nula");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void buscarCepInvalido() {
        assertThrows(Exception.class, () -> servicos.obterEnderecoPorCEP("123"), "Deveria lançar erro de formato");
        assertThrows(Exception.class, () -> servicos.obterEnderecoPorCEP(null), "Deveria tratar nulo");
    }
}