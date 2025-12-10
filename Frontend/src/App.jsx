import React from 'react'
import FormEndereco from './componentes/FormEndereco' 
import ListaEnderecos from './componentes/ListaEnderecos'

export default function App() {
  return (
    <div className="min-h-screen bg-slate-900 text-slate-100 p-4 md:p-8">
      <div className="max-w-7xl mx-auto space-y-8">
        
        {/* HEADER */}
        <header className="flex flex-col gap-2 pb-6 border-b border-slate-800">
          <h1 className="text-3xl font-extrabold tracking-tight text-white">
            Manter Endereço <span className="text-blue-500">— T3B</span>
          </h1>
          <p className="text-slate-400">
            Gerenciamento de endereços com integração ViaCEP e banco local.
          </p>
        </header>

        {/* MAIN CONTENT */}
        <main className="grid grid-cols-1 xl:grid-cols-2 gap-8 items-start">
          
          {/* COLUNA 1: FORMULÁRIO */}
          <section>
            <FormEndereco />
          </section>

          {/* COLUNA 2: LISTAGEM */}
          <section>
             {/* Note que aqui eu removi o cabeçalho duplicado pois já está dentro do componente Lista */}
            <ListaEnderecos />
          </section>

        </main>

        {/* FOOTER */}
        <footer className="text-center text-slate-500 text-sm py-8 border-t border-slate-800 mt-12">
          Trabalho T3B — MyEndereco • React + Java Servlets • {new Date().getFullYear()}
        </footer>
      </div>
    </div>
  )
}