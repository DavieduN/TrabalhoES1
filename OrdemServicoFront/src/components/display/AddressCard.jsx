import React from 'react';

export function AddressCard({ data, onClick }) {
  if (!data) return null;

  // Verifica se temos o objeto aninhado ou se os dados estão planos (dependendo do endpoint)
  const logradouro = data.logradouro?.nomeLogradouro || 'Logradouro não informado';
  const tipo = data.logradouro?.tipoLogradouro?.nomeTipoLogradouro || '';
  const bairro = data.bairro?.nomeBairro || 'Bairro não informado';
  const cidade = data.cidade?.nomeCidade || '';
  const uf = data.cidade?.unidadeFederativa?.siglaUF || '';
  const cep = data.cep || '';

  return (
    <div
      onClick={() => onClick && onClick(data)}
      className={`
        bg-white p-4 rounded-lg shadow-sm border border-slate-200 
        transition-colors
        ${onClick ? 'cursor-pointer hover:bg-blue-50 hover:border-blue-300' : ''}
      `}
    >
      <div className="flex flex-col gap-1">
        <h3 className="font-bold text-slate-800">
          <span className="font-normal text-slate-500 text-sm mr-1">{tipo}</span>
          {logradouro}
        </h3>

        <p className="text-slate-600 text-sm">
          {bairro} - {cidade}/{uf}
        </p>

        <div className="flex justify-between items-center mt-2">
          <span className="font-mono text-xs bg-slate-100 px-2 py-1 rounded text-slate-600">
            CEP: {cep}
          </span>
          {data.idEndereco && (
             <span className="text-xs text-slate-400">ID: {data.idEndereco}</span>
          )}
        </div>
      </div>
    </div>
  );
}