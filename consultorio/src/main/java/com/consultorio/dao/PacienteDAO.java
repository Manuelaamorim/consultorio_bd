package com.consultorio.dao;

import com.consultorio.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PacienteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Paciente paciente) {
        // 1. Salva na tabela pessoa
        String sqlPessoa = "INSERT INTO pessoa (cpf, nome, email, data_nascimento) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlPessoa,
                paciente.getCpf(),
                paciente.getNome(),
                paciente.getEmail(),
                java.sql.Date.valueOf(paciente.getDataNascimento()));

        // 2. Recupera o ID gerado da pessoa
        Long pessoaId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        // 3. Salva na tabela paciente (relacionando com pessoa)
        String sqlPaciente = "INSERT INTO paciente (pessoa_id) VALUES (?)";
        jdbcTemplate.update(sqlPaciente, pessoaId);

        // Telefones e Endereço devem ser salvos separadamente via seus respectivos DAOs
    }

    public void deletarPorCpf(String cpf) {
        String sql = "DELETE p FROM paciente p JOIN pessoa pe ON p.pessoa_id = pe.id WHERE pe.cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }
}
