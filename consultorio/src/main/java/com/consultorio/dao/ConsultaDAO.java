package com.consultorio.dao;

import com.consultorio.models.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsultaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consulta (data, horario_inicio, horario_termino, status_pagamento, metodo_pagamento, id_paciente, id_dentista) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                consulta.getData(),
                consulta.getHorarioInicio(),
                consulta.getHorarioTermino(),
                consulta.getStatusPagamento(),
                consulta.getMetodoPagamento(),
                consulta.getIdPaciente(),
                consulta.getIdDentista()
        );
    }

    public List<Consulta> listarTodas() {
        String sql = "SELECT * FROM consulta";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class));
    }
}
