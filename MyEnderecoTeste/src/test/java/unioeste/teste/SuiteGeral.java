package unioeste.teste;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import unioeste.teste.funcionalidades.*;
import unioeste.teste.unitarios.*;

@Suite
@SelectClasses({
        // Ordem estrita de execução:
        Teste01_IntegracaoExterna.class,  // Apenas consulta externa (não grava)
        Teste02_CadastroEPopulacao.class, // POPULA O BANCO (Crucial rodar antes dos outros)
        Teste03_Listagens.class,          // Consulta dados básicos
        Teste04_BuscaPorId.class,         // Consulta dados gerados no teste 02
        Teste05_BuscaPorCep.class,         // Consulta dados gerados no teste 02
        TesteTextoUtil.class
})
public class SuiteGeral {
    // Esta classe fica vazia. Ela serve apenas como container para as anotações.
}