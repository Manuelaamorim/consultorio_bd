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
        String sql = "INSERT INTO Paciente (CPF, nome, email, telefone, rua, numero, bairro, cidade, data_nascimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                paciente.getCpf(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getRua(),
                paciente.getNumero(),
                paciente.getBairro(),
                paciente.getCidade(),
                paciente.getDataNascimento().toString()
        );
    }
}
