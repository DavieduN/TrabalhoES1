import React, { useEffect, useState, useMemo } from 'react';
import CardEndereco from './CardEndereco'; 
import { listarUFs, listarTiposLogradouro, consultarExternoPorCEP, cadastrarEndereco } from '../api/enderecoApi';

/**
 * Componente: Formulário de Manutenção de Endereço (FormEndereco)
 * * Responsabilidade:
 * Implementa a funcionalidade de Manter Endereço. Atua como o "Thin Client" (Cliente Magro), 
 * focando apenas na coleta de dados, gerenciamento de estado de UI (carregamento/erros)
 * e delegação de todas as regras de negócio para a Camada de Serviço (Backend).
 * * Funcionalidades de Domínio Consumidas:
 * - listarUFs, listarTiposLogradouro: Para preenchimento dos campos Select.
 * - consultarExternoPorCEP: Para preenchimento automático.
 * - cadastrarEndereco: Para persistência no banco local.
 */
export default function FormEndereco() {
  
  // ===========================================================================
  // GERENCIAMENTO DE ESTADO (State Management)
  // ===========================================================================
  
  // --- Estados de Domínio (Dados do Formulário) ---
  const [ufs, setUfs] = useState([]);
  const [tipos, setTipos] = useState([]);
  
  const [cep, setCep] = useState('');
  const [tipoLogradouro, setTipoLogradouro] = useState('');
  const [nomeLogradouro, setNomeLogradouro] = useState('');
  const [numero, setNumero] = useState('');
  const [complemento, setComplemento] = useState('');
  const [bairro, setBairro] = useState('');
  const [cidade, setCidade] = useState('');
  const [uf, setUf] = useState('');

  // --- Estados de UI (Interface) ---
  const [status, setStatus] = useState(null); // { type: 'loading' | 'success' | 'error', text: string }
  const isLoading = status?.type === 'loading';
  const [enderecoVisualizacao, setEnderecoVisualizacao] = useState(null);


  // ===========================================================================
  // EFEITOS E CARGA INICIAL (Lifecycle)
  // ===========================================================================

  /**
   * Efeito: Carrega UFs e Tipos de Logradouro no Lifecycle do componente.
   * * Importante: A UI depende do backend fornecer os dados de domínio para os Selects.
   */
  useEffect(() => {
    listarUFs().then(setUfs).catch(() => setUfs([]));
    listarTiposLogradouro().then(setTipos).catch(() => setTipos([]));
  }, []);


  // ===========================================================================
  // HANDLERS (Controladores de Eventos)
  // ===========================================================================

  /**
   * Handler: Busca Endereço em Serviço Externo (ViaCEP simulado).
   * * Comportamento Thin Client:
   * 1. Limpa o CEP (remove não-dígitos) apenas para o formato da API de busca.
   * 2. Envia a requisição sem validação de campo obrigatório ou tamanho.
   * 3. Mapeia o resultado aninhado do backend para os estados locais (preenchimento automático).
   */
  async function handleBuscarExterno(e) {
    e?.preventDefault();
    // Limpeza MÍNIMA do CEP, apenas para o formato esperado pelo ViaCEP (8 dígitos puros)
    const cleanCep = cep.replace(/\D/g, ''); 

    setStatus({ type: 'loading', text: 'Buscando...' });
    setEnderecoVisualizacao(null); 

    try {
      const data = await consultarExternoPorCEP(cleanCep);
      
      if (!data || !data.endereco) {
          setStatus({type:'error', text:'CEP não encontrado.'});
          return;
      }

      // Mapeamento dos dados aninhados (Adapter Pattern)
      const root = data.endereco; 
      const rua = root.logradouro?.nomeLogradouro || '';
      const bairroVal = root.bairro?.nomeBairro || '';
      const cidadeVal = root.cidade?.nomeCidade || '';
      const ufVal = root.cidade?.unidadeFederativa?.siglaUF || '';
      const tipoVal = root.logradouro?.tipoLogradouro?.nomeTipoLogradouro || '';

      // Preenche estados
      setNomeLogradouro(rua);
      setBairro(bairroVal);
      setCidade(cidadeVal);
      setUf(ufVal);
      setTipoLogradouro(tipoVal);

      // Prepara o objeto para visualização no Card
      setEnderecoVisualizacao({ ...root, logradouro: rua, bairro: bairroVal, cidade: cidadeVal, uf: ufVal, cep: root.cep || cleanCep, id: data.idEnderecoEspecifico });

      setStatus({ type: 'success', text: 'Encontrado!' });

    } catch (err) {
      console.error(err);
      // Exibe erro genérico ou a mensagem de exceção vinda do Backend
      setStatus({ type: 'error', text: err.message || 'Erro ao buscar.' });
    }
  }

  /**
   * Handler: Cadastrar Endereço.
   * * Comportamento Thin Client:
   * 1. Monta o Payload JSON aninhado (DTO) exigido pela arquitetura de N-Camadas.
   * 2. NÃO realiza validação de campos vazios ou formatos (ex: CPF, CEP, etc.).
   * 3. Delega totalmente a validação e persistência ao serviço `cadastrarEndereco`.
   */
  async function handleCadastrar(e) {
    e?.preventDefault();
    
    // Preparação do Payload JSON aninhado
    const payload = {
      numero: String(numero),
      complemento: complemento || '',
      endereco: {
        // Envia o CEP limpo para o formato esperado pelo backend
        cep: String(cep).replace(/\D/g, ''),
        cidade: {
          nomeCidade: String(cidade),
          unidadeFederativa: { siglaUF: String(uf).toUpperCase() } // Conversão simples para o formato padrão do banco
        },
        bairro: { nomeBairro: String(bairro || '') },
        logradouro: {
          nomeLogradouro: String(nomeLogradouro),
          tipoLogradouro: { nomeTipoLogradouro: String(tipoLogradouro || '') }
        }
      }
    };

    setStatus({ type: 'loading', text: 'Salvando...' });
    
    try {
      const result = await cadastrarEndereco(payload);
      setStatus({ type: 'success', text: `Cadastrado! ID: ${result?.id || 'OK'}` });
      setEnderecoVisualizacao(null); 
    } catch (err) {
      // Captura e exibe a mensagem de exceção/erro HTTP retornada pelo Backend
      setStatus({ type: 'error', text: err.message || 'Erro ao cadastrar.' });
    }
  }

  // ===========================================================================
  // COMPONENTE DE UI (Presentation)
  // ===========================================================================

  // Helper Component para mensagens de status
  const StatusMessage = () => {
    if (!status) return null;
    const colors = {
      loading: 'bg-blue-500/20 text-blue-300 border-blue-500/30',
      success: 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30',
      error: 'bg-red-500/20 text-red-300 border-red-500/30'
    };
    return (
      <div className={`mt-4 p-3 rounded border text-sm text-center ${colors[status.type]}`}>
        {status.text}
      </div>
    );
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
      
      {/* FORMULÁRIO */}
      <div className="lg:col-span-7 bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-4 flex items-center gap-2">
          Cadastro de Endereço
        </h2>

        <form onSubmit={handleCadastrar} className="space-y-4">
          
          {/* CEP */}
          <div>
            <label className="text-xs font-semibold text-slate-400 uppercase ml-1">CEP</label>
            <div className="flex gap-2 mt-1">
              <input 
                value={cep} 
                onChange={e => setCep(e.target.value)}
                placeholder="00000000"
                className="input-modern font-mono text-lg"
              />
              <button type="button" onClick={handleBuscarExterno} disabled={isLoading} className="btn-modern btn-ghost">
                Buscar
              </button>
            </div>
          </div>

          {/* Tipo e Logradouro */}
          <div className="grid grid-cols-4 gap-4">
            <div className="col-span-1">
              <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Tipo</label>
              <select value={tipoLogradouro} onChange={e => setTipoLogradouro(e.target.value)} className="input-modern mt-1 appearance-none">
                <option value="">...</option>
                {tipos.map(t => <option key={t.id||t} value={t.nomeTipoLogradouro||t}>{t.nomeTipoLogradouro||t}</option>)}
              </select>
            </div>
            <div className="col-span-3">
              <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Logradouro</label>
              <input value={nomeLogradouro} onChange={e => setNomeLogradouro(e.target.value)} className="input-modern mt-1" />
            </div>
          </div>

          {/* Número e Complemento */}
          <div className="grid grid-cols-4 gap-4">
            <div className="col-span-1">
              <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Número</label>
              <input value={numero} onChange={e => setNumero(e.target.value)} className="input-modern mt-1" />
            </div>
            <div className="col-span-3">
              <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Complemento</label>
              <input value={complemento} onChange={e => setComplemento(e.target.value)} className="input-modern mt-1" />
            </div>
          </div>

          {/* Bairro, Cidade, UF */}
          <div className="grid grid-cols-6 gap-4">
             <div className="col-span-2">
               <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Bairro</label>
               <input value={bairro} onChange={e => setBairro(e.target.value)} className="input-modern mt-1" />
             </div>
             <div className="col-span-3">
               <label className="text-xs font-semibold text-slate-400 uppercase ml-1">Cidade</label>
               <input value={cidade} onChange={e => setCidade(e.target.value)} className="input-modern mt-1" />
             </div>
             <div className="col-span-1">
               <label className="text-xs font-semibold text-slate-400 uppercase ml-1">UF</label>
               <select value={uf} onChange={e => setUf(e.target.value)} className="input-modern mt-1 appearance-none">
                 <option value=""></option>
                 {ufs.map(u => <option key={u.id||u} value={u.siglaUF||u}>{u.siglaUF||u}</option>)}
               </select>
             </div>
          </div>

          <div className="pt-4">
            <button type="submit" disabled={isLoading} className="btn-modern btn-primary w-full">
              Confirmar Cadastro
            </button>
            <StatusMessage />
          </div>
        </form>
      </div>

      {/* PREVIEW */}
      <div className="lg:col-span-5 bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg min-h-[200px]">
        <h3 className="text-xs font-bold text-slate-500 uppercase mb-4">Preview da Busca</h3>
        {enderecoVisualizacao ? (
          <CardEndereco endereco={enderecoVisualizacao} />
        ) : (
          <div className="text-slate-600 text-center py-10 text-sm">Aguardando busca...</div>
        )}
      </div>
    </div>
  );
}