import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Layers, Plus, Save, Loader2 } from 'lucide-react';
import { SistemaService } from '../services/api';

export default function TiposServicoPage() {
  const [tipos, setTipos] = useState([]);
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, reset } = useForm();

  const carregarTipos = async () => {
    try {
      const dados = await SistemaService.listarTiposServico();
      setTipos(dados);
    } catch (error) { console.error(error); }
  };

  useEffect(() => { carregarTipos(); }, []);

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      await SistemaService.cadastrarTipoServico({ nomeTipoServico: data.nome });
      alert("Serviço cadastrado!");
      reset();
      carregarTipos();
    } catch (e) { alert("Erro ao cadastrar."); } 
    finally { setLoading(false); }
  };

  return (
    <div className="p-6 space-y-8 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
        <Layers /> Catálogo de Serviços
      </h1>

      <div className="bg-white p-6 rounded-lg shadow border border-slate-200">
        <h2 className="font-bold text-lg mb-4 text-slate-700">Novo Tipo de Serviço</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="flex gap-4">
          <input 
            {...register('nome', { required: true })}
            placeholder="Ex: Formatação, Limpeza de Cooler..." 
            className="flex-1 border p-2 rounded outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button type="submit" disabled={loading} className="bg-blue-600 text-white px-6 rounded font-medium hover:bg-blue-700 disabled:opacity-50 flex items-center gap-2">
            {loading ? <Loader2 className="animate-spin"/> : <Plus />} Adicionar
          </button>
        </form>
      </div>

      <div className="bg-white rounded-lg shadow border border-slate-200 overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-100 text-slate-600">
            <tr><th className="p-4">ID</th><th className="p-4">Descrição do Serviço</th></tr>
          </thead>
          <tbody>
            {tipos.map(t => (
              <tr key={t.idTipoServico} className="border-t hover:bg-slate-50">
                <td className="p-4 text-slate-500">#{t.idTipoServico}</td>
                <td className="p-4 font-medium">{t.nomeTipoServico}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}