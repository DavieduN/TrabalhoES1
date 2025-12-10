import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import FormEndereco from '../componentes/FormEndereco' // Ajuste o caminho se necessário

// Mock dos serviços para o componente não tentar chamar o Java de verdade durante o teste visual
vi.mock('../api/enderecoApi', () => ({
  listarUFs: vi.fn(() => Promise.resolve([])),
  listarTiposLogradouro: vi.fn(() => Promise.resolve([])),
  consultarExternoPorCEP: vi.fn(),
  cadastrarEndereco: vi.fn()
}))

describe('FormEndereco Component (Interface)', () => {
  it('deve renderizar os campos obrigatórios na tela', () => {
    render(<FormEndereco />)

    // Verifica se os inputs existem pelo Placeholder ou Label
    // Como usamos placeholder="00000000" no CEP:
    expect(screen.getByPlaceholderText(/00000000/i)).toBeInTheDocument()
    
    // Verifica botões
    expect(screen.getByText(/Buscar/i)).toBeInTheDocument()
    expect(screen.getByText(/Cadastrar/i)).toBeInTheDocument()
    
    // Verifica textos de label
    expect(screen.getByText(/Logradouro/i)).toBeInTheDocument()
    expect(screen.getByText(/Número/i)).toBeInTheDocument()
  })
})