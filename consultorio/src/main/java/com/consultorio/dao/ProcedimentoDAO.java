package com.consultorio.dao;

import com.consultorio.models.Procedimento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProcedimentoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Procedimento procedimento) {
        String sql = "INSERT INTO procedimento (codigo, nome, pos_operatorio, valor) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, procedimento.getCodigo(), procedimento.getNome(), procedimento.getPos_operatorio(), procedimento.getValor());
    }

    public List<Procedimento> listarTodos() {
        String sql = "SELECT * FROM procedimento";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Procedimento.class));
    }
}
