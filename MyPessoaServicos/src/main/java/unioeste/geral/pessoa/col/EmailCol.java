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
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public EmailCol(EmailDAO dao) {
        this.dao = dao;
    }

    public void salvarEmails(Connection con, List<Email> emails, int idPessoa) throws Exception {
        if (emails == null || emails.isEmpty()) return;
        if (idPessoa <= 0) throw new Exception("ID de pessoa inválido.");

        Set<String> processados = new HashSet<>();
        for (Email email : emails) {
            validarEmail(email);
            if (processados.contains(email.getEnderecoEmail().toLowerCase())) continue;

            dao.inserir(con, email, idPessoa);
            processados.add(email.getEnderecoEmail().toLowerCase());
        }
    }

    public List<Email> buscarPorPessoa(Connection con, int idPessoa) throws Exception {
        return dao.buscarPorPessoa(con, idPessoa);
    }

    private void validarEmail(Email email) throws Exception {
        if (email == null || email.getEnderecoEmail() == null) throw new Exception("Email vazio.");
        if (!EMAIL_PATTERN.matcher(email.getEnderecoEmail()).matches()) {
            throw new Exception("Formato de email inválido.");
        }
        email.setEnderecoEmail(email.getEnderecoEmail().trim());
    }
}