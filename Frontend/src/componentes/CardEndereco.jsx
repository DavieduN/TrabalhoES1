import React from 'react';

/**
 * Componente Visual: Card de Endereço.
 * * Responsável pela apresentação de dados de endereço em um formato de cartão (Card).
 * Implementa o padrão "Presentational Component" (Burro), focando apenas em UI
 * e recebendo dados via props, sem realizar chamadas de API ou lógica de negócio.
 * * @param {Object} props - Propriedades do componente.
 * @param {Object|null} props.endereco - Objeto contendo os dados do endereço. 
 * Pode ser null (não renderiza nada), um objeto plano (ViaCEP) ou aninhado (Backend Java).
 * * @returns {JSX.Element|null} Elemento JSX renderizado ou null se não houver dados.
 */
export default function CardEndereco({ endereco }) {
  // Defensive Programming: Retorna null imediatamente se não houver dados, evitando erros de renderização.
  if (!endereco) return null;

  /**
   * Helper: Normalizador de Dados (Adapter Pattern).
   * * Esta função resolve a discrepância entre diferentes formatos de dados que podem chegar ao front-end:
   * 1. Formato Java Entity (Aninhado): { id: 1, endereco: { logradouro: ... } }
   * 2. Formato ViaCEP/DTO (Plano): { logradouro: ..., localidade: ... }
   * * @param {Object} data - Objeto de dados bruto.
   * @returns {Object} Objeto normalizado com estrutura previsível para a View.
   */
  const extractData = (data) => {
    // Tenta localizar a raiz dos dados geográficos. Se existir 'data.endereco', usa ele (Backend).
    // Caso contrário, assume que a raiz 'data' já contém os campos (ViaCEP).
    const geoRoot = data.endereco || data;
    
    // Dados específicos (como ID e Número) geralmente ficam na raiz do objeto principal.
    const specRoot = data; 
    
    return {
      // Resolução de ID: Tenta diferentes convenções de nomenclatura (_id, id, idEnderecoEspecifico)
      id: specRoot.idEnderecoEspecifico || specRoot.id || specRoot._id,
      
      // Resolução de Logradouro: Prioriza estrutura aninhada, depois plana, com fallback padrão.
      logradouro: geoRoot.logradouro?.nomeLogradouro || 
                  geoRoot.nomeLogradouro || 
                  geoRoot.logradouro || 
                  'Logradouro não informado',
                  
      // Resolução de Bairro
      bairro: geoRoot.bairro?.nomeBairro || geoRoot.bairro,
      
      // Resolução de Cidade: Trata 'localidade' (padrão ViaCEP) e 'nomeCidade' (padrão Java)
      cidade: geoRoot.cidade?.nomeCidade || geoRoot.cidade || geoRoot.localidade,
      
      // Resolução de UF: Navega profundamente no objeto Cidade -> UF se necessário
      uf: geoRoot.cidade?.unidadeFederativa?.siglaUF || geoRoot.uf,
      
      // Campos diretos
      numero: specRoot.numero,
      complemento: specRoot.complemento,
      
      // Resolução de CEP
      cep: geoRoot.cep || specRoot.cep,
    };
  };

  // Executa a normalização
  const dados = extractData(endereco);

  // --- Renderização (View) ---
  return (
    <div className="relative bg-slate-800 border border-slate-700 rounded-lg p-4 shadow-sm hover:border-blue-500 transition-colors group">
      
      {/* Elemento Decorativo: Faixa lateral indicativa de status/grupo */}
      <div className="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-l-lg"></div>
      
      <div className="pl-3 flex flex-col gap-1">
        
        {/* Linha Superior: Logradouro e Número */}
        <div className="flex justify-between items-start">
          <h3 className="font-semibold text-slate-100 text-sm">
            {dados.logradouro}
            {dados.numero && <span className="text-blue-400 ml-1">, {dados.numero}</span>}
          </h3>
          
          {/* Badge de ID: Exibido apenas se o ID existir (útil para debug e referência) */}
          {dados.id && (
            <span className="text-[10px] font-mono font-bold text-slate-400 bg-slate-900 px-2 py-0.5 rounded border border-slate-700">
              #{dados.id}
            </span>
          )}
        </div>

        {/* Linha Intermediária: Complemento e Bairro */}
        <div className="text-xs text-slate-400">
          {/* Renderização Condicional: Só exibe o complemento e o separador se houver valor */}
          {dados.complemento && <span className="italic text-slate-500 mr-2">{dados.complemento} •</span>}
          {dados.bairro}
        </div>

        {/* Linha Inferior: Cidade, UF e CEP (Com borda superior separadora) */}
        <div className="flex items-center justify-between mt-2 pt-2 border-t border-slate-700/50">
          <span className="text-xs font-medium text-slate-300 bg-slate-700/50 px-2 py-0.5 rounded">
            {dados.cidade}/{dados.uf}
          </span>
          <span className="text-xs font-mono text-slate-500">
            CEP {dados.cep}
          </span>
        </div>

      </div>
    </div>
  );
}