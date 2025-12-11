import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { LayoutDashboard, FileText } from 'lucide-react';

import DemoPage from './pages/DemoPage';
import RegistryPage from './pages/RegistryPage';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-slate-50 flex flex-col font-sans text-slate-900">

        <nav className="bg-white border-b border-slate-200 sticky top-0 z-50">
          <div className="container mx-auto px-4 h-16 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="font-bold text-xl tracking-tight text-slate-800">
                EnderecoAPI
              </span>
            </div>

            <div className="flex gap-1">
              <Link
                to="/"
                className="flex items-center gap-2 px-4 py-2 rounded-md text-sm font-medium text-slate-600 hover:bg-slate-100 hover:text-blue-600 transition-colors"
              >
                <LayoutDashboard size={18} />
                Demonstração API
              </Link>
              <Link
                to="/cadastro"
                className="flex items-center gap-2 px-4 py-2 rounded-md text-sm font-medium text-slate-600 hover:bg-slate-100 hover:text-blue-600 transition-colors"
              >
                <FileText size={18} />
                Cadastro Completo
              </Link>
            </div>
          </div>
        </nav>

        <main className="flex-1">
          <Routes>
            <Route path="/" element={<DemoPage />} />
            <Route path="/cadastro" element={<RegistryPage />} />
          </Routes>
        </main>

      </div>
    </BrowserRouter>
  );
}

export default App;