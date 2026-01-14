import React, { useEffect, useState } from 'react';
import { Calendar, DollarSign, Search, Loader2, AlertCircle, CheckCircle, XCircle, User, ClipboardList, RefreshCcw, MapPin, Phone, Mail, Box, X } from 'lucide-react';
import { SistemaService } from '../services/api';

export default function AluguelPage() {

  const [equipamentos, setEquipamentos] = useState([]);
  const [alugueis, setAlugueis] = useState([]);
  const [cliente, setCliente] = useState(null);

  const [aluguelDetalhado, setAluguelDetalhado] = useState(null);
  const [modalAberto, setModalAberto] = useState(false);
  
  const [tipoBusca, setTipoBusca] = useState('ID');
  const [valorBusca, setValorBusca] = useState('');
  const [buscaNro, setBuscaNro] = useState('');

  const [idEquipamento, setIdEquipamento] = useState('');
  const [dataRet, setDataRet] = useState('');
  const [dataDev, setDataDev] = useState('');
  const [total, setTotal] = useState(0);
  
  const [loading, setLoading] = useState(false);
  const [loadingBusca, setLoadingBusca] = useState(false);
  const [loadingTabela, setLoadingTabela] = useState(false);
  const [feedback, setFeedback] = useState({ type: '', message: '' });

  useEffect(() => {
    carregarEquipamentos();
    carregarAlugueis();
  }, []);

  async function carregarEquipamentos() {
    try {
      const dados = await SistemaService.listarEquipamentos();
      setEquipamentos(dados);
    } catch (error) {
      console.error("Erro equipamentos", error);
      setFeedback({ type: 'error', message: "Não foi possível carregar a lista de equipamentos." });
    }
  }

  async function carregarAlugueis() {
    setLoadingTabela(true);
    try {
      const dados = await SistemaService.listarAlugueis();
      setAlugueis(Array.isArray(dados) ? dados : []);
    } catch (error) {
      console.error("Erro alugueis", error);
    } finally {
      setLoadingTabela(false);
    }
  }

  useEffect(() => {
    if (idEquipamento && dataRet && dataDev) {
      const eq = equipamentos.find(e => e.idEquipamento == idEquipamento);
      const inicio = new Date(dataRet);
      const fim = new Date(dataDev);
      const diffTime = fim - inicio;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 

      if (eq && diffDays > 0) {
        setTotal(diffDays * eq.valorDiaria);
      } else {
        setTotal(0);
      }
    } else {
      setTotal(0);
    }
  }, [idEquipamento, dataRet, dataDev, equipamentos]);

  const buscarCliente = async (e) => {
    e.preventDefault();
    if (!valorBusca) return;
    
    setLoadingBusca(true);
    setFeedback({ type: '', message: '' });
    setCliente(null);
    
    try {
      let resultado = null;

      if (tipoBusca === 'ID') {
        resultado = await SistemaService.buscarClientePorId(valorBusca);
      } else {
        const cpfLimpo = valorBusca.replace(/\D/g, '');
        resultado = await SistemaService.buscarClientePorCpf(cpfLimpo);
      }

      if (resultado && resultado.idPessoa) {
        setCliente(resultado);
      } else {
        setFeedback({ type: 'error', message: "Cliente não encontrado." });
      }
    } catch (error) {
      console.error(error);
      setFeedback({ type: 'error', message: "Erro ao buscar cliente (Verifique ID ou CPF)." });
    } finally {
      setLoadingBusca(false);
    }
  };

  const registrar = async () => {
    setLoading(true);
    setFeedback({ type: '', message: '' });

    try {
      const payload = {
        dataLocacao: dataRet,
        dataDevolucao: dataDev,
        valorTotalLocacao: total,
        cliente: { idPessoa: cliente.idPessoa },
        equipamento: { idEquipamento: parseInt(idEquipamento) }
      };

      console.log("Payload Envio:", payload);

      await SistemaService.registrarAluguel(payload);
      
      setFeedback({ type: 'success', message: "Aluguel registrado com sucesso!" });

      limparFormulario();
      carregarAlugueis();

    } catch (error) {
      console.error(error);
      let msg = "Erro ao registrar aluguel.";
      if (error.response?.data?.erro) msg = error.response.data.erro;
      setFeedback({ type: 'error', message: msg });
    } finally {
      setLoading(false);
    }
  };

  const limparFormulario = () => {
    setCliente(null);
    setValorBusca('');
    setIdEquipamento('');
    setTotal(0);
    setDataRet('');
    setDataDev('');
  };

  const handleBuscarAluguel = async (e) => {
    e.preventDefault();
    
    if (!buscaNro) {
      carregarAlugueis();
      return;
    }

    setLoadingTabela(true);
    try {
      const resultado = await SistemaService.buscarAluguelPorNumero(buscaNro);
      
      if (resultado) {
        setAluguelDetalhado(resultado);
        setModalAberto(true);
      } else {
        alert("Nenhum aluguel encontrado com este número.");
      }
    } catch (error) {
      console.error(error);
      alert("Erro na busca de aluguel.");
    } finally {
      setLoadingTabela(false);
    }
  };

  return (
    <div className="p-6 max-w-5xl mx-auto space-y-10">
      
      <section>
        <h1 className="text-2xl font-bold mb-6 flex items-center gap-2 text-slate-800">
          <Calendar /> Registrar Aluguel
        </h1>
        {feedback.message && (
          <div className={`p-4 mb-6 rounded-lg border flex items-start gap-3 ${feedback.type === 'success' ? 'bg-green-50 text-green-800 border-green-200' : 'bg-red-50 text-red-800 border-red-200'}`}>
            {feedback.type === 'success' ? <CheckCircle className="h-5 w-5"/> : <AlertCircle className="h-5 w-5"/>}
            <div className="flex-1"><p className="text-sm font-medium">{feedback.message}</p></div>
            <button onClick={() => setFeedback({type:'', message:''})}><XCircle className="h-5 w-5 opacity-50"/></button>
          </div>
        )}
        <div className="bg-white p-6 rounded-lg shadow border border-slate-200 grid grid-cols-1 md:grid-cols-2 gap-8">
          <div className="space-y-6">
            <div className="bg-slate-50 p-4 rounded border border-slate-200">
              <label className="block text-sm font-bold text-slate-700 mb-3 flex items-center gap-2"><User size={16}/> 1. Selecionar Cliente</label>
              <div className="flex gap-2 mb-2 text-xs font-bold text-slate-500">
                 <button onClick={() => { setTipoBusca('ID'); setValorBusca(''); }} className={`px-3 py-1 rounded-full border transition-colors ${tipoBusca === 'ID' ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-100'}`}>Por ID</button>
                 <button onClick={() => { setTipoBusca('CPF'); setValorBusca(''); }} className={`px-3 py-1 rounded-full border transition-colors ${tipoBusca === 'CPF' ? 'bg-blue-600 text-white border-blue-600' : 'bg-white hover:bg-slate-100'}`}>Por CPF</button>
              </div>
              <form onSubmit={buscarCliente} className="flex gap-2">
                <input type={tipoBusca === 'ID' ? 'number' : 'text'} placeholder={tipoBusca === 'ID' ? "Ex: 10" : "Ex: 000.000.000-00"} value={valorBusca} onChange={e => setValorBusca(e.target.value)} className="border p-2 rounded flex-1 focus:ring-2 focus:ring-blue-500 outline-none"/>
                <button type="submit" disabled={loadingBusca || !valorBusca} className="bg-slate-200 hover:bg-slate-300 px-3 rounded transition-colors disabled:opacity-50">{loadingBusca ? <Loader2 size={18} className="animate-spin"/> : <Search size={18} />}</button>
              </form>
              {cliente && (
                <div className="mt-3 p-3 bg-green-50 border border-green-200 rounded text-sm text-green-800 animate-in fade-in slide-in-from-top-2">
                  <p className="font-bold text-lg">{cliente.nome}</p>
                  <div className="text-green-700 flex flex-col gap-1 mt-1 text-xs">
                    <span>ID: <b>{cliente.idPessoa}</b></span>
                    <span>CPF: {cliente.cpf}</span>
                  </div>
                </div>
              )}
            </div>
            <div>
              <label className="block text-sm font-bold text-slate-700 mb-2">2. Selecionar Equipamento</label>
              <select className="w-full border p-2 rounded bg-white focus:ring-2 focus:ring-blue-500 outline-none" onChange={e => setIdEquipamento(e.target.value)} value={idEquipamento}>
                <option value="">Selecione...</option>
                {equipamentos.map(eq => (<option key={eq.idEquipamento} value={eq.idEquipamento}>{eq.nomeEquipamento} - R$ {eq.valorDiaria?.toFixed(2)}/dia</option>))}
              </select>
            </div>
          </div>
          <div className="space-y-6 border-l pl-0 md:pl-8 border-slate-100">
            <div className="grid grid-cols-2 gap-4">
              <div><label className="block text-xs font-bold text-slate-500 mb-1">Retirada</label><input type="date" className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" value={dataRet} onChange={e => setDataRet(e.target.value)} /></div>
              <div><label className="block text-xs font-bold text-slate-500 mb-1">Devolução</label><input type="date" className="w-full border p-2 rounded focus:ring-2 focus:ring-blue-500 outline-none" value={dataDev} onChange={e => setDataDev(e.target.value)} /></div>
            </div>
            <div className="bg-blue-50 p-6 rounded-xl text-center border border-blue-100">
              <p className="text-sm text-blue-600 font-medium mb-1">Valor Total Estimado</p>
              <div className="text-4xl font-bold text-blue-800 flex items-center justify-center"><DollarSign size={28} /> {total.toFixed(2)}</div>
            </div>
            <button onClick={registrar} disabled={!cliente || total <= 0 || loading} className="w-full bg-green-600 text-white py-3 rounded-lg font-bold hover:bg-green-700 disabled:opacity-50 flex items-center justify-center gap-2">{loading && <Loader2 className="animate-spin h-5 w-5" />}Confirmar Locação</button>
          </div>
        </div>
      </section>

      <hr className="border-slate-200" />

      <section>
        <h2 className="text-xl font-bold mb-4 flex items-center gap-2 text-slate-700">
          <ClipboardList /> Consultar Aluguéis
        </h2>

        <div className="bg-white rounded-lg shadow border border-slate-200 overflow-hidden">
          <div className="p-4 border-b border-slate-100 bg-slate-50 flex gap-2">
            <form onSubmit={handleBuscarAluguel} className="flex gap-2 w-full max-w-md">
              <input type="number" placeholder="Buscar por Nº do Aluguel" value={buscaNro} onChange={(e) => setBuscaNro(e.target.value)} className="border p-2 rounded flex-1 focus:outline-none focus:border-blue-500"/>
              <button type="submit" className="bg-blue-600 text-white px-4 rounded hover:bg-blue-700 flex items-center gap-2"><Search size={16} /> Buscar</button>
              {buscaNro && <button type="button" onClick={() => { setBuscaNro(''); carregarAlugueis(); }} className="bg-white border border-slate-300 text-slate-600 px-3 rounded hover:bg-slate-100" title="Limpar Filtro"><RefreshCcw size={16} /></button>}
            </form>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-slate-100 text-slate-600 border-b border-slate-200">
                <tr><th className="p-4">Nº</th><th className="p-4">Cliente</th><th className="p-4">Equipamento</th><th className="p-4">Período</th><th className="p-4 text-right">Total</th></tr>
              </thead>
              <tbody>
                {loadingTabela ? (
                  <tr><td colSpan="5" className="p-8 text-center text-slate-500"><Loader2 className="animate-spin h-6 w-6 mx-auto mb-2" />Carregando...</td></tr>
                ) : alugueis.length > 0 ? (
                  alugueis.map((a) => (
                    <tr key={a.nroAluguel || a.idAluguel} className="border-b border-slate-50 hover:bg-slate-50">
                      <td className="p-4 font-bold text-slate-700">#{a.nroAluguel}</td>
                      <td className="p-4">
                        <div className="font-medium">{a.cliente?.nome}</div>
                        <div className="text-xs text-slate-400">{a.cliente?.cpf}</div>
                      </td>
                      <td className="p-4"><span className="bg-blue-50 text-blue-700 px-2 py-1 rounded text-xs font-semibold">{a.equipamento?.nomeEquipamento}</span></td>
                      <td className="p-4 text-slate-600">{a.dataLocacao} ➜ {a.dataDevolucao}</td>
                      <td className="p-4 text-right font-bold text-green-600">R$ {a.valorTotalLocacao?.toFixed(2)}</td>
                    </tr>
                  ))
                ) : (
                  <tr><td colSpan="5" className="p-8 text-center text-slate-500">Nenhum aluguel encontrado.</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </section>

      {modalAberto && aluguelDetalhado && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 animate-in fade-in duration-200">
          <div className="bg-white rounded-xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-y-auto">
            
            <div className="sticky top-0 bg-white border-b border-slate-100 p-4 flex items-center justify-between z-10">
              <h2 className="text-xl font-bold text-slate-800 flex items-center gap-2">
                <ClipboardList className="text-blue-600"/> 
                Detalhes do Aluguel #{aluguelDetalhado.nroAluguel}
              </h2>
              <button onClick={() => setModalAberto(false)} className="p-2 hover:bg-slate-100 rounded-full transition-colors">
                <X size={24} className="text-slate-500"/>
              </button>
            </div>

            <div className="p-6 space-y-8">
              
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-blue-50 p-4 rounded-lg border border-blue-100">
                  <span className="text-xs font-bold text-blue-600 uppercase">Data Pedido</span>
                  <div className="text-lg font-semibold text-slate-800">{aluguelDetalhado.dataPedido}</div>
                </div>
                <div className="bg-green-50 p-4 rounded-lg border border-green-100">
                  <span className="text-xs font-bold text-green-600 uppercase">Valor Total</span>
                  <div className="text-lg font-semibold text-slate-800">R$ {aluguelDetalhado.valorTotalLocacao?.toFixed(2)}</div>
                </div>
                <div className="bg-purple-50 p-4 rounded-lg border border-purple-100">
                  <span className="text-xs font-bold text-purple-600 uppercase">Período</span>
                  <div className="text-sm font-semibold text-slate-800">
                    {aluguelDetalhado.dataLocacao} <br/> até {aluguelDetalhado.dataDevolucao}
                  </div>
                </div>
              </div>

              <hr className="border-slate-100"/>

              <div>
                <h3 className="text-sm font-bold text-slate-500 uppercase mb-3 flex items-center gap-2"><Box size={16}/> Equipamento</h3>
                <div className="flex items-center gap-4 bg-slate-50 p-4 rounded-lg">
                  <div className="h-12 w-12 bg-white rounded flex items-center justify-center border text-slate-400 font-bold text-xl">
                    {aluguelDetalhado.equipamento?.nomeEquipamento?.charAt(0)}
                  </div>
                  <div>
                    <div className="font-bold text-slate-800">{aluguelDetalhado.equipamento?.nomeEquipamento}</div>
                    <div className="text-sm text-slate-500">
                      Tipo: {aluguelDetalhado.equipamento?.tipoEquipamento?.nomeTipoEquipamento} • 
                      Diária: R$ {aluguelDetalhado.equipamento?.valorDiaria?.toFixed(2)}
                    </div>
                  </div>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                
                <div className="space-y-6">
                   <div>
                      <h3 className="text-sm font-bold text-slate-500 uppercase mb-3 flex items-center gap-2"><User size={16}/> Cliente</h3>
                      <div className="p-3 border rounded-lg bg-slate-50 space-y-1">
                        <div className="font-bold text-lg">{aluguelDetalhado.cliente?.nome}</div>
                        <div className="text-sm text-slate-600">CPF: {aluguelDetalhado.cliente?.cpf}</div>
                        <div className="text-xs text-slate-400">ID: {aluguelDetalhado.cliente?.idPessoa}</div>
                      </div>
                   </div>

                   <div>
                      <h3 className="text-sm font-bold text-slate-500 uppercase mb-3 flex items-center gap-2"><MapPin size={16}/> Endereço</h3>
                      <div className="text-sm text-slate-700 bg-white border p-3 rounded-lg space-y-1">
                         <div className="font-medium">
                            {aluguelDetalhado.cliente?.endereco?.logradouro?.tipoLogradouro?.nomeTipoLogradouro} {aluguelDetalhado.cliente?.endereco?.logradouro?.nomeLogradouro}, {aluguelDetalhado.cliente?.numero}
                         </div>
                         <div>{aluguelDetalhado.cliente?.complemento}</div>
                         <div>Bairro: {aluguelDetalhado.cliente?.endereco?.bairro?.nomeBairro}</div>
                         <div>
                            {aluguelDetalhado.cliente?.endereco?.cidade?.nomeCidade} - {aluguelDetalhado.cliente?.endereco?.cidade?.unidadeFederativa?.siglaUF}
                         </div>
                         <div className="text-slate-500 text-xs mt-1">CEP: {aluguelDetalhado.cliente?.endereco?.cep}</div>
                      </div>
                   </div>
                </div>

                <div className="space-y-6">
                   <div>
                      <h3 className="text-sm font-bold text-slate-500 uppercase mb-3 flex items-center gap-2"><Phone size={16}/> Telefones</h3>
                      <ul className="space-y-2">
                        {aluguelDetalhado.cliente?.telefones?.map((tel, i) => (
                          <li key={i} className="flex items-center gap-2 text-sm p-2 bg-slate-50 rounded border border-slate-100">
                             <span className="bg-white px-1.5 py-0.5 rounded border text-xs font-mono text-slate-500">+{tel.ddi?.ddi}</span>
                             <span className="font-medium">({tel.ddd?.ddd}) {tel.numero}</span>
                          </li>
                        ))}
                      </ul>
                   </div>

                   <div>
                      <h3 className="text-sm font-bold text-slate-500 uppercase mb-3 flex items-center gap-2"><Mail size={16}/> Emails</h3>
                      <ul className="space-y-2">
                        {aluguelDetalhado.cliente?.emails?.map((email, i) => (
                          <li key={i} className="text-sm p-2 bg-slate-50 rounded border border-slate-100 text-blue-600 underline">
                             {email.enderecoEmail}
                          </li>
                        ))}
                      </ul>
                   </div>
                </div>
              </div>

            </div>

            <div className="p-4 bg-slate-50 border-t border-slate-200 flex justify-end">
              <button onClick={() => setModalAberto(false)} className="px-6 py-2 bg-slate-200 hover:bg-slate-300 text-slate-700 font-bold rounded-lg transition-colors">
                Fechar
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}