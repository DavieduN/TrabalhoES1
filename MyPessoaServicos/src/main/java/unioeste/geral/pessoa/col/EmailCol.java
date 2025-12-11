package unioeste.geral.pessoa.col;

import unioeste.geral.pessoa.bo.Email;
import unioeste.geral.pessoa.dao.EmailDAO;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class EmailCol {

    private final EmailDAO dao;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public EmailCol() {
        this.dao = new EmailDAO();
    }

    public void salvarEmails(Connection con, List<Email> emails, int idPessoa) throws Exception {
        if (emails == null || emails.isEmpty()) {
            return;
        }

        if (idPessoa <= 0) {
            throw new Exception("Erro de integridade: Tentativa de salvar e-mail sem ID de pessoa vinculado.");
        }

        Set<String> emailsProcessados = new HashSet<>();

        for (Email email : emails) {
            validarEmail(email);
            if (emailsProcessados.contains(email.getEnderecoEmail().toLowerCase())) {
                continue;
            }
            // se for permitir adicionar emails depois do cadastro verificar se esse email já está associado
            // se nao permitir que dois usuarios tenham o mesmo email tem que testar aqui também
            // ou sera que da problema de concorrencia?

            dao.inserir(con, email, idPessoa);

            emailsProcessados.add(email.getEnderecoEmail().toLowerCase());
        }
    }

    public List<Email> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        if (idPessoa <= 0) {
            throw new Exception("ID de pessoa inválido para busca de e-mails.");
        }
        return dao.buscarPorPessoa(con, idPessoa);
    }

    private void validarEmail(Email email) throws Exception {
        if (email == null) {
            throw new Exception("Objeto e-mail não pode ser nulo.");
        }

        String endereco = email.getEnderecoEmail();

        if (endereco == null || endereco.trim().isEmpty()) {
            throw new Exception("O endereço de e-mail não pode estar vazio.");
        }

        if (!EMAIL_PATTERN.matcher(endereco).matches()) {
            throw new Exception("Formato de e-mail inválido: " + endereco);
        }

        email.setEnderecoEmail(endereco.trim());
    }
}