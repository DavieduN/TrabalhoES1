import axios from 'axios';

const api = axios.create({
  baseURL: '',
});

export const SistemaService = {
  // Estoque
  
  // GET /aluguel/tipos-equipamento
  listarTipos: async () => {
    return (await api.get('/aluguel/tipos-equipamento')).data;
  },
  
  // POST /aluguel/tipos-equipamento/cadastrar
  cadastrarTipo: async (payload) => {
    return await api.post('/aluguel/tipos-equipamento/cadastrar', payload);
  },
  
  // GET /aluguel/equipamentos
  listarEquipamentos: async () => {
    return (await api.get('/aluguel/equipamentos')).data;
  },
  
  // POST /aluguel/equipamentos/cadastrar
  cadastrarEquipamento: async (payload) => {
    return await api.post('/aluguel/equipamentos/cadastrar', payload);
  },

  // Clientes
  
  // GET /endereco/enderecos/externo/{cep} 
  buscarCep: async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    return (await api.get(`/endereco/enderecos/externo/${cepLimpo}`)).data; 
  },

  // GET /ufs
  listarUfs: async () => {
    return (await api.get('/endereco/ufs')).data;
  },

  // GET /tipos-logradouro
  listarTiposLogradouro: async () => {
    return (await api.get('/endereco/tipos-logradouro')).data;
  },

  // POST /aluguel/clientes/cadastrar
  cadastrarCliente: async (payload) => {
    return await api.post('/aluguel/clientes/cadastrar', payload);
  },
  
  // POST /aluguel/clientes/buscar-id
  buscarClientePorId: async (id) => {
    return (await api.post('/aluguel/clientes/buscar-id', { idPessoa: id })).data;
  },

  // POST /aluguel/clientes/buscar-cpf
  buscarClientePorCpf: async (cpf) => {
    return (await api.post('/aluguel/clientes/buscar-cpf', { cpf })).data;
  },

  // Aluguel
  
  // POST /aluguel/aluguel/registrar
  registrarAluguel: async (payload) => {
    return await api.post('/aluguel/aluguel/registrar', payload);
  },
  
  // GET /aluguel/aluguel/consultar
  listarAlugueis: async () => {
    return (await api.get('/aluguel/aluguel/consultar')).data;
  },

  // POST /aluguel/aluguel/buscar-numero
  buscarAluguelPorNumero: async (nroAluguel) => {
    return (await api.post('/aluguel/aluguel/buscar-numero', { nroAluguel })).data;
  }
};