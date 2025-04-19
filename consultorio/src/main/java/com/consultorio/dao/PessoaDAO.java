package com.consultorio.dao;

import com.consultorio.models.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;

@Repository
public class PessoaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long salvar(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (cpf, nome, email, data_nascimento) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, pessoa.getCpf());
            ps.setString(2, pessoa.getNome());
            ps.setString(3, pessoa.getEmail());
            ps.setDate(4, Date.valueOf(pessoa.getDataNascimento()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
