package com.consultorio.dao;

import com.consultorio.models.Dentista;
import com.consultorio.models.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DentistaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FuncionarioDAO funcionarioDAO;

    public void salvar(Dentista dentista) {
        // Verificar se a dataNascimento da pessoa (herdada de Pessoa) não é nula
        if (dentista.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }

        // Salvar a Pessoa associada ao Funcionario
        String sqlPessoa = "INSERT INTO pessoa (cpf, nome, email, data_nascimento) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlPessoa,
                dentista.getCpf(),  // Vem de Pessoa (herança)
                dentista.getNome(), // Vem de Pessoa (herança)
                dentista.getEmail(), // Vem de Pessoa (herança)
                java.sql.Date.valueOf(dentista.getDataNascimento()));  // Vem de Pessoa (herança)

        Long pessoaId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);  // Obtém o ID da Pessoa

        // Salvar o Funcionario, que é necessário para criar um Dentista
        Funcionario funcionario = new Funcionario();
        funcionario.setId(pessoaId);  // Associa o ID da Pessoa ao Funcionario diretamente
        funcionarioDAO.salvar(funcionario);

        Long funcionarioId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);  // Obtém o ID do Funcionario

        // Salvar o Dentista, incluindo os dados específicos
        String sqlDentista = "INSERT INTO dentista (funcionario_id, cro, especialidade) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlDentista,
                funcionarioId,
                dentista.getCro(),
                dentista.getEspecialidade());
    }

    public void deletarPorCpf(String cpf) {
        String sql = "DELETE d " +
                "FROM dentista d " +
                "JOIN funcionario f ON d.funcionario_id = f.id " +
                "JOIN pessoa p ON f.pessoa_id = p.id " +
                "WHERE p.cpf = ?";

        jdbcTemplate.update(sql, cpf);
    }
}
