package com.consultorio.dao;

import com.consultorio.models.Telefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TelefoneDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Telefone telefone) {
        String sql = "INSERT INTO telefone (numero, pessoa_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, telefone.getNumero(), telefone.getPessoaId());
    }
}
