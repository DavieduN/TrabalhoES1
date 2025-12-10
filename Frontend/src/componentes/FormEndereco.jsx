import React, { useEffect, useState, useMemo } from 'react';
import CardEndereco from './CardEndereco'; 
import { listarUFs, listarTiposLogradouro, consultarExternoPorCEP, cadastrarEndereco } from '../api/enderecoApi';

export default function FormEndereco() {

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

  const [status, setStatus] = useState(null);
  const isLoading = status?.type === 'loading';
  const [enderecoVisualizacao, setEnderecoVisualizacao] = useState(null);

  useEffect(() => {
    listarUFs().then(setUfs).catch(() => setUfs([]));
    listarTiposLogradouro().then(setTipos).catch(() => setTipos([]));
  }, []);


  async function handleBuscarExterno(e) {
    e?.preventDefault();
    const cleanCep = cep.replace(/\D/g, ''); 

    setStatus({ type: 'loading', text: 'Buscando...' });
    setEnderecoVisualizacao(null); 

    try {
      const data = await consultarExternoPorCEP(cleanCep);
      
      if (!data || !data.endereco) {
          setStatus({type:'error', text:'CEP não encontrado.'});
          return;
      }

      const root = data.endereco; 
      const rua = root.logradouro?.nomeLogradouro || '';
      const bairroVal = root.bairro?.nomeBairro || '';
      const cidadeVal = root.cidade?.nomeCidade || '';
      const ufVal = root.cidade?.unidadeFederativa?.siglaUF || '';
      const tipoVal = root.logradouro?.tipoLogradouro?.nomeTipoLogradouro || '';

      setNomeLogradouro(rua);
      setBairro(bairroVal);
      setCidade(cidadeVal);
      setUf(ufVal);
      setTipoLogradouro(tipoVal);

      setEnderecoVisualizacao({ ...root, logradouro: rua, bairro: bairroVal, cidade: cidadeVal, uf: ufVal, cep: root.cep || cleanCep, id: data.idEnderecoEspecifico });

      setStatus({ type: 'success', text: 'Encontrado!' });

    } catch (err) {
      console.error(err);
      setStatus({ type: 'error', text: err.message || 'Erro ao buscar.' });
    }
  }


  async function handleCadastrar(e) {
    e?.preventDefault();
    
    const payload = {
      numero: String(numero),
      complemento: complemento || '',
      endereco: {
        cep: String(cep).replace(/\D/g, ''),
        cidade: {
          nomeCidade: String(cidade),
          unidadeFederativa: { siglaUF: String(uf).toUpperCase() } 
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
      setStatus({ type: 'error', text: err.message || 'Erro ao cadastrar.' });
    }
  }

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
      
      <div className="lg:col-span-7 bg-slate-800 rounded-lg p-6 border border-slate-700 shadow-lg">
        <h2 className="text-lg font-bold text-white mb-4 flex items-center gap-2">
          Cadastro de Endereço
        </h2>

        <form onSubmit={handleCadastrar} className="space-y-4">
          
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