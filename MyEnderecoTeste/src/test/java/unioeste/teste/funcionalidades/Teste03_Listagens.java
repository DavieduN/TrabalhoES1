package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.*;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Order(3) // AGORA É O TERCEIRO
public class Teste03_Listagens {

    private static UCEnderecoGeralServicos servicos = new UCEnderecoGeralServicos();

    @Test
    public void verificarVolumeDeCidades() throws Exception {
        System.out.println(">>> [Teste 03] Verificando listagem de Cidades...");
        List<Cidade> cidades = servicos.consultarCidades();
        
        // Esperamos pelo menos as 4 cidades que inserimos no Teste 02
        assertTrue(cidades.size() >= 4, "Deveria ter pelo menos 4 cidades cadastradas");
        
        boolean temFoz = cidades.stream().anyMatch(c -> c.getNomeCidade().contains("Foz do Iguaçu"));
        boolean temSP = cidades.stream().anyMatch(c -> c.getNomeCidade().contains("São Paulo"));
        boolean temCuritiba = cidades.stream().anyMatch(c -> c.getNomeCidade().contains("Curitiba"));
        boolean temRio = cidades.stream().anyMatch(c -> c.getNomeCidade().contains("Rio de Janeiro"));
        
        assertTrue(temFoz && temSP && temCuritiba && temRio, "Todas as cidades populadas devem aparecer na lista");
    }

    @Test
    public void verificarUfs() throws Exception {
        List<UnidadeFederativa> ufs = servicos.consultarUFs();
        
        // Esperamos PR, SP, RJ
        assertTrue(ufs.stream().anyMatch(u -> u.getSiglaUF().equals("PR")));
        assertTrue(ufs.stream().anyMatch(u -> u.getSiglaUF().equals("SP")));
        assertTrue(ufs.stream().anyMatch(u -> u.getSiglaUF().equals("RJ")));
    }

    @Test
    public void verificarTiposLogradouro() throws Exception {
        List<TipoLogradouro> tipos = servicos.consultarTiposLogradouro();
        
        // 85867900 -> Avenida Tancredo Neves
        // 01001000 -> Praça da Sé
        // 80020000 -> Praça Tiradentes (Geralmente)
        // 20040002 -> Rua da Assembléia
        
        assertTrue(tipos.stream().anyMatch(t -> t.getNomeTipoLogradouro().equals("Avenida")));
        assertTrue(tipos.stream().anyMatch(t -> t.getNomeTipoLogradouro().equals("Praça")));
        assertTrue(tipos.stream().anyMatch(t -> t.getNomeTipoLogradouro().equals("Rua")));
    }
}