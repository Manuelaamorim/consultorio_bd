package com.consultorio.dao;

import com.consultorio.models.Auxiliar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuxiliarDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Auxiliar auxiliar) {
        String sql = "INSERT INTO auxiliar (nome, cpf, telefone, telefone2, email, rua, numero, bairro, cidade, data_nascimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                auxiliar.getNome(),
                auxiliar.getCpf(),
                auxiliar.getTelefone(),
                auxiliar.getTelefone2(),
                auxiliar.getEmail(),
                auxiliar.getRua(),
                auxiliar.getNumero(),
                auxiliar.getBairro(),
                auxiliar.getCidade(),
                auxiliar.getDataNascimento()
        );
    }

    public List<Auxiliar> listarAuxiliares() {
        String sql = "SELECT * FROM auxiliar";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Auxiliar.class));
    }

    public Auxiliar buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM auxiliar WHERE cpf = ?";
        List<Auxiliar> resultado = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Auxiliar.class), cpf);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public Auxiliar buscarPorCpfEEmail(String cpf, String email) {
        String sql = "SELECT * FROM auxiliar WHERE cpf = ? AND email = ?";
        List<Auxiliar> auxiliares = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Auxiliar.class), cpf, email);
        return auxiliares.isEmpty() ? null : auxiliares.get(0);
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM auxiliar WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public void deletarPorCpf(String cpf) {
        String sql = "DELETE FROM auxiliar WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }

    public void atualizar(Auxiliar auxiliar) {
        String sql = "UPDATE auxiliar SET nome = ?, telefone = ?, telefone2 = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, data_nascimento = ? WHERE cpf = ?";
        jdbcTemplate.update(sql,
                auxiliar.getNome(),
                auxiliar.getTelefone(),
                auxiliar.getTelefone2(),
                auxiliar.getEmail(),
                auxiliar.getRua(),
                auxiliar.getNumero(),
                auxiliar.getBairro(),
                auxiliar.getCidade(),
                auxiliar.getDataNascimento(),
                auxiliar.getCpf());
    }
}