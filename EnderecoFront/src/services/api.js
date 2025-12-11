import axios from 'axios';

const api = axios.create({
  baseURL: '',
});

export const EnderecoService = {
  getUfs: async () => (await api.get('/endereco/ufs')).data,
  getTiposLogradouro: async () => (await api.get('/endereco/tipos-logradouro')).data,

  consultarCepExterno: async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    return (await api.get(`/endereco/enderecos/externo/${cepLimpo}`)).data;
  },
  consultarCepInterno: async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    return (await api.get(`/endereco/enderecos/cep/${cepLimpo}`)).data;
  },
  buscarEnderecoPorId: async (idEndereco) => {
    return (await api.post('/endereco/enderecos/buscar-id', { idEndereco })).data;
  },
  buscarCidadePorId: async (idCidade) => {
    return (await api.post('/endereco/cidades/buscar-id', { idCidade })).data;
  },
  cadastrarEndereco: async (payload) => {
    return (await api.post('/endereco/enderecos/cadastrar', payload)).data;
  }
};