package unioeste.teste.funcionalidades;

import org.junit.jupiter.api.*;
import unioeste.geral.endereco.bo.EnderecoEspecifico;
import unioeste.geral.endereco.exception.EnderecoException;
import unioeste.geral.endereco.manager.UCEnderecoGeralServicos;
import unioeste.teste.utils.ContextoTestes;

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
    public void popularDezEnderecosValidos() {
        System.out.println(">>> [Teste 02] Populando banco com dados reais...");
        try {
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ, "1000", "Bloco 1");
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ, "2000", "Bloco 2");
            cadastrarAuxiliar(ContextoTestes.CEP_FOZ, "3000", "Lab");
            
            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO, "10", "Térreo");
            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO, "15", "Fundos");
            cadastrarAuxiliar(ContextoTestes.CEP_SAO_PAULO, "20", "Loja");
            
            cadastrarAuxiliar(ContextoTestes.CEP_CURITIBA, "500", "Centro");
            cadastrarAuxiliar(ContextoTestes.CEP_CURITIBA, "501", "Sala 2");
            
            cadastrarAuxiliar(ContextoTestes.CEP_RIO, "1", "Quiosque");
            cadastrarAuxiliar(ContextoTestes.CEP_RIO, "99", "Prédio Histórico");

            assertEquals(10, ContextoTestes.idsEnderecosGerados.size());
            System.out.println("   + Sucesso: 10 endereços inseridos.");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Falha ao popular: " + e.getMessage());
        }
    }

    private void cadastrarAuxiliar(String cep, String num, String comp) throws Exception {
        EnderecoEspecifico end = servicos.obterEnderecoExterno(cep);
        end.setNumero(num);
        end.setComplemento(comp);
        servicos.cadastrarEndereco(end);
        
        ContextoTestes.idsEnderecosGerados.add(end.getIdEnderecoEspecifico());
        
        int idCidade = end.getEndereco().getCidade().getIdCidade();
        if (!ContextoTestes.idsCidadesGeradas.contains(idCidade)) {
            ContextoTestes.idsCidadesGeradas.add(idCidade);
        }
    }

    @Test
    @Order(2)
    public void tentarCadastrosInvalidos() {
        assertThrows(EnderecoException.class, () -> servicos.cadastrarEndereco(null));
    }

    @Test
    @Order(3)
    public void tentarSqlInjection() {
        try {
            EnderecoEspecifico end = servicos.obterEnderecoExterno(ContextoTestes.CEP_FOZ);
            
            String ataqueCurto = "10' OR '1'='1"; 
            end.setNumero(ataqueCurto);
            
            servicos.cadastrarEndereco(end);
            
            EnderecoEspecifico filtro = new EnderecoEspecifico();
            filtro.setIdEnderecoEspecifico(end.getIdEnderecoEspecifico());
            
            EnderecoEspecifico salvo = servicos.obterEnderecoPorID(filtro);

            assertEquals(ataqueCurto, salvo.getNumero(), "O SQL Injection não deve ser processado, apenas salvo como texto.");
            
            System.out.println("   + Sucesso: SQL Injection neutralizado.");
        } catch (Exception e) {
            fail("Erro no teste de SQL Injection: " + e.getMessage());
        }
    }
}