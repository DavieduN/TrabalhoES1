import React, { useEffect, useState } from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { User, Search, Save, Loader2, MapPin, Phone, Mail, Plus, Trash2, AlertCircle, XCircle } from 'lucide-react';
import { SistemaService } from '../services/api';

export default function AtendentesPage() {
  const { register, control, handleSubmit, setValue, getValues, reset, setFocus } = useForm({
    defaultValues: {
      telefones: [{ ddi: '55', ddd: '', numero: '' }],
      emails: [{ enderecoEmail: '' }]
    }
  });

  const { fields: fieldsTel, append: appendTel, remove: removeTel } = useFieldArray({
    control,
    name: "telefones"
  });

  const { fields: fieldsEmail, append: appendEmail, remove: removeEmail } = useFieldArray({
    control,
    name: "emails"
  });
  
  const [ufs, setUfs] = useState([]);
  const [tiposLogradouro, setTiposLogradouro] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingCep, setLoadingCep] = useState(false);
  const [feedback, setFeedback] = useState({ type: '', message: '' });

  const formatError = (error) => {
    if (error.response) {
      const data = error.response.data;
      if (data && data.erro) return data.erro;
      if (data && data.message) return data.message;
      if (typeof data === 'string') return data;
    }
    return "Erro de conexão ou erro desconhecido. Verifique o console (F12).";
  };

  useEffect(() => {
    async function carregarDominios() {
      try {
        const [listaUfs, listaTipos] = await Promise.all([
          SistemaService.listarUfs(),
          SistemaService.listarTiposLogradouro()
        ]);
        setUfs(listaUfs);
        setTiposLogradouro(listaTipos);
      } catch (error) {
        console.error("Erro domínios", error);
        setFeedback({ type: 'error', message: "Falha ao carregar UFs e Tipos de Logradouro." });
      }
    }
    carregarDominios();
  }, []);

  const handleBuscaCep = async () => {
    const cep = getValues('cep');
    if (!cep || cep.length < 8) return;

    setLoadingCep(true);
    setFeedback({ type: '', message: '' });
    
    try {
      const dados = await SistemaService.buscarCep(cep);
      
      if (!dados || dados.erro) {
        setFeedback({ type: 'error', message: "CEP não encontrado na base externa." });
        return;
      }

      if (dados.logradouro) {
        setValue('logradouro', dados.logradouro.nomeLogradouro);
        if (dados.logradouro.tipoLogradouro) {
           setValue('tipoLogradouro', dados.logradouro.tipoLogradouro.nomeTipoLogradouro);
        }
      }
      if (dados.bairro) setValue('bairro', dados.bairro.nomeBairro);
      if (dados.cidade) {
        setValue('cidade', dados.cidade.nomeCidade);
        if (dados.cidade.unidadeFederativa) {
           setValue('uf', dados.cidade.unidadeFederativa.siglaUF);
        }
      }
      setFocus('numero');

    } catch (error) {
      console.error(error);
      setFeedback({ type: 'error', message: "Erro ao buscar CEP. Tente manualmente." });
    } finally {
      setLoadingCep(false);
    }
  };

  const onSubmit = async (data) => {
    setLoading(true);
    setFeedback({ type: '', message: '' });

    try {
      const payload = {
        idAtendente: 0,
        nome: data.nome,
        cpf: data.cpf,
        numero: data.numero,
        complemento: data.complemento,
        endereco: {
          cep: data.cep,
          cidade: {
            nomeCidade: data.cidade,
            unidadeFederativa: { siglaUF: data.uf }
          },
          bairro: { nomeBairro: data.bairro },
          logradouro: {
            nomeLogradouro: data.logradouro,
            tipoLogradouro: { nomeTipoLogradouro: data.tipoLogradouro }
          }
        },
        telefones: data.telefones.map(tel => ({
          numero: tel.numero,
          ddd: { ddd: parseInt(tel.ddd) },
          ddi: { ddi: parseInt(tel.ddi) }
        })),
        emails: data.emails.map(email => ({
          enderecoEmail: email.enderecoEmail
        }))
      };

      await SistemaService.cadastrarAtendente(payload);
      
      setFeedback({ type: 'success', message: "Atendente cadastrado com sucesso!" });
      reset();
    } catch (error) {
      console.error("Erro no submit:", error);
      setFeedback({ type: 'error', message: formatError(error) });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-5xl mx-auto">
      <h1 className="text-2xl font-bold mb-6 flex items-center gap-2 text-slate-800">
        <User /> Cadastro de Atendente
      </h1>
      
      {feedback.message && (
        <div className={`p-4 mb-6 rounded-lg border flex items-start gap-3 ${
          feedback.type === 'success' ? 'bg-green-50 text-green-800 border-green-200' : 'bg-red-50 text-red-800 border-red-200'
        }`}>
          {feedback.type === 'success' ? <User className="h-5 w-5"/> : <AlertCircle className="h-5 w-5 shrink-0"/>}
          <div className="flex-1">
            <p className="font-bold text-sm">{feedback.type === 'success' ? 'Sucesso!' : 'Erro ao Processar'}</p>
            <p className="text-sm">{feedback.message}</p>
          </div>
          <button onClick={() => setFeedback({type:'', message:''})} className="opacity-50 hover:opacity-100">
            <XCircle className="h-5 w-5" />
          </button>
        </div>
      )}

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
        
        <div className="bg-white p-6 rounded-lg shadow-sm border border-slate-200">
          <h2 className="text-sm font-bold text-slate-500 uppercase tracking-wider mb-4">Dados Pessoais</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Nome Completo</label>
              <input {...register('nome', { required: true })} className="input-padrao w-full border p-2 rounded" />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">CPF</label>
              <input {...register('cpf', { required: true })} className="input-padrao w-full border p-2 rounded" placeholder="000.000.000-00"/>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-sm border border-slate-200">
          <h2 className="text-sm font-bold text-slate-500 uppercase tracking-wider mb-4 flex items-center gap-2">
            <Phone size={16}/> Contatos
          </h2>
          
          <div className="mb-6">
            <label className="block text-xs font-bold text-slate-400 mb-2">TELEFONES</label>
            {fieldsTel.map((item, index) => (
              <div key={item.id} className="flex gap-2 mb-2 items-end">
                <div className="w-20">
                  <label className="text-xs text-slate-500">DDI</label>
                  <input 
                    {...register(`telefones.${index}.ddi`, { required: true })} 
                    placeholder="55"
                    className="w-full border p-2 rounded text-center"
                  />
                </div>
                <div className="w-20">
                  <label className="text-xs text-slate-500">DDD</label>
                  <input 
                    {...register(`telefones.${index}.ddd`, { required: true })} 
                    placeholder="45"
                    className="w-full border p-2 rounded text-center"
                  />
                </div>
                <div className="flex-1">
                  <label className="text-xs text-slate-500">Número</label>
                  <input 
                    {...register(`telefones.${index}.numero`, { required: true })} 
                    placeholder="99999-9999"
                    className="w-full border p-2 rounded"
                  />
                </div>
                <button 
                  type="button" 
                  onClick={() => removeTel(index)}
                  className="p-2 text-red-500 hover:bg-red-50 rounded"
                  title="Remover telefone"
                >
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <button 
              type="button" 
              onClick={() => appendTel({ ddi: '55', ddd: '', numero: '' })}
              className="text-sm text-blue-600 hover:text-blue-800 flex items-center gap-1 mt-2"
            >
              <Plus size={16} /> Adicionar Telefone
            </button>
          </div>

          <hr className="border-slate-100 my-4"/>

          <div>
            <label className="block text-xs font-bold text-slate-400 mb-2">E-MAILS</label>
            {fieldsEmail.map((item, index) => (
              <div key={item.id} className="flex gap-2 mb-2">
                <div className="flex-1">
                  <input 
                    {...register(`emails.${index}.enderecoEmail`, { required: true })} 
                    placeholder="exemplo@email.com"
                    type="email"
                    className="w-full border p-2 rounded"
                  />
                </div>
                <button 
                  type="button" 
                  onClick={() => removeEmail(index)}
                  className="p-2 text-red-500 hover:bg-red-50 rounded"
                >
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            <button 
              type="button" 
              onClick={() => appendEmail({ enderecoEmail: '' })}
              className="text-sm text-blue-600 hover:text-blue-800 flex items-center gap-1 mt-2"
            >
              <Plus size={16} /> Adicionar Email
            </button>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-sm border border-slate-200">
          <h2 className="text-sm font-bold text-slate-500 uppercase tracking-wider mb-4 flex items-center gap-2">
            <MapPin size={16}/> Endereço
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
             <div className="relative">
                <label className="block text-sm font-medium text-slate-700 mb-1">CEP</label>
                <div className="relative">
                    <input 
                      {...register('cep', { required: true })} 
                      onBlur={handleBuscaCep}
                      placeholder="00000000"
                      className="w-full border p-2 pl-3 pr-10 rounded focus:ring-2 focus:ring-blue-500 outline-none" 
                    />
                    {loadingCep ? (
                      <Loader2 className="absolute right-3 top-3 h-4 w-4 animate-spin text-blue-600" />
                    ) : (
                      <Search className="absolute right-3 top-3 h-4 w-4 text-slate-400" />
                    )}
                </div>
             </div>
             <div className="md:col-span-3 flex items-end pb-2">
                <span className="text-xs text-slate-500">TAB para buscar automático.</span>
             </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
             <div className="md:col-span-1">
                <label className="block text-sm font-medium text-slate-700 mb-1">Tipo</label>
                <select {...register('tipoLogradouro', { required: true })} className="w-full border p-2 rounded bg-white">
                  <option value="">...</option>
                  {tiposLogradouro.map(t => (
                    <option key={t.idTipoLogradouro || t.nomeTipoLogradouro} value={t.nomeTipoLogradouro}>{t.nomeTipoLogradouro}</option>
                  ))}
                </select>
             </div>
             <div className="md:col-span-3">
                <label className="block text-sm font-medium text-slate-700 mb-1">Logradouro</label>
                <input {...register('logradouro', { required: true })} className="w-full border p-2 rounded" />
             </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Número</label>
                <input {...register('numero', { required: true })} className="w-full border p-2 rounded" />
             </div>
             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Complemento</label>
                <input {...register('complemento')} className="w-full border p-2 rounded" />
             </div>
             <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">Bairro</label>
                <input {...register('bairro', { required: true })} className="w-full border p-2 rounded" />
             </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
             <div className="md:col-span-3">
                <label className="block text-sm font-medium text-slate-700 mb-1">Cidade</label>
                <input {...register('cidade', { required: true })} className="w-full border p-2 rounded" />
             </div>
             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">UF</label>
                <select {...register('uf', { required: true })} className="w-full border p-2 rounded bg-white">
                  <option value="">UF</option>
                  {ufs.map(uf => (
                    <option key={uf.siglaUF} value={uf.siglaUF}>{uf.siglaUF}</option>
                  ))}
                </select>
             </div>
          </div>
        </div>

        <div className="flex justify-end">
          <button 
            type="submit" 
            disabled={loading}
            className="bg-green-600 text-white px-8 py-3 rounded-lg font-bold hover:bg-green-700 flex items-center gap-2 disabled:opacity-50"
          >
            {loading ? <Loader2 className="animate-spin"/> : <Save />} 
            Cadastrar Atendente
          </button>
        </div>

      </form>
    </div>
  );
}