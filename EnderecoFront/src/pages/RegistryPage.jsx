import React from 'react';
import { AddressForm } from '../components/form/AddressForm';

export default function RegistryPage() {
  return (
    <div className="container mx-auto p-6 max-w-6xl">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-slate-800">Cadastro de Endereço</h1>
        <p className="text-slate-600">
          Preencha os dados abaixo. O sistema verificará automaticamente se o endereço já existe.
        </p>
      </header>

      <AddressForm />
    </div>
  );
}