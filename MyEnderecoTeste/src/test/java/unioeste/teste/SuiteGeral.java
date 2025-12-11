package unioeste.teste;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import unioeste.teste.funcionalidades.*;
import unioeste.teste.unitarios.*;

@Suite
@SelectClasses({
        Teste01_IntegracaoExterna.class,
        Teste02_CadastroEPopulacao.class,
        Teste03_Listagens.class,
        Teste04_BuscaPorId.class,
        Teste05_BuscaPorCep.class,
        TesteTextoUtil.class
})
public class SuiteGeral {

}