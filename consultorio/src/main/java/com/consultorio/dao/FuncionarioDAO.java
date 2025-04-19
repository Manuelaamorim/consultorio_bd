package com.consultorio.dao;

import com.consultorio.models.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FuncionarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (pessoa_id) VALUES (?)";
        jdbcTemplate.update(sql, funcionario.getId());

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public Long getUltimoId() {
        String sql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
