package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Define que esta classe é a SEGUNDA a rodar
@Order(2)
public class Teste02_Consultas {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    public void consultarPorIdGerado() {
        System.out.println(">>> [Teste 02] Buscando pelo ID gerado anteriormente...");
        
        // Verifica se o teste anterior rodou com sucesso
        if (ContextoTestes.idEnderecoGerado == 0) {
            fail("O teste de cadastro anterior falhou ou não rodou, não temos ID para buscar.");
        }

        try {
            // Busca usando o ID estático
            // Criamos um objeto apenas para carregar o ID
            EnderecoEspecifico filtro = new EnderecoEspecifico();
            filtro.setIdEnderecoEspecifico(ContextoTestes.idEnderecoGerado);

            // Passamos o objeto
            EnderecoEspecifico recuperado = servicos.obterEnderecoPorID(filtro);
            
            assertNotNull(recuperado, "Deveria ter encontrado o endereço");
            assertEquals("100", recuperado.getNumero());
            assertEquals(ContextoTestes.cepTeste, recuperado.getEndereco().getCep());
            
            System.out.println(">>> [Teste 02] Sucesso na busca por ID.");

        } catch (Exception e) {
            fail("Erro ao buscar: " + e.getMessage());
        }
    }
    
    @Test
    public void consultarPorCep() {
        System.out.println(">>> [Teste 02] Buscando Lista por CEP...");
        try {
            List<EnderecoEspecifico> lista = servicos.obterEnderecoPorCEP(ContextoTestes.cepTeste);
            
            assertNotNull(lista);
            assertFalse(lista.isEmpty());
            
            // Verifica se o nosso endereço está na lista
            boolean achou = lista.stream()
                .anyMatch(e -> e.getIdEnderecoEspecifico() == ContextoTestes.idEnderecoGerado);
                
            assertTrue(achou, "O endereço cadastrado deveria estar na lista do CEP");

        } catch (Exception e) {
            fail("Erro na busca por CEP: " + e.getMessage());
        }
    }
}