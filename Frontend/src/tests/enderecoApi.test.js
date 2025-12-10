import { describe, it, expect, vi, beforeEach } from 'vitest'
import { api, consultarExternoPorCEP, cadastrarEndereco } from '../api/enderecoApi'

// Mock do Axios (para não fazer requisição real na internet/java)
vi.mock('../api/enderecoApi', async (importOriginal) => {
  const actual = await importOriginal()
  return {
    ...actual,
    api: {
      get: vi.fn(),
      post: vi.fn(),
      interceptors: {
        response: { use: vi.fn() } // Mock do interceptor
      }
    }
  }
})

describe('Serviços de Endereço (enderecoApi)', () => {
  
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('consultarExternoPorCEP deve chamar o endpoint GET correto', async () => {
    // 1. Prepara o cenário (Mock da resposta)
    const mockData = { data: { endereco: { logradouro: 'Rua Teste' } } }
    api.get.mockResolvedValue(mockData)

    // 2. Executa a ação
    const cep = '85851000'
    const resultado = await consultarExternoPorCEP(cep)

    // 3. Verifica se chamou a URL certa
    expect(api.get).toHaveBeenCalledWith(`/enderecos/externo/${cep}`)
    // Verifica se retornou os dados tratados (extract)
    expect(resultado).toEqual(mockData.data)
  })

  it('cadastrarEndereco deve chamar o endpoint POST com o payload', async () => {
    // 1. Prepara
    const mockResponse = { data: { id: 99 } }
    const payload = { numero: '10', endereco: { cep: '123' } }
    api.post.mockResolvedValue(mockResponse)

    // 2. Executa
    const resultado = await cadastrarEndereco(payload)

    // 3. Verifica
    expect(api.post).toHaveBeenCalledWith('/enderecos/cadastrar', payload)
    expect(resultado).toEqual(mockResponse.data)
  })
})