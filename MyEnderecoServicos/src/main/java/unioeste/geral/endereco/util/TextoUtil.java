package unioeste.geral.endereco.util;

import unioeste.geral.endereco.exception.EnderecoException;
import java.util.Arrays;
import java.util.List;

public class TextoUtil {

    private TextoUtil() {}

    private static final List<String> PARTICULAS = Arrays.asList(
            "de", "da", "do", "das", "dos", "e",
            "del", "di", "dall", "dalla",
            "em", "no", "na", "nos", "nas",
            "com", "por", "sem", "sob"
    );

    private static final List<String> ROMANOS = Arrays.asList(
            "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x",
            "xi", "xii", "xiii", "xiv", "xv", "xvi", "xvii", "xviii", "xix", "xx", "xxi", "xxii",
            "xxiii", "xxiv", "xxv", "xxvi", "xxvii", "xxviii", "xxix", "xxx", "xxxi"
    );

    // Validações (Mantidas iguais)
    public static void validarNome(String texto, String nomeCampo) throws EnderecoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new EnderecoException(nomeCampo + " é obrigatório.");
        }
        if (!texto.matches("^[\\p{L}\\s'-]+$")) {
            throw new EnderecoException(nomeCampo + " contém caracteres inválidos. Utilize apenas letras.");
        }
    }

    public static void validarTextoAlfanumerico(String texto, String nomeCampo) throws EnderecoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new EnderecoException(nomeCampo + " é obrigatório.");
        }
        if (!texto.matches("^[\\p{L}0-9\\s'.-]+$")) {
            throw new EnderecoException(nomeCampo + " contém caracteres inválidos.");
        }
    }

    /**
     * Formatação Padrão (Cidades, Bairros, Pessoas).
     * Força a primeira letra a ser Maiúscula ("Das Nações", "Do Herval").
     */
    public static String formatarNome(String texto) {
        return formatar(texto, true);
    }

    /**
     * Formatação Específica para Logradouros (Nome separado do Tipo).
     * Permite que a primeira palavra seja minúscula se for partícula ("da Sé", "do Ouvidor").
     */
    public static String formatarNomeLogradouro(String texto) {
        return formatar(texto, false);
    }

    // Lógica Central
    private static String formatar(String texto, boolean forcarPrimeiraMaiuscula) {
        if (texto == null || texto.isEmpty()) return "";

        String[] palavras = texto.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < palavras.length; i++) {
            String palavra = palavras[i];

            if (!palavra.isEmpty()) {
                // REGRA 1: Romanos -> Sempre MAIÚSCULO
                if (ROMANOS.contains(palavra)) {
                    sb.append(palavra.toUpperCase());
                }
                // REGRA 2: Partículas
                // Se for a 1ª palavra E forçarmos maiúscula -> Capitaliza (Ex: "Das Nações")
                // Se for a 1ª palavra E NÃO forçarmos -> Minúscula (Ex: "da Sé")
                // Se estiver no meio -> Minúscula (Ex: "Foz do Iguaçu")
                else if (PARTICULAS.contains(palavra)) {
                    if (i == 0 && forcarPrimeiraMaiuscula) {
                        sb.append(capitalizarTermo(palavra));
                    } else if (i == 0) {
                        sb.append(palavra); // Deixa minúsculo no início (caso logradouro)
                    } else {
                        sb.append(palavra); // Deixa minúsculo no meio
                    }
                }
                // REGRA 3: Palavras normais -> Capitaliza
                else {
                    sb.append(capitalizarTermo(palavra));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static String capitalizarTermo(String termo) {
        StringBuilder sb = new StringBuilder();
        boolean deveCapitalizar = true;

        for (char c : termo.toCharArray()) {
            if (deveCapitalizar) {
                sb.append(Character.toUpperCase(c));
                deveCapitalizar = false;
            } else {
                sb.append(c);
            }
            if (c == '\'' || c == '-') {
                deveCapitalizar = true;
            }
        }
        return sb.toString();
    }
}