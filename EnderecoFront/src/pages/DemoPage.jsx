import React from 'react';
import { EndpointViewer } from '../components/display/EndpointViewer';
import { CityCard } from '../components/display/CityCard';
import { AddressCard } from '../components/display/AddressCard';
import { EnderecoService } from '../services/api';

export default function DemoPage() {
  return (
    <div className="container mx-auto p-6 max-w-4xl space-y-8">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-slate-800">Demonstração da API</h1>
      </header>

      <EndpointViewer
        title="1. Consultar Endereço pelo ViaCEP"
        label="Digite o CEP (somente números)"
        ResultComponent={AddressCard}
        onSearch={EnderecoService.consultarCepExterno}
      />

      <EndpointViewer
        title="2. Consultar Endereços no Banco Local"
        label="Digite o CEP (somente números)"
        ResultComponent={AddressCard}
        onSearch={EnderecoService.consultarCepInterno}
      />

      <EndpointViewer
        title="3. Buscar Endereço por ID"
        label="ID do Endereço"
        ResultComponent={AddressCard}
        onSearch={EnderecoService.buscarEnderecoPorId}
      />

      <EndpointViewer
        title="4. Buscar Cidade por ID"
        label="ID da Cidade"
        ResultComponent={CityCard}
        onSearch={EnderecoService.buscarCidadePorId}
      />
    </div>
  );
}