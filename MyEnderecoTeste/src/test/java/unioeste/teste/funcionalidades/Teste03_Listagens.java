package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Order(3)
public class Teste03_Listagens {

    private static UCEnderecoGeralServicos servicos;

    @BeforeAll
    public static void setup() {
        servicos = new UCEnderecoGeralServicos();
    }

    @Test
    @DisplayName("Deve listar as UFs cadastradas no domínio")
    public void verificarUfs() throws Exception {
        System.out.println(">>> [Teste 03.1] Verificando listagem de UFs...");

        List<UnidadeFederativa> ufs = servicos.consultarUFs();

        assertNotNull(ufs, "A lista de UFs não deve ser nula.");
        assertFalse(ufs.isEmpty(), "A lista de UFs não deve estar vazia (deve conter dados do init.sql).");

        assertTrue(ufs.stream().anyMatch(u -> u.getSiglaUF().equalsIgnoreCase("PR")), "A lista deve conter o Paraná (PR)");
        assertTrue(ufs.stream().anyMatch(u -> u.getSiglaUF().equalsIgnoreCase("SP")), "A lista deve conter São Paulo (SP)");
    }

    @Test
    @DisplayName("Deve listar os Tipos de Logradouro (Rua, Avenida, etc)")
    public void verificarTiposLogradouro() throws Exception {
        System.out.println(">>> [Teste 03.2] Verificando listagem de Tipos de Logradouro...");

        List<TipoLogradouro> tipos = servicos.consultarTiposLogradouro();

        assertNotNull(tipos, "A lista de tipos não deve ser nula.");
        assertFalse(tipos.isEmpty(), "A lista não deve estar vazia.");

        assertTrue(tipos.stream().anyMatch(t -> t.getNomeTipoLogradouro().equalsIgnoreCase("Rua")), "Deve conter 'Rua'");
        assertTrue(tipos.stream().anyMatch(t -> t.getNomeTipoLogradouro().equalsIgnoreCase("Avenida")), "Deve conter 'Avenida'");
    }
}