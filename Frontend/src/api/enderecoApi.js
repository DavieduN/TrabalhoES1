import axios from "axios";

// -------------------------------------------------------------
// CONFIGURAÇÃO BÁSICA
// O baseURL é relativo ("/api") para usar o Proxy do Vite
// -------------------------------------------------------------
export const api = axios.create({ 
  baseURL: "/api",
  timeout: 8000,
});

// -------------------------------------------------------------
// INTERCEPTOR DE ERROS
// Padroniza a mensagem de erro vinda do Servlet/Backend
// -------------------------------------------------------------
api.interceptors.response.use(
  (res) => res,
  (err) => {
    const backendMessage =
      err?.response?.data?.erro ||
      err?.response?.data?.message ||
      err?.message ||
      "Erro inesperado no servidor ou na rede.";

    // Injeta a mensagem tratada no erro para fácil acesso no frontend
    err.message = backendMessage;
    return Promise.reject(err);
  }
);

// -------------------------------------------------------------
// HELPERS (Para extrair dados do Axios)
// -------------------------------------------------------------
const extract = (res) => res?.data ?? null;
const extractArray = (res) => (Array.isArray(res?.data) ? res.data : []);

// -------------------------------------------------------------
// SERVIÇOS DE DOMÍNIO (Listagens Auxiliares)
// Geralmente mantidos como GET pois não possuem parâmetros complexos
// -------------------------------------------------------------
export const listarUFs = () => api.get("/ufs").then(extractArray);
export const listarCidades = () => api.get("/cidades").then(extractArray);
export const listarBairros = () => api.get("/bairros").then(extractArray);
export const listarTiposLogradouro = () => api.get("/tipos-logradouro").then(extractArray);
export const listarLogradouros = () => api.get("/logradouros").then(extractArray);

// -------------------------------------------------------------
// SERVIÇOS DE NEGÓCIO - ENDEREÇO (N-Camadas / RPC Style)
// -------------------------------------------------------------

/**
 * Serviço: obterEnderecoExterno
 * Consulta API externa (ViaCEP) através do backend.
 */
export const consultarExternoPorCEP = (cep) => 
  api.get(`/enderecos/externo/${cep}`).then(extract);

/**
 * Serviço: obterEnderecoPorCEP (Local)
 * Retorna lista de endereços do banco local dado um CEP.
 */
export const consultarLocalPorCEP = (cep) => 
  api.get(`/enderecos/cep/${cep}`).then(extractArray);


// --- MUDANÇA IMPORTANTE ABAIXO ---
// Adaptado para o padrão N-Camadas (Action/Command):
// Usa POST e envia o ID no corpo (Body), ao invés de GET na URL.

/**
 * Serviço: obterEnderecoPorID
 * @param {number|string} id - ID do endereço específico
 */
export const buscarEnderecoPorID = (id) =>
  api
    .post("/enderecos/buscar-id", { idEnderecoEspecifico: id })
    .then(extract);

/**
 * Serviço: obterCidade (por ID)
 * @param {number|string} id - ID da cidade
 */
export const buscarCidadePorID = (id) =>
  api
    .post("/cidades/buscar-id", { idCidade: id })
    .then(extract);

/**
 * Serviço: cadastrarEndereco
 * Envia o objeto complexo aninhado para persistência.
 */
export const cadastrarEndereco = (payload) =>
  api.post("/enderecos/cadastrar", payload).then(extract);

// -------------------------------------------------------------
// EXPORT DEFAULT
// -------------------------------------------------------------
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