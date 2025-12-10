import React from 'react';


export default function CardEndereco({ endereco }) {
  if (!endereco) return null;

  const extractData = (data) => {
    const geoRoot = data.endereco || data;
    const specRoot = data; 
    
    return {
      id: specRoot.idEnderecoEspecifico || specRoot.id || specRoot._id,
      
      logradouro: geoRoot.logradouro?.nomeLogradouro || 
                  geoRoot.nomeLogradouro || 
                  geoRoot.logradouro || 
                  'Logradouro não informado',
                  
      bairro: geoRoot.bairro?.nomeBairro || geoRoot.bairro,
      
      cidade: geoRoot.cidade?.nomeCidade || geoRoot.cidade || geoRoot.localidade,
      
      uf: geoRoot.cidade?.unidadeFederativa?.siglaUF || geoRoot.uf,
      
      numero: specRoot.numero,
      complemento: specRoot.complemento,
      
      cep: geoRoot.cep || specRoot.cep,
    };
  };

  const dados = extractData(endereco);

  return (
    <div className="relative bg-slate-800 border border-slate-700 rounded-lg p-4 shadow-sm hover:border-blue-500 transition-colors group">
      
      <div className="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 rounded-l-lg"></div>
      
      <div className="pl-3 flex flex-col gap-1">
        
        <div className="flex justify-between items-start">
          <h3 className="font-semibold text-slate-100 text-sm">
            {dados.logradouro}
            {dados.numero && <span className="text-blue-400 ml-1"> {dados.numero}</span>}
          </h3>
          
          {dados.id && (
            <span className="text-[10px] font-mono font-bold text-slate-400 bg-slate-900 px-2 py-0.5 rounded border border-slate-700">
              #{dados.id}
            </span>
          )}
        </div>

        <div className="text-xs text-slate-400">
          {dados.complemento && <span className="italic text-slate-500 mr-2">{dados.complemento} •</span>}
          {dados.bairro}
        </div>

        <div className="flex items-center justify-between mt-2 pt-2 border-t border-slate-700/50">
          <span className="text-xs font-medium text-slate-300 bg-slate-700/50 px-2 py-0.5 rounded">
            {dados.cidade}/{dados.uf}
          </span>
          <span className="text-xs font-mono text-slate-500">
            CEP {dados.cep}
          </span>
        </div>

      </div>
    </div>
  );
}