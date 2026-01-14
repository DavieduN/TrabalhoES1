import React from 'react';

export function CityCard({ data }) {
  if (!data) return null;

  return (
    <div className="bg-white p-4 rounded-lg shadow-sm border border-slate-200">
      <div className="flex justify-between items-start">
        <div>
          <h3 className="font-bold text-lg text-slate-800">{data.nomeCidade}</h3>
          <span className="text-sm text-slate-500">ID: {data.idCidade}</span>
        </div>
        {data.unidadeFederativa && (
          <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">
            {data.unidadeFederativa.siglaUF}
          </span>
        )}
      </div>
    </div>
  );
}