import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Package, Plus, Save, Loader2 } from 'lucide-react';
import { SistemaService } from '../services/api';

export default function EstoquePage() {
  const [tipos, setTipos] = useState([]);
  const [equipamentos, setEquipamentos] = useState([]);
  const [loading, setLoading] = useState(false);
  
  const { register: regTipo, handleSubmit: subTipo, reset: resetTipo } = useForm();
  const { register: regEq, handleSubmit: subEq, reset: resetEq } = useForm();

  const carregarDados = async () => {
    try {
      const [listaTipos, listaEq] = await Promise.all([
        SistemaService.listarTipos(),
        SistemaService.listarEquipamentos()
      ]);
      setTipos(listaTipos);
      setEquipamentos(listaEq);
    } catch (error) { 
      console.error("Erro ao carregar dados", error); 
    }
  };

  useEffect(() => { carregarDados(); }, []);

  const onSalvarTipo = async (data) => {
    if (!data.nome) return;
    setLoading(true);
    try {
      const payload = {
        nomeTipoEquipamento: data.nome
      };
      await SistemaService.cadastrarTipo(payload);
      resetTipo();
      await carregarDados();
      alert("Tipo cadastrado com sucesso!");
    } catch (e) { 
      console.error(e);
      alert("Erro ao salvar tipo."); 
    } finally {
      setLoading(false);
    }
  };

  const onSalvarEquipamento = async (data) => {
    setLoading(true);
    try {
      const payload = {
        nomeEquipamento: data.nome,
        valorDiaria: parseFloat(data.valorDiaria),
        tipoEquipamento: {
          idTipoEquipamento: parseInt(data.idTipo)
        }
      };
      
      await SistemaService.cadastrarEquipamento(payload);
      resetEq();
      await carregarDados();
      alert("Equipamento cadastrado com sucesso!");
    } catch (e) { 
      console.error(e);
      alert("Erro ao salvar equipamento."); 
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 space-y-8">
      <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
        <Package /> Gestão de Estoque
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow border border-slate-200 h-fit">
          <h2 className="font-bold text-lg mb-4 text-slate-700">Novo Tipo</h2>
          <form onSubmit={subTipo(onSalvarTipo)} className="flex gap-2">
            <input 
              {...regTipo('nome', { required: true })}
              placeholder="Ex: Ferramentas" 
              className="flex-1 border p-2 rounded focus:ring-2 ring-blue-500 outline-none"
            />
            <button 
              type="submit" 
              disabled={loading}
              className="bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? <Loader2 className="animate-spin h-5 w-5"/> : <Plus />}
            </button>
          </form>
        </div>

        <div className="md:col-span-2 bg-white p-6 rounded-lg shadow border border-slate-200">
          <h2 className="font-bold text-lg mb-4 text-slate-700">Novo Equipamento</h2>
          <form onSubmit={subEq(onSalvarEquipamento)} className="grid grid-cols-1 md:grid-cols-3 gap-4">
            
            <input 
              {...regEq('nome', { required: true })}
              placeholder="Nome/Modelo (Ex: Betoneira)" 
              className="border p-2 rounded focus:ring-2 ring-blue-500 outline-none"
            />
            
            <input 
              {...regEq('valorDiaria', { required: true })}
              type="number" step="0.01" placeholder="Valor Diária (R$)" 
              className="border p-2 rounded focus:ring-2 ring-blue-500 outline-none"
            />
            
            <select 
              {...regEq('idTipo', { required: true })}
              className="border p-2 rounded focus:ring-2 ring-blue-500 outline-none bg-white"
            >
              <option value="">Selecione o Tipo...</option>
              {tipos.map(t => (
                <option key={t.idTipoEquipamento} value={t.idTipoEquipamento}>
                  {t.nomeTipoEquipamento}
                </option>
              ))}
            </select>

            <button 
              type="submit" 
              disabled={loading}
              className="md:col-span-3 bg-green-600 text-white py-2 rounded font-medium hover:bg-green-700 flex justify-center gap-2 disabled:opacity-50"
            >
              {loading ? <Loader2 className="animate-spin"/> : <Save size={20} />} 
              Salvar Equipamento
            </button>
          </form>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow border border-slate-200 overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-slate-100 text-slate-600">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Equipamento</th>
              <th className="p-4">Tipo</th>
              <th className="p-4 text-right">Valor Diária</th>
            </tr>
          </thead>
          <tbody>
            {equipamentos.length > 0 ? (
              equipamentos.map(eq => (
                <tr key={eq.idEquipamento} className="border-t border-slate-100 hover:bg-slate-50">
                  <td className="p-4 text-slate-500">#{eq.idEquipamento}</td>
                  <td className="p-4 font-medium text-slate-800">{eq.nomeEquipamento}</td>
                  <td className="p-4">
                    <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full font-semibold">
                      {eq.tipoEquipamento?.nomeTipoEquipamento || 'Sem Tipo'}
                    </span>
                  </td>
                  <td className="p-4 text-right text-green-600 font-bold">
                    R$ {eq.valorDiaria?.toFixed(2)}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="p-8 text-center text-slate-500">
                  Nenhum equipamento cadastrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}