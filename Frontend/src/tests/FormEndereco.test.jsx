import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import FormEndereco from '../componentes/FormEndereco'

vi.mock('../api/enderecoApi', () => ({
  listarUFs: vi.fn(() => Promise.resolve([])),
  listarTiposLogradouro: vi.fn(() => Promise.resolve([])),
  consultarExternoPorCEP: vi.fn(),
  cadastrarEndereco: vi.fn()
}))

describe('FormEndereco Component (Interface)', () => {
  it('deve renderizar os campos obrigatórios na tela', () => {
    render(<FormEndereco />)

    expect(screen.getByPlaceholderText(/00000000/i)).toBeInTheDocument()
    
    expect(screen.getByText(/Buscar/i)).toBeInTheDocument()
    expect(screen.getByText(/Cadastrar/i)).toBeInTheDocument()
    
    expect(screen.getByText(/Logradouro/i)).toBeInTheDocument()
    expect(screen.getByText(/Número/i)).toBeInTheDocument()
  })
})