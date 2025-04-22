package com.consultorio.dao;

import com.consultorio.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class PacienteDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Paciente paciente) {
        String sql = "INSERT INTO Paciente (CPF, nome, email, telefone, telefone2, rua, numero, bairro, cidade, data_nascimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                paciente.getCpf(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getTelefone2(),
                paciente.getRua(),
                paciente.getNumero(),
                paciente.getBairro(),
                paciente.getCidade(),
                paciente.getDataNascimento()
        );
    }

    public void deletarPorCpf(String cpf){
        String sql = "DELETE FROM paciente WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }

    public List<Paciente> listarPacientes() {
        String sql = "SELECT * FROM paciente";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Paciente.class));
    }

    public Paciente buscarPorCpf(String cpf){
        String sql = "SELECT * FROM paciente WHERE cpf = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Paciente.class), cpf);
    }

    public void atualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nome = ?, email = ?, telefone = ?, telefone2 = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, data_nascimento = ? WHERE cpf = ?";
        jdbcTemplate.update(sql,
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getTelefone2(),
                paciente.getRua(),
                paciente.getNumero(),
                paciente.getBairro(),
                paciente.getCidade(),
                paciente.getDataNascimento(),
                paciente.getCpf());
    }
}
