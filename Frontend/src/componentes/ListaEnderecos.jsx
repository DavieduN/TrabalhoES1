import React, { useState } from 'react';
// Importação dos serviços de domínio (Endereço e Cidade)
import { consultarLocalPorCEP, buscarEnderecoPorID, buscarCidadePorID } from '../api/enderecoApi';
import CardEndereco from './CardEndereco';

/**
 * Componente: Painel de Consultas (ListaEnderecos)
 * * * Responsabilidade:
 * Atua como um container centralizador para operações de leitura (Read) do sistema.
 * Implementa o padrão "Thin Client", removendo validações de frontend para garantir
 * que todas as regras de negócio (incluindo validação de tipos e formatos) sejam
 * processadas exclusivamente pela Camada de Serviço (Backend).
 * * * Funcionalidades:
 * 1. Busca Massiva: Retorna lista de endereços por CEP.
 * 2. Busca Pontual: Retorna uma entidade Endereço por ID.
 * 3. Busca Auxiliar: Retorna uma entidade Cidade por ID (Prova de conceito do serviço `obterCidade`).
 */
export default function ListaEnderecos() {
  
  // ===========================================================================
  // GERENCIAMENTO DE ESTADO (State Management)
  // ===========================================================================

  // --- Contexto 1: Busca de Endereços por CEP (One-to-Many) ---
  const [cepBusca, setCepBusca] = useState('');
  const [listaEnderecos, setListaEnderecos] = useState(null); // null = estado inicial (sem busca realizada)
  const [loadingCEP, setLoadingCEP] = useState(false);
  const [errorCEP, setErrorCEP] = useState(null);

  // --- Contexto 2: Busca de Endereço por ID (One-to-One) ---
  const [idBusca, setIdBusca] = useState('');
  const [enderecoEncontrado, setEnderecoEncontrado] = useState(null);
  const [loadingID, setLoadingID] = useState(false);
  const [errorID, setErrorID] = useState(null);

  // --- Contexto 3: Busca de Cidade por ID (Entity Lookup) ---
  const [idCidadeBusca, setIdCidadeBusca] = useState('');
  const [cidadeEncontrada, setCidadeEncontrada] = useState(null);
  const [loadingCidade, setLoadingCidade] = useState(false);
  const [errorCidade, setErrorCidade] = useState(null);


  // ===========================================================================
  // CONTROLADORES DE EVENTOS (Event Handlers)
  // ===========================================================================

  /**
   * Handler: Busca por CEP.
   * * Fluxo:
   * 1. Envia o input bruto (raw) do usuário para a API.
   * 2. O Backend valida se o CEP tem 8 dígitos ou caracteres inválidos.
   * 3. Renderiza a lista retornada ou exibe o erro do backend.
   */
  async function handleBuscarPorCEP(e) {
    e?.preventDefault();
    
    setLoadingCEP(true);
    setErrorCEP(null);
    setListaEnderecos([]);

    try {
      // Chamada ao serviço `obterEnderecoPorCEP`
      const data = await consultarLocalPorCEP(cepBusca);
      
      // Garante que o estado seja sempre um array para evitar quebra na renderização (Map)
      setListaEnderecos(Array.isArray(data) ? data : []);
    } catch (err) {
      // Captura exceções de negócio (ex: "CEP Inválido") lançadas pelo Backend
      setErrorCEP(err.message || 'Erro ao buscar.');
    } finally {
      setLoadingCEP(false);
    }
  }

  /**
   * Handler: Busca Endereço por ID.
   * * Fluxo:
   * 1. Converte o input para Number (necessário para a assinatura do método Java/JSON).
   * 2. Se o usuário digitar texto, a conversão gera NaN ou o backend retorna 400 Bad Request.
   */
  async function handleBuscarPorID(e) {
    e?.preventDefault();
    
    setLoadingID(true);
    setErrorID(null);
    setEnderecoEncontrado(null);

    try {
      // Chamada ao serviço `obterEnderecoPorID`
      const data = await buscarEnderecoPorID(Number(idBusca));
      setEnderecoEncontrado(data);
    } catch (err) {
      setErrorID(err.message || 'Não encontrado.');
    } finally {
      setLoadingID(false);
    }
  }

  /**
   * Handler: Busca Cidade por ID.
   * * Fluxo:
   * 1. Consulta entidade isolada de Cidade.
   * 2. Aplica validação de nulidade (Defensive Programming) caso o backend retorne 200 OK vazio.
   */
  async function handleBuscarCidade(e) {
    e?.preventDefault();
    
    setLoadingCidade(true);
    setErrorCidade(null);
    setCidadeEncontrada(null);

    try {
      // Chamada ao serviço `obterCidade`
      const data = await buscarCidadePorID(Number(idCidadeBusca));
      
      // Validação defensiva de resposta
      if (!data) throw new Error("Cidade não encontrada");
      
      setCidadeEncontrada(data);
    } catch (err) {
      setErrorCidade(err.message || 'Cidade não encontrada.');
    } finally {
      setLoadingCidade(false);
    }
  }

  // ===========================================================================
  // CAMADA DE APRESENTAÇÃO (View Layer)
  // ===========================================================================
  return (
    <div className="space-y-6">
      
      {/* -------------------------------------------------------- */}
      {/* PAINEL 1: BASE LOCAL POR CEP                             */}
      {/* -------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg flex flex-col h-[450px]">
        <h2 className="text-lg font-bold text-white mb-4">Base Local (Por CEP)</h2>
        
        <form className="flex gap-2 mb-4" onSubmit={handleBuscarPorCEP}>
          <input 
            className="input-modern" 
            placeholder="CEP..." 
            value={cepBusca} 
            onChange={e => setCepBusca(e.target.value)} // Input sem máscara (Raw)
          />
          <button disabled={loadingCEP} className="btn-modern btn-primary">{loadingCEP ? '...' : 'Listar'}</button>
        </form>

        <div className="flex-1 overflow-y-auto space-y-3 pr-2 custom-scrollbar">
          {/* Feedback de Erro */}
          {errorCEP && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorCEP}</div>}
          
          {/* Lista de Resultados */}
          {listaEnderecos?.map((item) => (
            <CardEndereco key={item.idEnderecoEspecifico || item.id} endereco={item} />
          ))}
          
          {/* Empty State */}
          {listaEnderecos?.length === 0 && !errorCEP && !loadingCEP && <div className="text-slate-500 text-center py-10">Nada encontrado.</div>}
        </div>
      </div>

      {/* -------------------------------------------------------- */}
      {/* PAINEL 2: BUSCA ENDEREÇO POR ID                          */}
      {/* -------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-4">Busca Endereço por ID</h2>
        <form className="flex gap-2 mb-4" onSubmit={handleBuscarPorID}>
          <input className="input-modern" placeholder="ID Endereço..." value={idBusca} onChange={e => setIdBusca(e.target.value)} />
          <button disabled={loadingID} className="btn-modern bg-purple-600 text-white hover:bg-purple-500">Buscar</button>
        </form>
        {enderecoEncontrado && <CardEndereco endereco={enderecoEncontrado} />}
        {errorID && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorID}</div>}
      </div>

      {/* -------------------------------------------------------- */}
      {/* PAINEL 3: BUSCA CIDADE POR ID (AUXILIAR)                 */}
      {/* -------------------------------------------------------- */}
      <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-2 flex items-center gap-2">
          {/* Ícone Decorativo */}
          <svg className="w-5 h-5 text-emerald-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" /></svg>
          Busca Cidade (Auxiliar)
        </h2>
        <p className="text-xs text-slate-400 mb-4">Teste do serviço `obterCidade`.</p>

        <form className="flex gap-2 mb-4" onSubmit={handleBuscarCidade}>
          <input className="input-modern" placeholder="ID Cidade..." value={idCidadeBusca} onChange={e => setIdCidadeBusca(e.target.value)} />
          <button disabled={loadingCidade} className="btn-modern bg-emerald-600 text-white hover:bg-emerald-500">Buscar</button>
        </form>

        {/* Card Ad-hoc para exibição de Cidade (Sem componente dedicado) */}
        {cidadeEncontrada && (
          <div className="animate-enter bg-slate-700/50 p-4 rounded border border-slate-600">
            <div className="text-xs text-emerald-400 uppercase font-bold mb-1">Cidade Encontrada:</div>
            <div className="text-lg font-bold text-white">
              {cidadeEncontrada.nomeCidade || cidadeEncontrada.nome}
            </div>
            <div className="text-sm text-slate-300">
              Estado: {cidadeEncontrada.unidadeFederativa?.nomeUF || cidadeEncontrada.uf || '-'} ({cidadeEncontrada.unidadeFederativa?.siglaUF || '-'})
            </div>
          </div>
        )}
        
        {errorCidade && <div className="p-3 bg-red-900/20 text-red-300 rounded text-sm">{errorCidade}</div>}
      </div>

    </div>
  );
}