import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Search, Save, Loader2, ArrowRight, Download, AlertCircle } from 'lucide-react';
import { EnderecoService } from '../../services/api';
import { AddressCard } from '../display/AddressCard';

export function AddressForm() {
  const { register, handleSubmit, setValue, getValues, formState: { errors } } = useForm();

  const [ufs, setUfs] = useState([]);
  const [tiposLogradouro, setTiposLogradouro] = useState([]);
  const [internalMatches, setInternalMatches] = useState([]);

  const [loadingCep, setLoadingCep] = useState(false);
  const [loadingId, setLoadingId] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState({ type: '', message: '' });
  const [searchId, setSearchId] = useState('');

  const formatError = (error) => {
      if (error.response) {
          const data = error.response.data;

          if (typeof data === 'string') {
              try {
                  const parsed = JSON.parse(data);
                  if (parsed.erro) return parsed.erro;
              } catch {
                  return data;
              }
          }

          if (typeof data === 'object') {
              if (data.erro) return data.erro;
              if (data.message) return data.message;
              if (data.error) return data.error;
          }
      }
  }

  useEffect(() => {
    async function loadDomains() {
      try {
        const [listaUfs, listaTipos] = await Promise.all([
          EnderecoService.getUfs(),
          EnderecoService.getTiposLogradouro()
        ]);
        setUfs(listaUfs);
        setTiposLogradouro(listaTipos);
      } catch (error) {
        console.error("Erro domínios", error);
        setFeedback({ type: 'error', message: 'Não foi possível carregar as listas de UF e Tipos.' });
      }
    }
    loadDomains();
  }, []);

  const preencherFormulario = (dados) => {
    if (!dados) return;
    setValue('cep', dados.cep || '');
    setValue('logradouro', dados.logradouro?.nomeLogradouro || '');
    setValue('tipoLogradouro', dados.logradouro?.tipoLogradouro?.nomeTipoLogradouro || '');
    setValue('bairro', dados.bairro?.nomeBairro || '');
    setValue('cidade', dados.cidade?.nomeCidade || '');
    setValue('uf', dados.cidade?.unidadeFederativa?.siglaUF || '');
  };

  const handleBuscarId = async (e) => {
    e.preventDefault();
    if (!searchId) return;

    setLoadingId(true);
    setFeedback({ type: '', message: '' });

    try {
      const dados = await EnderecoService.buscarEnderecoPorId(searchId);
      if (dados) {
        preencherFormulario(dados);
        setFeedback({ type: 'success', message: 'Endereço carregado com sucesso!' });
      } else {
        setFeedback({ type: 'error', message: 'Endereço não encontrado com este ID.' });
      }
    } catch (error) {
      console.error(error);
      setFeedback({ type: 'error', message: formatError(error) });
    } finally {
      setLoadingId(false);
    }
  };

  const handleBuscaCep = async () => {
    const cep = getValues('cep');
    if (!cep || cep.length < 8) return;

    setLoadingCep(true);
    setInternalMatches([]);
    setFeedback({ type: '', message: '' });

    try {
      const [externo, interno] = await Promise.allSettled([
        EnderecoService.consultarCepExterno(cep),
        EnderecoService.consultarCepInterno(cep)
      ]);

      if (externo.status === 'fulfilled' && externo.value && !externo.value.erro) {
         preencherFormulario(externo.value);
      }

      if (interno.status === 'fulfilled' && Array.isArray(interno.value) && interno.value.length > 0) {
        setInternalMatches(interno.value);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoadingCep(false);
    }
  };

  const handleSelectMatch = (endereco) => {
    if(!window.confirm("Deseja carregar os dados deste endereço?")) return;
    preencherFormulario(endereco);
  };

  const onSubmit = async (data) => {
    setSubmitting(true);
    setFeedback({ type: '', message: '' });

    const payload = {
      cep: data.cep,
      cidade: {
        nomeCidade: data.cidade,
        unidadeFederativa: { siglaUF: data.uf }
      },
      bairro: {
        nomeBairro: data.bairro
      },
      logradouro: {
        nomeLogradouro: data.logradouro,
        tipoLogradouro: { nomeTipoLogradouro: data.tipoLogradouro }
      }
    };

    try {
      const resposta = await EnderecoService.cadastrarEndereco(payload);
      setFeedback({ type: 'success', message: `Endereço salvo/atualizado! ID: ${resposta.idEndereco}` });
      setInternalMatches([]);
    } catch (error) {
      setFeedback({ type: 'error', message: formatError(error) });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-start">

      <div className="lg:col-span-2 bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">

        <div className="bg-slate-50 border-b border-slate-200 p-4 flex items-center justify-between">
            <h2 className="text-lg font-bold text-slate-700 flex items-center gap-2">
                <Save className="h-5 w-5 text-blue-600" />
                Dados do Endereço
            </h2>

            <form onSubmit={handleBuscarId} className="flex gap-2 items-center">
                <label className="text-xs font-semibold text-slate-500 uppercase">Carregar ID:</label>
                <input
                    type="number"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    placeholder="#"
                    className="w-20 px-2 py-1 text-sm border border-slate-300 rounded focus:outline-none focus:border-blue-500"
                />
                <button
                    type="submit"
                    disabled={loadingId || !searchId}
                    className="bg-white border border-slate-300 p-1.5 rounded hover:bg-slate-100 text-slate-600 disabled:opacity-50"
                    title="Carregar endereço por ID"
                >
                    {loadingId ? <Loader2 className="h-4 w-4 animate-spin" /> : <Download className="h-4 w-4" />}
                </button>
            </form>
        </div>

        <div className="p-6">
            {feedback.message && (
            <div className={`p-4 mb-6 rounded-lg text-sm border flex items-start gap-2 ${feedback.type === 'success' ? 'bg-green-50 text-green-700 border-green-200' : 'bg-red-50 text-red-700 border-red-200'}`}>
                {feedback.type === 'error' && <AlertCircle className="h-5 w-5 shrink-0" />}
                <span>{feedback.message}</span>
            </div>
            )}

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

            <div className="flex gap-4 items-end">
                <div className="w-1/3">
                <label className="block text-sm font-medium text-slate-700 mb-1">CEP</label>
                <div className="relative">
                    <input
                    {...register('cep', { required: true })}
                    onBlur={handleBuscaCep}
                    placeholder="00000000"
                    className="w-full pl-3 pr-10 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                    />
                    {loadingCep && (
                    <div className="absolute right-3 top-2.5">
                        <Loader2 className="h-4 w-4 animate-spin text-blue-600" />
                    </div>
                    )}
                </div>
                </div>
                <div className="flex-1 pb-2">
                <span className="text-xs text-slate-400">
                    Digite o CEP e aperte Tab para buscar.
                </span>
                </div>
            </div>

            <hr className="border-slate-100 my-4" />

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="md:col-span-1">
                <label className="block text-sm font-medium text-slate-700 mb-1">Tipo</label>
                <select
                    {...register('tipoLogradouro', { required: true })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none bg-white"
                >
                    <option value="">...</option>
                    {tiposLogradouro.map(t => (
                    <option key={t.idTipoLogradouro || t.nomeTipoLogradouro} value={t.nomeTipoLogradouro}>
                        {t.nomeTipoLogradouro}
                    </option>
                    ))}
                </select>
                </div>
                <div className="md:col-span-3">
                <label className="block text-sm font-medium text-slate-700 mb-1">Logradouro</label>
                <input
                    {...register('logradouro', { required: true })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                </div>
            </div>

            <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Bairro</label>
                <input
                {...register('bairro', { required: true })}
                className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="md:col-span-3">
                <label className="block text-sm font-medium text-slate-700 mb-1">Cidade</label>
                <input
                    {...register('cidade', { required: true })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                </div>
                <div className="md:col-span-1">
                <label className="block text-sm font-medium text-slate-700 mb-1">UF</label>
                <select
                    {...register('uf', { required: true })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none bg-white"
                >
                    <option value="">UF</option>
                    {ufs.map(uf => (
                    <option key={uf.siglaUF} value={uf.siglaUF}>{uf.siglaUF}</option>
                    ))}
                </select>
                </div>
            </div>

            <div className="pt-4 flex justify-end">
                <button
                type="submit"
                disabled={submitting}
                className="bg-blue-600 text-white px-6 py-2 rounded-md hover:bg-blue-700 font-medium transition-colors disabled:opacity-50 flex items-center gap-2"
                >
                {submitting && <Loader2 className="animate-spin h-4 w-4" />}
                {submitting ? 'Salvando...' : 'Cadastrar Endereço'}
                </button>
            </div>

            </form>
        </div>
      </div>

      <div className="lg:col-span-1 space-y-4">
        {internalMatches.length > 0 ? (
          <div className="bg-slate-50 border border-blue-200 rounded-xl p-4 animate-in fade-in slide-in-from-right-4 duration-500">
            <div className="flex items-center gap-2 text-blue-800 font-bold mb-3 text-sm uppercase tracking-wide">
              <Search className="h-4 w-4" />
              Encontrados no Banco
            </div>
            <p className="text-xs text-slate-600 mb-4">
              Estes endereços já existem para o CEP informado. Clique para reutilizar.
            </p>

            <div className="flex flex-col gap-3">
              {internalMatches.map((match, idx) => (
                <div key={idx} className="relative group">
                  <AddressCard
                    data={match}
                    onClick={handleSelectMatch}
                  />
                  <div className="absolute right-2 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity">
                     <ArrowRight className="text-blue-500 h-5 w-5" />
                  </div>
                </div>
              ))}
            </div>
          </div>
        ) : (
          !loadingCep && (
            <div className="border border-dashed border-slate-300 rounded-xl p-8 text-center text-slate-400">
              <Search className="h-8 w-8 mx-auto mb-2 opacity-50" />
              <p className="text-sm">Digite um CEP para verificar a existência ou use o ID no topo.</p>
            </div>
          )
        )}
      </div>

    </div>
  );
}