import React, { useState } from 'react';
// Importação dos serviços, incluindo busca de cidade
import { consultarLocalPorCEP, buscarEnderecoPorID, buscarCidadePorID } from '../api/enderecoApi';
import CardEndereco from './CardEndereco';

/**
 * Componente: ListaEnderecos (Container de Consultas)
 * * Responsabilidade:
 * Atua como um painel centralizador para operações de leitura (Read) do sistema de Endereços.
 * Segue o padrão de projeto "Thin Client" (Cliente Magro), onde:
 * 1. Não realiza validações de regra de negócio (delega ao Backend via API).
 * 2. Envia inputs brutos (raw) para os serviços.
 * 3. Gerencia estados de UI (loading, erro, sucesso) baseados nas respostas HTTP.
 * * Funcionalidades:
 * - Listagem de endereços por CEP (One-to-Many).
 * - Busca de endereço específico por ID (One-to-One).
 * - Busca auxiliar de Cidade por ID (Entity Lookup).
 */
export default function ListaEnderecos() {
  
  // ===========================================================================
  // ESTADOS (State Management)
  // ===========================================================================

  // --- Contexto: Busca Massiva (Por CEP) ---
  const [cepBusca, setCepBusca] = useState('');
  const [listaEnderecos, setListaEnderecos] = useState(null); // null indica estado inicial (sem busca)
  const [loadingCEP, setLoadingCEP] = useState(false);
  const [errorCEP, setErrorCEP] = useState(null);

  // --- Contexto: Busca Específica (Endereço por ID) ---
  const [idBusca, setIdBusca] = useState('');
  const [enderecoEncontrado, setEnderecoEncontrado] = useState(null);
  const [loadingID, setLoadingID] = useState(false);
  const [errorID, setErrorID] = useState(null);

  // --- Contexto: Busca Auxiliar (Cidade por ID) ---
  const [idCidadeBusca, setIdCidadeBusca] = useState('');
  const [cidadeEncontrada, setCidadeEncontrada] = useState(null);
  const [loadingCidade, setLoadingCidade] = useState(false);
  const [errorCidade, setErrorCidade] = useState(null);


  // ===========================================================================
  // HANDLERS (Controladores de Eventos)
  // ===========================================================================

  /**
   * Executa a consulta de endereços baseada em um CEP.
   * Serviço consumido: `obterEnderecoPorCEP`
   * * Comportamento:
   * - Envia o CEP sem pré-processamento (raw string).
   * - Trata resposta vazia como array vazio [].
   * - Exibe erros de validação retornados pelo servidor (ex: "CEP Inválido").
   */
  async function handleBuscarPorCEP(e) {
    e?.preventDefault();
    
    // Inicia estado de carregamento
    setLoadingCEP(true);
    setErrorCEP(null);
    setListaEnderecos([]);

    try {
      // Chamada assíncrona ao serviço de domínio
      const data = await consultarLocalPorCEP(cepBusca);
      
      // Garante integridade do tipo Array para o renderizador
      setListaEnderecos(Array.isArray(data) ? data : []);
    } catch (err) {
      // Captura e exibe a mensagem de exceção vinda da camada de serviço/backend
      setErrorCEP(err.message || 'Erro ao buscar.');
    } finally {
      setLoadingCEP(false);
    }
  }

  /**
   * Busca uma entidade Endereço única pelo seu Identificador (ID).
   * Serviço consumido: `obterEnderecoPorID`
   * * Comportamento:
   * - Envia o ID como string para o serviço (conversão implícita ou tratamento no back).
   */
  async function handleBuscarPorID(e) {
    e?.preventDefault();
    
    setLoadingID(true);
    setErrorID(null);
    setEnderecoEncontrado(null);

    try {
      // O input do usuário é enviado diretamente. A validação numérica é responsabilidade do DTO/Backend.
      const data = await buscarEnderecoPorID(idBusca);
      setEnderecoEncontrado(data);
    } catch (err) {
      setErrorID(err.message || 'Não encontrado.');
    } finally {
      setLoadingID(false);
    }
  }

  /**
   * Busca uma entidade Cidade única pelo seu ID.
   * Serviço consumido: `obterCidade`
   * * Nota: Esta é uma funcionalidade auxiliar para validar a integridade referencial
   * ou checar existência de cidades cadastradas independentemente de endereços.
   */
  async function handleBuscarCidade(e) {
    e?.preventDefault();
    
    setLoadingCidade(true);
    setErrorCidade(null);
    setCidadeEncontrada(null);

    try {
      const data = await buscarCidadePorID(idCidadeBusca);
      
      // Validação de nulidade (Defensive Programming) caso o backend retorne 200 OK mas corpo vazio
      if (!data) throw new Error("Cidade não encontrada");
      
      setCidadeEncontrada(data);
    } catch (err) {
      setErrorCidade(err.message || 'Cidade não encontrada.');
    } finally {
      setLoadingCidade(false);
    }
  }

  // ===========================================================================
  // RENDERIZAÇÃO (View Layer)
  // ===========================================================================
  return (
    <div className="space-y-6">
      
      {/* -------------------------------------------------------------------------- */}
      {/* BOX 1: PAINEL DE CONSULTA POR CEP                                          */}
      {/* -------------------------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg flex flex-col h-[450px]">
        <h2 className="text-lg font-bold text-white mb-4">Base Local (Por CEP)</h2>
        
        <form className="flex gap-2 mb-4" onSubmit={handleBuscarPorCEP}>
          <input 
            className="input-modern" 
            placeholder="CEP..." 
            value={cepBusca} 
            // Two-way binding sem filtros (Input Raw)
            onChange={e => setCepBusca(e.target.value)} 
          />
          <button disabled={loadingCEP} className="btn-modern btn-primary">
            {loadingCEP ? '...' : 'Listar'}
          </button>
        </form>

        <div className="flex-1 overflow-y-auto space-y-3 pr-2 custom-scrollbar">
          {/* Feedback de Erro do Servidor */}
          {errorCEP && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorCEP}</div>}
          
          {/* Lista de Resultados (Iteração de Componentes) */}
          {listaEnderecos?.map((item) => (
            <CardEndereco key={item.idEnderecoEspecifico || item.id} endereco={item} />
          ))}
          
          {/* Estado Vazio (Empty State) */}
          {listaEnderecos?.length === 0 && !errorCEP && !loadingCEP && (
            <div className="text-slate-500 text-center py-10">Nada encontrado.</div>
          )}
        </div>
      </div>

      {/* -------------------------------------------------------------------------- */}
      {/* BOX 2: PAINEL DE BUSCA POR ID (ENTIDADE ENDEREÇO)                          */}
      {/* -------------------------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-4">Busca Endereço por ID</h2>
        <form className="flex gap-2 mb-4" onSubmit={handleBuscarPorID}>
          <input 
            className="input-modern" 
            placeholder="ID Endereço..." 
            value={idBusca} 
            onChange={e => setIdBusca(e.target.value)} 
          />
          <button disabled={loadingID} className="btn-modern bg-purple-600 text-white hover:bg-purple-500">
            Buscar
          </button>
        </form>
        
        {/* Renderização Condicional do Resultado */}
        {enderecoEncontrado && <CardEndereco endereco={enderecoEncontrado} />}
        {errorID && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorID}</div>}
      </div>

      {/* -------------------------------------------------------------------------- */}
      {/* BOX 3: PAINEL DE BUSCA POR ID (ENTIDADE CIDADE)                            */}
      {/* -------------------------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-2 flex items-center gap-2">
          {/* Ícone SVG inline para decoração */}
          <svg className="w-5 h-5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
          </svg>
          Busca Cidade (Auxiliar)
        </h2>
        <p className="text-xs text-slate-400 mb-4">Teste do serviço `obterCidade`.</p>

        <form className="flex gap-2 mb-4" onSubmit={handleBuscarCidade}>
          <input 
            className="input-modern" 
            placeholder="ID Cidade..." 
            value={idCidadeBusca} 
            onChange={e => setIdCidadeBusca(e.target.value)} 
          />
          <button disabled={loadingCidade} className="btn-modern bg-emerald-600 text-white hover:bg-emerald-500">
            Buscar
          </button>
        </form>

        {/* Card Ad-hoc para exibição simples de Cidade (sem componente separado) */}
        {cidadeEncontrada && (
          <div className="animate-enter bg-slate-700/50 p-4 rounded border border-slate-600">
            <div className="text-xs text-emerald-400 uppercase font-bold mb-1">Cidade Encontrada:</div>
            <div className="text-lg font-bold text-white">
              {cidadeEncontrada.nomeCidade || cidadeEncontrada.nome}
            </div>
            <div className="text-sm text-slate-300">
              Estado: {cidadeEncontrada.unidadeFederativa?.nomeUF || cidadeEncontrada.uf || '-'} 
              <span className="ml-1 text-slate-400">({cidadeEncontrada.unidadeFederativa?.siglaUF || '-'})</span>
            </div>
          </div>
        )}
        
        {errorCidade && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorCidade}</div>}
      </div>

    </div>
  );
}