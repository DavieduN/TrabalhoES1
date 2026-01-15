import React, { useEffect, useState } from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { ClipboardList, Search, Plus, Trash2, Save, Loader2, User, Wrench, DollarSign, X, MapPin, Phone, Mail } from 'lucide-react';
import { SistemaService } from '../services/api';

export default function OrdemServicoPage() {
  const [tiposServico, setTiposServico] = useState([]);
  const [listaOS, setListaOS] = useState([]);
  
  const [cliente, setCliente] = useState(null);
  const [atendente, setAtendente] = useState(null);
  
  const [buscaClienteTermo, setBuscaClienteTermo] = useState('');
  const [buscaAtendenteTermo, setBuscaAtendenteTermo] = useState('');

  const [tipoBuscaCliente, setTipoBuscaCliente] = useState('ID');
  const [tipoBuscaAtendente, setTipoBuscaAtendente] = useState('ID');
  
  const [osDetalhada, setOsDetalhada] = useState(null);
  const [modalAberto, setModalAberto] = useState(false);
  const [buscaNroOS, setBuscaNroOS] = useState('');

  const { register, control, handleSubmit, watch, reset, setValue } = useForm({
    defaultValues: {
      descricaoProblema: '',
      itens: [{ idTipoServico: '', valor: '' }]
    }
  });

  const { fields, append, remove } = useFieldArray({ control, name: "itens" });
  
  const itensAssistidos = watch("itens");
  const totalCalculado = itensAssistidos.reduce((acc, curr) => acc + (parseFloat(curr.valor) || 0), 0);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    SistemaService.listarTiposServico().then(setTiposServico);
    carregarListaOS();
  }, []);

  const carregarListaOS = () => {
    SistemaService.listarOSs().then(dados => setListaOS(Array.isArray(dados) ? dados : []));
  };

  const buscarAtor = async (tipoAtor) => {
    const termo = tipoAtor === 'CLIENTE' ? buscaClienteTermo : buscaAtendenteTermo;
    const metodo = tipoAtor === 'CLIENTE' ? tipoBuscaCliente : tipoBuscaAtendente;

    if (!termo) return;

    try {
      let res = null;
      
      if (tipoAtor === 'CLIENTE') {
        if (metodo === 'ID') {
           res = await SistemaService.buscarClientePorId(termo);
        } else {
           res = await SistemaService.buscarClientePorCpf(termo.replace(/\D/g, ''));
        }
        
        if (res && res.idPessoa) setCliente(res); 
        else alert('Cliente não encontrado');
      
      } else {
        if (metodo === 'ID') {
           res = await SistemaService.buscarAtendentePorId(termo);
        } else {
           res = await SistemaService.buscarAtendentePorCpf(termo.replace(/\D/g, ''));
        }

        if (res && res.idPessoa) setAtendente(res); 
        else alert('Atendente não encontrado');
      }

    } catch (e) { 
      console.error(e);
      alert('Erro na busca (verifique se o ID/CPF está correto)'); 
    }
  };

  const buscarOSEspecifica = async (e) => {
    e.preventDefault();
    if(!buscaNroOS) return;
    try {
        const res = await SistemaService.buscarOSPorNumero(buscaNroOS);
        const os = Array.isArray(res) ? res[0] : res;
        
        if (os) {
            setOsDetalhada(os);
            setModalAberto(true);
        } else {
            alert("OS não encontrada");
        }
    } catch (e) { console.error(e); alert("Erro ao buscar OS"); }
  };

  const onSubmit = async (data) => {
    if (!cliente || !atendente) return alert("Selecione Cliente e Atendente!");
    setLoading(true);
    
    try {
      const payload = {
        dataEmissao: new Date().toISOString().split('T')[0],
        descricaoProblema: data.descricaoProblema,
        cliente: { idPessoa: cliente.idPessoa },
        atendente: { idPessoa: atendente.idPessoa },
        listaItens: data.itens.map(item => ({
          valorServico: parseFloat(item.valor),
          tipoServico: { idTipoServico: parseInt(item.idTipoServico) }
        }))
      };

      await SistemaService.registrarOS(payload);
      alert("Ordem de Serviço registrada com sucesso!");
      
      reset();
      setCliente(null);
      setAtendente(null);
      setBuscaClienteTermo('');
      setBuscaAtendenteTermo('');
      carregarListaOS();

    } catch (error) {
      console.error(error);
      alert("Erro ao registrar OS.");
    } finally { setLoading(false); }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto space-y-8">
      <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
        <ClipboardList /> Gestão de Ordens de Serviço
      </h1>

      <div className="bg-white p-6 rounded-lg shadow border border-slate-200 grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        <div className="lg:col-span-1 space-y-6">
          
          <div className="bg-slate-50 p-4 rounded border border-slate-200">
             <label className="text-xs font-bold text-slate-500 uppercase mb-2 block">Cliente</label>
             
             <div className="flex gap-2 mb-2 text-[10px] font-bold">
                <button onClick={() => { setTipoBuscaCliente('ID'); setBuscaClienteTermo(''); }} 
                  className={`px-2 py-1 rounded border ${tipoBuscaCliente === 'ID' ? 'bg-blue-600 text-white' : 'bg-white'}`}>
                  Por ID
                </button>
                <button onClick={() => { setTipoBuscaCliente('CPF'); setBuscaClienteTermo(''); }} 
                  className={`px-2 py-1 rounded border ${tipoBuscaCliente === 'CPF' ? 'bg-blue-600 text-white' : 'bg-white'}`}>
                  Por CPF
                </button>
             </div>

             <div className="flex gap-2">
               <input 
                 value={buscaClienteTermo} 
                 onChange={e=>setBuscaClienteTermo(e.target.value)} 
                 type={tipoBuscaCliente === 'ID' ? 'number' : 'text'}
                 className="border p-2 rounded w-full text-sm" 
                 placeholder={tipoBuscaCliente === 'ID' ? "ID..." : "CPF..."}
               />
               <button onClick={()=>buscarAtor('CLIENTE')} className="bg-white border p-2 rounded hover:bg-slate-100">
                 <Search size={18}/>
               </button>
             </div>

             {cliente && (
               <div className="mt-3 p-2 bg-green-50 border border-green-200 rounded text-sm text-green-900 animate-in fade-in">
                 <div className="font-bold">{cliente.nome}</div>
                 <div className="text-xs flex flex-col gap-1 mt-1 text-green-700">
                   <span>CPF: {cliente.cpf}</span>
                   <span>ID: {cliente.idPessoa}</span>
                 </div>
               </div>
             )}
          </div>

          <div className="bg-slate-50 p-4 rounded border border-slate-200">
             <label className="text-xs font-bold text-slate-500 uppercase mb-2 block">Atendente</label>
             
             <div className="flex gap-2 mb-2 text-[10px] font-bold">
                <button onClick={() => { setTipoBuscaAtendente('ID'); setBuscaAtendenteTermo(''); }} 
                  className={`px-2 py-1 rounded border ${tipoBuscaAtendente === 'ID' ? 'bg-blue-600 text-white' : 'bg-white'}`}>
                  Por ID
                </button>
                <button onClick={() => { setTipoBuscaAtendente('CPF'); setBuscaAtendenteTermo(''); }} 
                  className={`px-2 py-1 rounded border ${tipoBuscaAtendente === 'CPF' ? 'bg-blue-600 text-white' : 'bg-white'}`}>
                  Por CPF
                </button>
             </div>

             <div className="flex gap-2">
               <input 
                 value={buscaAtendenteTermo} 
                 onChange={e=>setBuscaAtendenteTermo(e.target.value)} 
                 type={tipoBuscaAtendente === 'ID' ? 'number' : 'text'}
                 className="border p-2 rounded w-full text-sm" 
                 placeholder={tipoBuscaAtendente === 'ID' ? "ID..." : "CPF..."}
               />
               <button onClick={()=>buscarAtor('ATENDENTE')} className="bg-white border p-2 rounded hover:bg-slate-100">
                 <Search size={18}/>
               </button>
             </div>

             {atendente && (
               <div className="mt-3 p-2 bg-blue-50 border border-blue-200 rounded text-sm text-blue-900 animate-in fade-in">
                 <div className="font-bold">{atendente.nome}</div>
                 <div className="text-xs flex flex-col gap-1 mt-1 text-blue-700">
                   <span>CPF: {atendente.cpf}</span>
                   <span>ID: {atendente.idPessoa}</span>
                 </div>
               </div>
             )}
          </div>

          <div>
            <label className="block text-sm font-bold text-slate-700 mb-2">Descrição do Problema</label>
            <textarea 
              {...register('descricaoProblema', { required: true })}
              className="w-full border p-2 rounded h-24 resize-none focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Ex: Computador não liga..."
            />
          </div>
        </div>

        <div className="lg:col-span-2 flex flex-col">
          <div className="flex-1 space-y-4">
             <div className="flex justify-between items-center mb-2">
               <label className="text-sm font-bold text-slate-700">Serviços Realizados</label>
               <button type="button" onClick={() => append({ idTipoServico: '', valor: '' })} className="text-sm text-blue-600 hover:text-blue-800 flex items-center gap-1">
                 <Plus size={16}/> Adicionar Item
               </button>
             </div>

             <div className="space-y-3 max-h-[400px] overflow-y-auto pr-2">
               {fields.map((item, index) => (
                 <div key={item.id} className="flex gap-3 items-start bg-slate-50 p-3 rounded border border-slate-200">
                   <div className="flex-1">
                     <label className="text-xs text-slate-400 mb-1 block">Tipo de Serviço</label>
                     <select 
                        {...register(`itens.${index}.idTipoServico`, { required: true })}
                        className="w-full border p-2 rounded bg-white"
                     >
                        <option value="">Selecione...</option>
                        {tiposServico.map(t => <option key={t.idTipoServico} value={t.idTipoServico}>{t.nomeTipoServico}</option>)}
                     </select>
                   </div>
                   <div className="w-32">
                     <label className="text-xs text-slate-400 mb-1 block">Valor (R$)</label>
                     <input 
                        type="number" step="0.01" 
                        {...register(`itens.${index}.valor`, { required: true })}
                        className="w-full border p-2 rounded" placeholder="0.00"
                     />
                   </div>
                   <button type="button" onClick={() => remove(index)} className="mt-6 text-red-500 hover:bg-red-100 p-2 rounded"><Trash2 size={18}/></button>
                 </div>
               ))}
             </div>
          </div>

          <div className="mt-6 pt-6 border-t border-slate-100 flex items-center justify-between">
            <div className="text-right">
               <p className="text-sm text-slate-500">Valor Total Estimado</p>
               <p className="text-3xl font-bold text-slate-800">R$ {totalCalculado.toFixed(2)}</p>
            </div>
            <button 
              onClick={handleSubmit(onSubmit)}
              disabled={loading}
              className="bg-green-600 text-white px-8 py-4 rounded-lg font-bold hover:bg-green-700 flex items-center gap-2 disabled:opacity-50"
            >
              {loading ? <Loader2 className="animate-spin"/> : <Save />} Finalizar OS
            </button>
          </div>
        </div>
      </div>

      <hr className="border-slate-200"/>

      <div>
         <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-slate-700">Histórico de Ordens</h2>
            <form onSubmit={buscarOSEspecifica} className="flex gap-2">
               <input 
                 type="number" placeholder="Buscar Nº OS" 
                 value={buscaNroOS} onChange={e=>setBuscaNroOS(e.target.value)}
                 className="border p-2 rounded w-40"
               />
               <button type="submit" className="bg-blue-600 text-white p-2 rounded hover:bg-blue-700"><Search size={20}/></button>
            </form>
         </div>

         <div className="bg-white rounded-lg shadow border border-slate-200 overflow-hidden">
            <table className="w-full text-left text-sm">
               <thead className="bg-slate-100 text-slate-600">
                  <tr><th className="p-4">Nº</th><th className="p-4">Data</th><th className="p-4">Cliente</th><th className="p-4">Descrição</th><th className="p-4 text-right">Total</th></tr>
               </thead>
               <tbody>
                  {listaOS.map(os => (
                     <tr key={os.nroOrdemServico} className="border-t hover:bg-slate-50">
                        <td className="p-4 font-bold">#{os.nroOrdemServico}</td>
                        <td className="p-4">{os.dataEmissao}</td>
                        <td className="p-4">{os.cliente?.nome}</td>
                        <td className="p-4 truncate max-w-xs">{os.descricaoProblema}</td>
                        <td className="p-4 text-right font-bold text-green-600">R$ {os.valorTotal?.toFixed(2)}</td>
                     </tr>
                  ))}
               </tbody>
            </table>
         </div>
      </div>
      
      {modalAberto && osDetalhada && (
         <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 animate-in fade-in duration-200">
            <div className="bg-white rounded-xl shadow-2xl w-full max-w-5xl max-h-[90vh] overflow-y-auto flex flex-col">
               
               <div className="p-6 border-b border-slate-100 flex justify-between items-start bg-slate-50 rounded-t-xl">
                  <div>
                     <h2 className="text-2xl font-bold flex items-center gap-2 text-slate-800">
                        <Wrench className="text-blue-600"/> OS #{osDetalhada.nroOrdemServico}
                     </h2>
                     <p className="text-slate-500 text-sm mt-1">
                        Emitida em: <span className="font-medium text-slate-700">
                           {osDetalhada.dataEmissao ? osDetalhada.dataEmissao.split('-').reverse().join('/') : '-'}
                        </span>
                     </p>
                  </div>
                  <button onClick={()=>setModalAberto(false)} className="p-2 hover:bg-slate-200 rounded-full transition-colors">
                     <X size={24} className="text-slate-500"/>
                  </button>
               </div>

               <div className="p-6 space-y-8 overflow-y-auto">

                  <div className="bg-amber-50 p-4 rounded-lg border border-amber-100">
                     <span className="font-bold text-amber-700 uppercase text-xs tracking-wider">Descrição do Problema</span>
                     <p className="mt-2 text-lg text-slate-800 leading-relaxed">{osDetalhada.descricaoProblema || "Sem descrição."}</p>
                  </div>

                  <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                     
                     <div className="border border-slate-200 rounded-xl overflow-hidden">
                        <div className="bg-slate-100 p-3 border-b border-slate-200 flex items-center gap-2 font-bold text-slate-700">
                           <User size={18}/> Cliente
                        </div>
                        <div className="p-4 space-y-4 text-sm">
                           <div>
                              <div className="text-lg font-bold text-slate-800">{osDetalhada.cliente?.nome}</div>
                              <div className="text-slate-500">CPF: {osDetalhada.cliente?.cpf} • ID: {osDetalhada.cliente?.idPessoa}</div>
                           </div>
                           
                           <div className="bg-slate-50 p-3 rounded border border-slate-100">
                              <h4 className="font-bold text-xs text-slate-400 uppercase mb-2 flex items-center gap-1"><MapPin size={12}/> Endereço</h4>
                              <p>
                                 {osDetalhada.cliente?.endereco?.logradouro?.tipoLogradouro?.nomeTipoLogradouro} {osDetalhada.cliente?.endereco?.logradouro?.nomeLogradouro}, {osDetalhada.cliente?.numero}
                              </p>
                              {osDetalhada.cliente?.complemento && <p className="text-slate-500">{osDetalhada.cliente.complemento}</p>}
                              <p>{osDetalhada.cliente?.endereco?.bairro?.nomeBairro}</p>
                              <p>{osDetalhada.cliente?.endereco?.cidade?.nomeCidade} - {osDetalhada.cliente?.endereco?.cidade?.unidadeFederativa?.siglaUF}</p>
                              <p className="text-xs text-slate-400 mt-1">CEP: {osDetalhada.cliente?.endereco?.cep}</p>
                           </div>

                           <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                              <div>
                                 <h4 className="font-bold text-xs text-slate-400 uppercase mb-1">Telefones</h4>
                                 <ul className="space-y-1">
                                    {osDetalhada.cliente?.telefones?.length > 0 ? osDetalhada.cliente.telefones.map((tel, i) => (
                                       <li key={i} className="flex items-center gap-1 text-slate-700">
                                          <Phone size={12} className="text-blue-500"/> 
                                          <span>+{tel.ddi?.ddi} ({tel.ddd?.ddd}) {tel.numero}</span>
                                       </li>
                                    )) : <span className="text-slate-400 italic">Sem telefone</span>}
                                 </ul>
                              </div>
                              <div>
                                 <h4 className="font-bold text-xs text-slate-400 uppercase mb-1">E-mails</h4>
                                 <ul className="space-y-1">
                                    {osDetalhada.cliente?.emails?.length > 0 ? osDetalhada.cliente.emails.map((email, i) => (
                                       <li key={i} className="flex items-center gap-1 text-slate-700 break-all">
                                          <Mail size={12} className="text-blue-500"/>
                                          <span>{email.enderecoEmail}</span>
                                       </li>
                                    )) : <span className="text-slate-400 italic">Sem e-mail</span>}
                                 </ul>
                              </div>
                           </div>
                        </div>
                     </div>

                     <div className="border border-slate-200 rounded-xl overflow-hidden">
                        <div className="bg-slate-100 p-3 border-b border-slate-200 flex items-center gap-2 font-bold text-slate-700">
                           <User size={18}/> Atendente
                        </div>
                        <div className="p-4 space-y-4 text-sm">
                           <div>
                              <div className="text-lg font-bold text-slate-800">{osDetalhada.atendente?.nome}</div>
                              <div className="text-slate-500">CPF: {osDetalhada.atendente?.cpf} • ID: {osDetalhada.atendente?.idPessoa}</div>
                           </div>

                           <div className="bg-slate-50 p-3 rounded border border-slate-100">
                              <h4 className="font-bold text-xs text-slate-400 uppercase mb-2 flex items-center gap-1"><MapPin size={12}/> Endereço</h4>
                              <p>
                                 {osDetalhada.atendente?.endereco?.logradouro?.tipoLogradouro?.nomeTipoLogradouro} {osDetalhada.atendente?.endereco?.logradouro?.nomeLogradouro}, {osDetalhada.atendente?.numero}
                              </p>
                              {osDetalhada.atendente?.complemento && <p className="text-slate-500">{osDetalhada.atendente.complemento}</p>}
                              <p>{osDetalhada.atendente?.endereco?.bairro?.nomeBairro}</p>
                              <p>{osDetalhada.atendente?.endereco?.cidade?.nomeCidade} - {osDetalhada.atendente?.endereco?.cidade?.unidadeFederativa?.siglaUF}</p>
                              <p className="text-xs text-slate-400 mt-1">CEP: {osDetalhada.atendente?.endereco?.cep}</p>
                           </div>

                           <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                              <div>
                                 <h4 className="font-bold text-xs text-slate-400 uppercase mb-1">Telefones</h4>
                                 <ul className="space-y-1">
                                    {osDetalhada.atendente?.telefones?.length > 0 ? osDetalhada.atendente.telefones.map((tel, i) => (
                                       <li key={i} className="flex items-center gap-1 text-slate-700">
                                          <Phone size={12} className="text-blue-500"/> 
                                          <span>+{tel.ddi?.ddi} ({tel.ddd?.ddd}) {tel.numero}</span>
                                       </li>
                                    )) : <span className="text-slate-400 italic">Sem telefone</span>}
                                 </ul>
                              </div>
                              <div>
                                 <h4 className="font-bold text-xs text-slate-400 uppercase mb-1">E-mails</h4>
                                 <ul className="space-y-1">
                                    {osDetalhada.atendente?.emails?.length > 0 ? osDetalhada.atendente.emails.map((email, i) => (
                                       <li key={i} className="flex items-center gap-1 text-slate-700 break-all">
                                          <Mail size={12} className="text-blue-500"/>
                                          <span>{email.enderecoEmail}</span>
                                       </li>
                                    )) : <span className="text-slate-400 italic">Sem e-mail</span>}
                                 </ul>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>

                  <div className="border border-slate-200 rounded-xl overflow-hidden">
                     <div className="bg-slate-100 p-3 border-b border-slate-200 font-bold text-slate-700 flex justify-between items-center">
                        <span>Itens e Serviços Realizados</span>
                        <span className="text-xs bg-slate-200 text-slate-600 px-2 py-1 rounded-full">{osDetalhada.listaItens?.length || 0} itens</span>
                     </div>
                     <table className="w-full text-sm">
                        <thead className="bg-slate-50 text-slate-500 border-b border-slate-100">
                           <tr>
                              <th className="p-3 text-left font-medium">ID</th>
                              <th className="p-3 text-left font-medium">Serviço</th>
                              <th className="p-3 text-right font-medium">Valor</th>
                           </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-100">
                           {osDetalhada.listaItens?.map((item, idx) => (
                              <tr key={idx} className="hover:bg-slate-50">
                                 <td className="p-3 text-slate-400">#{item.idItemServico}</td>
                                 <td className="p-3 font-medium text-slate-700">{item.tipoServico?.nomeTipoServico}</td>
                                 <td className="p-3 text-right text-slate-600">R$ {item.valorServico?.toFixed(2)}</td>
                              </tr>
                           ))}
                        </tbody>
                        <tfoot className="bg-green-50">
                           <tr>
                              <td colSpan="2" className="p-4 font-bold text-green-800 uppercase tracking-wider">Valor Total da OS</td>
                              <td className="p-4 text-right font-bold text-green-700 text-xl">
                                 R$ {osDetalhada.valorTotal?.toFixed(2)}
                              </td>
                           </tr>
                        </tfoot>
                     </table>
                  </div>

               </div>

               <div className="p-4 bg-slate-50 border-t border-slate-200 flex justify-end rounded-b-xl">
                  <button onClick={()=>setModalAberto(false)} className="px-6 py-2 bg-white border border-slate-300 hover:bg-slate-100 text-slate-700 font-bold rounded-lg transition-colors shadow-sm">
                     Fechar
                  </button>
               </div>
            </div>
         </div>
      )}
    </div>
  );
}