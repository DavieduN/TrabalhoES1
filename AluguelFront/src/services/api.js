import axios from 'axios';

const api = axios.create({
  baseURL: '',
});

export const SistemaService = {
  // Estoque
  
  // GET /api/tipos-equipamento
  listarTipos: async () => {
    return (await api.get('/api/tipos-equipamento')).data;
  },
  
  // POST /api/tipos-equipamento/cadastrar
  cadastrarTipo: async (payload) => {
    return await api.post('/api/tipos-equipamento/cadastrar', payload);
  },
  
  // GET /api/equipamentos
  listarEquipamentos: async () => {
    return (await api.get('/api/equipamentos')).data;
  },
  
  // POST /api/equipamentos/cadastrar
  cadastrarEquipamento: async (payload) => {
    return await api.post('/api/equipamentos/cadastrar', payload);
  },

  // Clientes
  
  // GET /enderecos/externo/{cep} 
  buscarCep: async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    return (await api.get(`/enderecos/externo/${cepLimpo}`)).data; 
  },

  // GET /ufs
  listarUfs: async () => {
    return (await api.get('/endereco/ufs')).data;
  },

  // GET /tipos-logradouro
  listarTiposLogradouro: async () => {
    return (await api.get('/endereco/tipos-logradouro')).data;
  },

  // POST /api/clientes/cadastrar
  cadastrarCliente: async (payload) => {
    return await api.post('/api/clientes/cadastrar', payload);
  },
  
  // POST /api/clientes/buscar-id
  buscarClientePorId: async (id) => {
    return (await api.post('/api/clientes/buscar-id', { idPessoa: id })).data;
  },

  // POST /api/clientes/buscar-cpf
  buscarClientePorCpf: async (cpf) => {
    return (await api.post('/api/clientes/buscar-cpf', { cpf })).data;
  },

  // Aluguel
  
  // POST /api/aluguel/registrar
  registrarAluguel: async (payload) => {
    return await api.post('/api/aluguel/registrar', payload);
  },
  
  // GET /api/aluguel/consultar
  listarAlugueis: async () => {
    return (await api.get('/api/aluguel/consultar')).data;
  },

  // POST /api/aluguel/buscar-numero
  buscarAluguelPorNumero: async (nroAluguel) => {
    return (await api.post('/api/aluguel/buscar-numero', { nroAluguel })).data;
  }
};