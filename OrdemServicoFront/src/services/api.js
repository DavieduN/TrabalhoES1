import axios from 'axios';

const api = axios.create({
  baseURL: '', 
});

export const SistemaService = {
  
  // GET /os/tipos-servico
  listarTiposServico: async () => {
    return (await api.get('/os/tipos-servico')).data;
  },
  
  cadastrarTipoServico: async (payload) => {
    return await api.post('/os/tipos-servico/cadastrar', payload);
  },
  
  // POST /os/atendentes/cadastrar
  cadastrarAtendente: async (payload) => {
    return await api.post('/os/atendentes/cadastrar', payload);
  },
  
  // POST /os/atendentes/buscar-id
  buscarAtendentePorId: async (id) => {
    return (await api.post('/os/atendentes/buscar-id', { idPessoa: id })).data;
  },

  // POST /os/atendentes/buscar-cpf
  buscarAtendentePorCpf: async (cpf) => {
    return (await api.post('/os/atendentes/buscar-cpf', { cpf })).data;
  },
  
  cadastrarCliente: async (payload) => await api.post('/os/clientes/cadastrar', payload),
  buscarClientePorId: async (id) => (await api.post('/os/clientes/buscar-id', { idPessoa: id })).data,
  buscarClientePorCpf: async (cpf) => (await api.post('/os/clientes/buscar-cpf', { cpf })).data,

  
  // POST /os/ordem-servico/registrar
  registrarOS: async (payload) => {
    return await api.post('/os/ordem-servico/registrar', payload);
  },
  
  // GET /os/ordem-servico/consultar
  listarOSs: async () => {
    return (await api.get('/os/ordem-servico/consultar')).data;
  },

  // POST /os/ordem-servico/buscar-numero
  buscarOSPorNumero: async (nro) => {
    return (await api.post('/os/ordem-servico/buscar-numero', { nroOrdemServico: nro })).data;
  },

  
  buscarCep: async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    return (await api.get(`/endereco/enderecos/externo/${cepLimpo}`)).data; 
  },
  listarUfs: async () => (await api.get('/endereco/ufs')).data,
  listarTiposLogradouro: async () => (await api.get('/endereco/tipos-logradouro')).data,
};