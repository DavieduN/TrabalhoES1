import { describe, it, expect, vi, beforeEach } from 'vitest'
import { api, consultarExternoPorCEP, cadastrarEndereco } from '../api/enderecoApi'

vi.mock('../api/enderecoApi', async (importOriginal) => {
  const actual = await importOriginal()
  return {
    ...actual,
    api: {
      get: vi.fn(),
      post: vi.fn(),
      interceptors: {
        response: { use: vi.fn() }
      }
    }
  }
})

describe('Serviços de Endereço (enderecoApi)', () => {
  
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('consultarExternoPorCEP deve chamar o endpoint GET correto', async () => {
    const mockData = { data: { endereco: { logradouro: 'Rua Teste' } } }
    api.get.mockResolvedValue(mockData)

    const cep = '85851000'
    const resultado = await consultarExternoPorCEP(cep)

    expect(api.get).toHaveBeenCalledWith(`/enderecos/externo/${cep}`)
    expect(resultado).toEqual(mockData.data)
  })

  it('cadastrarEndereco deve chamar o endpoint POST com o payload', async () => {
    const mockResponse = { data: { id: 99 } }
    const payload = { numero: '10', endereco: { cep: '123' } }
    api.post.mockResolvedValue(mockResponse)

    const resultado = await cadastrarEndereco(payload)

    expect(api.post).toHaveBeenCalledWith('/enderecos/cadastrar', payload)
    expect(resultado).toEqual(mockResponse.data)
  })
})