package com.consultorio.dao;

import com.consultorio.models.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public List<Consulta> listarPorCpfDentista(String cpfDentista) {
        String sql = """
        SELECT c.* FROM consulta c
        JOIN dentista d ON c.id_dentista = d.id
        WHERE d.cpf = ?
        """;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class), cpfDentista);
    }

    public List<Consulta> listarPorPacienteId(int idPaciente) {
        String sql = "SELECT * FROM consulta WHERE id_paciente = ? ORDER BY data DESC, horario_inicio DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class), idPaciente);
    }
    // Atualizar uma consulta
    public void atualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET ";

        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (consulta.getData() != null) {
            updates.add("data = ?");
            params.add(consulta.getData());
        }
        if (consulta.getHorarioInicio() != null) {
            updates.add("horario_inicio = ?");
            params.add(consulta.getHorarioInicio());
        }
        if (consulta.getHorarioTermino() != null) {
            updates.add("horario_termino = ?");
            params.add(consulta.getHorarioTermino());
        }
        if (consulta.getStatusPagamento() != null && !consulta.getStatusPagamento().isEmpty()) {
            updates.add("status_pagamento = ?");
            params.add(consulta.getStatusPagamento());
        }
        if (consulta.getMetodoPagamento() != null && !consulta.getMetodoPagamento().isEmpty()) {
            updates.add("metodo_pagamento = ?");
            params.add(consulta.getMetodoPagamento());
        }
        if (consulta.getIdPaciente() != 0) {
            updates.add("id_paciente = ?");
            params.add(consulta.getIdPaciente());
        }
        if (consulta.getIdDentista() != 0) {
            updates.add("id_dentista = ?");
            params.add(consulta.getIdDentista());
        }

        if (updates.isEmpty()) {
            // Nenhum campo para atualizar
            return;
        }

        sql += String.join(", ", updates) + " WHERE id = ?";
        params.add(consulta.getId());

        jdbcTemplate.update(sql, params.toArray());
    }


    // Buscar uma consulta pelo ID
    public Consulta buscarPorId(int id) {
        String sql = "SELECT * FROM consulta WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Consulta.class), id);
    }

    // Excluir uma consulta
    public void excluir(int id) {
        String sql = "DELETE FROM consulta WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
