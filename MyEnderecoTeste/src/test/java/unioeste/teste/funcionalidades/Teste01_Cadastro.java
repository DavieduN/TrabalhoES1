package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import static org.junit.jupiter.api.Assertions.*;

// Define que esta classe é a PRIMEIRA a rodar
@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Teste01_Cadastro {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    @Order(1)
    public void cadastrarNovoEndereco() {
        System.out.println(">>> [Teste 01] Iniciando Cadastro...");
        try {
            // 1. Busca dados do ViaCEP (simulando a tela)
            EnderecoEspecifico novo = servicos.obterEnderecoExterno(ContextoTestes.cepTeste);
            
            // 2. Preenche manual
            novo.setNumero("100");
            novo.setComplemento("Laboratório de Testes");

            // 3. Cadastra
            servicos.cadastrarEndereco(novo);

            // 4. Validações
            assertTrue(novo.getIdEnderecoEspecifico() > 0, "ID deve ser gerado");
            
            // 5. SALVA O ID NO CONTEXTO COMPARTILHADO
            ContextoTestes.idEnderecoGerado = novo.getIdEnderecoEspecifico();
            
            System.out.println(">>> [Teste 01] Sucesso! ID gerado: " + ContextoTestes.idEnderecoGerado);

        } catch (Exception e) {
            fail("Falha ao cadastrar: " + e.getMessage());
        }
    }
}