import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { Layers, UserCog, ClipboardCheck, Users } from 'lucide-react';

import TiposServicoPage from './pages/TiposServicoPage';
import AtendentesPage from './pages/AtendentesPage';
import OrdemServicoPage from './pages/OrdemServicoPage';
import ClientesPage from './pages/ClientesPage'; 

function App() {
  return (
    <BrowserRouter>
      <div className="flex min-h-screen bg-slate-50 font-sans text-slate-900">
        
        <aside className="w-64 bg-slate-900 text-slate-300 flex flex-col">
          <div className="p-6 border-b border-slate-800">
            <h1 className="text-xl font-bold text-white tracking-tight">Sistema OS</h1>
            <p className="text-xs text-slate-500 mt-1">Módulo de Serviços</p>
          </div>
          
          <nav className="flex-1 p-4 space-y-2">
            
            <Link to="/tipos-servico" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <Layers size={20} /> Tipos de Serviço
            </Link>

            <Link to="/atendentes" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <UserCog size={20} /> Atendentes
            </Link>

            <Link to="/clientes" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <Users size={20} /> Clientes
            </Link>

            <Link to="/ordens-servico" className="flex items-center gap-3 px-4 py-3 hover:bg-slate-800 rounded-lg transition-colors hover:text-white">
              <ClipboardCheck size={20} /> Ordem de Serviço
            </Link>

          </nav>

          <div className="p-4 text-xs text-slate-600 text-center">
            &copy; 2025 TechServices
          </div>
        </aside>

        <main className="flex-1 overflow-auto bg-slate-100">
          <Routes>
            <Route path="/tipos-servico" element={<TiposServicoPage />} />
            <Route path="/atendentes" element={<AtendentesPage />} />
            <Route path="/clientes" element={<ClientesPage />} />
            <Route path="/ordens-servico" element={<OrdemServicoPage />} />
            
            <Route path="/" element={
              <div className="flex flex-col items-center justify-center h-full text-slate-400">
                <ClipboardCheck size={64} className="mb-4 opacity-20"/>
                <p>Selecione uma opção no menu lateral.</p>
              </div>
            } />
          </Routes>
        </main>
        
      </div>
    </BrowserRouter>
  );
}

export default App;