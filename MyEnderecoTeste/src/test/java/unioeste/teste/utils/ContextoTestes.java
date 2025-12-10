package unioeste.teste.utils;

import java.util.ArrayList;
import java.util.List;

public class ContextoTestes {
    // --- DADOS DE ENTRADA (CEPs) ---
    public static final String CEP_FOZ = "85867900"; 
    public static final String CEP_SAO_PAULO = "01001000"; 
    public static final String CEP_CURITIBA = "80020000";
    public static final String CEP_RIO = "20040002"; // Adicionado Rio

    // --- DADOS ESPERADOS (Validação Exata do JSON do ViaCEP) ---
    
    // Foz do Iguaçu (85867-900)
    public static final String EXPECTED_CIDADE_FOZ = "Foz do Iguaçu";
    public static final String EXPECTED_UF_FOZ = "PR";
    public static final String EXPECTED_BAIRRO_FOZ = "Porto Belo"; // Corrigido conforme seu JSON
    public static final String EXPECTED_TIPO_FOZ = "Avenida";
    public static final String EXPECTED_LOG_FOZ = "Tancredo Neves"; // Sem o "Avenida"

    // São Paulo (01001-000)
    public static final String EXPECTED_CIDADE_SP = "São Paulo";
    public static final String EXPECTED_UF_SP = "SP";
    public static final String EXPECTED_BAIRRO_SP = "Sé";
    public static final String EXPECTED_TIPO_SP = "Praça";
    public static final String EXPECTED_LOG_SP = "da Sé"; // Sem "Praça"

    // Curitiba (80020-000)
    public static final String EXPECTED_CIDADE_CUR = "Curitiba";
    public static final String EXPECTED_UF_CUR = "PR";
    public static final String EXPECTED_BAIRRO_CUR = "Centro";
    public static final String EXPECTED_TIPO_CUR = "Rua";
    public static final String EXPECTED_LOG_CUR = "Voluntários da Pátria";

    // Rio de Janeiro (20040-002)
    public static final String EXPECTED_CIDADE_RIO = "Rio de Janeiro";
    public static final String EXPECTED_UF_RIO = "RJ";
    public static final String EXPECTED_BAIRRO_RIO = "Centro";
    public static final String EXPECTED_TIPO_RIO = "Avenida";
    public static final String EXPECTED_LOG_RIO = "Rio Branco";

    // --- ESTADO COMPARTILHADO ---
    public static List<Integer> idsEnderecosGerados = new ArrayList<>();
    public static List<Integer> idsCidadesGeradas = new ArrayList<>();
}