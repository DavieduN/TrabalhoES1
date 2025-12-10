import axios from "axios";

export const api = axios.create({ 
  baseURL: "/api",
  timeout: 8000,
});


api.interceptors.response.use(
  (res) => res,
  (err) => {
    const backendMessage =
      err?.response?.data?.erro ||
      err?.response?.data?.message ||
      err?.message ||
      "Erro inesperado no servidor ou na rede.";

    err.message = backendMessage;
    return Promise.reject(err);
  }
);

const extract = (res) => res?.data ?? null;
const extractArray = (res) => (Array.isArray(res?.data) ? res.data : []);

export const listarUFs = () => api.get("/ufs").then(extractArray);
export const listarCidades = () => api.get("/cidades").then(extractArray);
export const listarBairros = () => api.get("/bairros").then(extractArray);
export const listarTiposLogradouro = () => api.get("/tipos-logradouro").then(extractArray);
export const listarLogradouros = () => api.get("/logradouros").then(extractArray);


export const consultarExternoPorCEP = (cep) => 
  api.get(`/enderecos/externo/${cep}`).then(extract);


export const consultarLocalPorCEP = (cep) => 
  api.get(`/enderecos/cep/${cep}`).then(extractArray);


export const buscarEnderecoPorID = (id) =>
  api
    .post("/enderecos/buscar-id", { idEnderecoEspecifico: id })
    .then(extract);


export const buscarCidadePorID = (id) =>
  api
    .post("/cidades/buscar-id", { idCidade: id })
    .then(extract);

export const cadastrarEndereco = (payload) =>
  api.post("/enderecos/cadastrar", payload).then(extract);

export default {
  listarUFs,
  listarCidades,
  listarBairros,
  listarTiposLogradouro,
  listarLogradouros,
  consultarExternoPorCEP,
  consultarLocalPorCEP,
  buscarEnderecoPorID,
  buscarCidadePorID,
  cadastrarEndereco,
};