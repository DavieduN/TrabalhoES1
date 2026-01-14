import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { Package, Users, ClipboardList } from 'lucide-react';

import EstoquePage from './pages/EstoquePage';
import ClientesPage from './pages/ClientesPage';
import AluguelPage from './pages/AluguelPage';

function App() {
  return (
    <BrowserRouter>
      <div className="flex min-h-screen bg-slate-50 font-sans text-slate-900">
        
        <aside className="w-64 bg-slate-900 text-slate-300 flex flex-col">
          <div className="p-6 border-b border-slate-800">
            <h1 className="text-xl font-bold text-white tracking-tight">RentalSystem</h1>
          </div>
          
          <nav className="flex-1 p-4 space-y-2">
            <Link to="/estoque" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <Package size={20} /> Estoque
            </Link>
            <Link to="/clientes" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <Users size={20} /> Clientes
            </Link>
            <Link to="/aluguel" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <ClipboardList size={20} /> Registrar Aluguel
            </Link>
          </nav>

          <div className="p-4 text-xs text-slate-600 text-center">
            &copy; 2025 RentalSystem
          </div>
        </aside>

        <main className="flex-1 overflow-auto">
          <Routes>
            <Route path="/estoque" element={<EstoquePage />} />
            <Route path="/clientes" element={<ClientesPage />} />
            <Route path="/aluguel" element={<AluguelPage />} />
            <Route path="/" element={<div className="p-10 text-slate-500">Selecione um módulo no menu.</div>} />
          </Routes>
        </main>
        
      </div>
    </BrowserRouter>
  );
}

export default App;