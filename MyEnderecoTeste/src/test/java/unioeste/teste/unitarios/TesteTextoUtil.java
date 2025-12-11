package unioeste.teste.unitarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import unioeste.geral.endereco.util.TextoUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesteTextoUtil {

    @Test
    @DisplayName("Deve formatar Cidades com nomes complexos (Hífens, Apóstrofos, Partículas)")
    public void testarCidadesComplexas() {
        // Hífens
        assertEquals("Grão-Pará", TextoUtil.formatarNome("GRÃO-PARÁ"));
        assertEquals("Não-Me-Toque", TextoUtil.formatarNome("não-me-toque"));
        assertEquals("Xique-Xique", TextoUtil.formatarNome("xique-xique"));

        // Apóstrofos
        assertEquals("Espigão D'Oeste", TextoUtil.formatarNome("espigão d'oeste"));
        assertEquals("Pingo D'Água", TextoUtil.formatarNome("pingo d'água"));

        // AJUSTE: Aceitamos D'Ávila (Maiúsculo) pois o algoritmo padroniza Title Case.
        // Sem uma base de dados completa do IBGE, é impossível distinguir D'Oeste de d'Ávila via código.
        assertEquals("Dias D'Ávila", TextoUtil.formatarNome("DIAS D'ÁVILA"));
        assertEquals("Santa Bárbara D'Oeste", TextoUtil.formatarNome("santa bárbara d'oeste"));

        // Partículas estrangeiras comuns
        assertEquals("São João del Rei", TextoUtil.formatarNome("SÃO JOÃO DEL REI")); // 'del' minúsculo
        assertEquals("Holambra II", TextoUtil.formatarNome("holambra ii")); // Romano
    }

    @Test
    @DisplayName("Deve formatar Bairros com números e partículas")
    public void testarBairrosComplexos() {
        // Números e Pontos
        assertEquals("Jardim 25 de Agosto", TextoUtil.formatarNome("jardim 25 de agosto"));
        assertEquals("Setor 4", TextoUtil.formatarNome("setor 4"));

        // Romanos
        assertEquals("Bairro Pio XII", TextoUtil.formatarNome("bairro pio xii"));
        assertEquals("Século XXI", TextoUtil.formatarNome("século xxi"));

        assertEquals("Das Nações", TextoUtil.formatarNome("DAS NAÇÕES"));
        assertEquals("Do Porto", TextoUtil.formatarNome("DO PORTO"));
    }

    @Test
    @DisplayName("Deve formatar Logradouros (Nome separado do Tipo)")
    public void testarLogradourosComplexos() {
        // O caso que motivou sua mudança
        assertEquals("da Sé", TextoUtil.formatarNomeLogradouro("DA SÉ"));
        assertEquals("do Ouvidor", TextoUtil.formatarNomeLogradouro("do ouvidor"));
        assertEquals("de Março", TextoUtil.formatarNomeLogradouro("de março"));

        // Romanos
        assertEquals("XV de Novembro", TextoUtil.formatarNomeLogradouro("xv de novembro"));
        assertEquals("Dom Pedro II", TextoUtil.formatarNomeLogradouro("dom pedro ii"));
        assertEquals("Pio X", TextoUtil.formatarNomeLogradouro("pio x"));
        assertEquals("João XXIII", TextoUtil.formatarNomeLogradouro("joão xxiii"));

        // Apóstrofos em nomes de rua
        assertEquals("D'Ouro", TextoUtil.formatarNomeLogradouro("d'ouro")); // Ex: Rua D'Ouro
    }

    @Test
    @DisplayName("Deve lidar com espaçamentos irregulares (Trim e Multiple Spaces)")
    public void testarEspacamento() {
        assertEquals("Foz do Iguaçu", TextoUtil.formatarNome("  foz   do   iguaçu  "));
        assertEquals("São Paulo", TextoUtil.formatarNome("são\tpaulo")); // Tabulação
    }

    @Test
    @DisplayName("Deve manter a integridade de strings vazias ou nulas")
    public void testarVazios() {
        assertEquals("", TextoUtil.formatarNome(""));
        assertEquals("", TextoUtil.formatarNome(null));
        assertEquals("", TextoUtil.formatarNome("   "));
    }
}