import React, { useState } from 'react';
import { Search, Loader2, AlertCircle } from 'lucide-react';

export function EndpointViewer({ title, label, onSearch, ResultComponent }) {
  const [query, setQuery] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const data = await onSearch(query);
      setResult(data);
    } catch (err) {
      console.error(err);
      setError('Erro ao buscar. Verifique o console ou se o ID/CEP existe.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-md overflow-hidden border border-slate-200">
      {/* Cabeçalho */}
      <div className="bg-slate-50 p-4 border-b border-slate-200">
        <h2 className="font-semibold text-slate-700">{title}</h2>
      </div>

      <div className="p-4 flex flex-col gap-4">
        {/* Formulário de Busca */}
        <form onSubmit={handleSearch} className="flex gap-2">
          <input
            type="text"
            placeholder={label}
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            className="flex-1 px-3 py-2 border border-slate-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            type="submit"
            disabled={loading}
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:opacity-50 flex items-center gap-2 transition-colors"
          >
            {loading ? <Loader2 className="animate-spin h-4 w-4" /> : <Search className="h-4 w-4" />}
            Consultar
          </button>
        </form>

        {/* Área de Erro */}
        {error && (
          <div className="p-3 bg-red-50 text-red-700 text-sm rounded-md flex items-center gap-2">
            <AlertCircle className="h-4 w-4" />
            {error}
          </div>
        )}

        {/* Área de Resultados */}
        {result && (
          <div className="bg-slate-50 p-4 rounded-lg border border-slate-200 mt-2">
            <h4 className="text-xs font-bold text-slate-400 uppercase mb-3 tracking-wider">Resultado</h4>

            <div className="flex flex-col gap-3">
              {Array.isArray(result) ? (
                result.length > 0 ? (
                  result.map((item, index) => (
                    <ResultComponent key={index} data={item} />
                  ))
                ) : (
                  <p className="text-sm text-slate-500 italic">Nenhum registro encontrado.</p>
                )
              ) : (
                <ResultComponent data={result} />
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}