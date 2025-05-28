package com.consultorio.dao;

import com.consultorio.models.Consulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ConsultaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int salvar(Consulta consulta) {
        // Converte List<String> procedimentos para String separada por vírgula
        String procedimentosConcatenados = null;
        if (consulta.getProcedimentos() != null && !consulta.getProcedimentos().isEmpty()) {
            procedimentosConcatenados = String.join(",", consulta.getProcedimentos());
        } else {
            procedimentosConcatenados = ""; // ou NULL, conforme preferir
        }

        String sql = "CALL registrar_consulta(?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                consulta.getData(),
                consulta.getHorarioInicio(),
                consulta.getHorarioTermino(),
                consulta.getStatusPagamento(),
                consulta.getMetodoPagamento(),
                consulta.getIdPaciente(),
                consulta.getIdDentista(),
                procedimentosConcatenados
        );

        // Recupera o último ID inserido da consulta (pode ser feito também via OUT param na procedure)
        Integer consultaId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        return consultaId;
    }


    // ✅ Listar todas as consultas
    public List<Consulta> listarTodas() {
        String sql = "SELECT * FROM consulta";
        List<Consulta> consultas = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class));
        for (Consulta c : consultas) {
            c.setProcedimentos(buscarProcedimentosPorConsulta(c.getId()));
            c.setValorConsulta(calcularValorConsulta(c.getId()));
        }
        return consultas;
    }

    // ✅ Listar por CPF do dentista
    public List<Consulta> listarPorCpfDentista(String cpfDentista) {
        String sql = """
        SELECT c.* FROM consulta c
        JOIN dentista d ON c.id_dentista = d.id
        WHERE d.cpf = ?
        """;

        List<Consulta> consultas = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class), cpfDentista);
        for (Consulta c : consultas) {
            c.setProcedimentos(buscarProcedimentosPorConsulta(c.getId()));
            c.setValorConsulta(calcularValorConsulta(c.getId()));
        }
        return consultas;
    }

    // ✅ Listar por paciente
    public List<Consulta> listarPorPacienteId(int idPaciente) {
        String sql = "SELECT * FROM consulta WHERE id_paciente = ? ORDER BY data DESC, horario_inicio DESC";
        List<Consulta> consultas = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Consulta.class), idPaciente);
        for (Consulta c : consultas) {
            c.setProcedimentos(buscarProcedimentosPorConsulta(c.getId()));
            c.setValorConsulta(calcularValorConsulta(c.getId()));
        }
        return consultas;
    }

    // ✅ Atualizar consulta (sem alterar procedimentos)
    public void atualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET data = ?, horario_inicio = ?, horario_termino = ?, " +
                "status_pagamento = ?, metodo_pagamento = ?, id_paciente = ?, id_dentista = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                consulta.getData(),
                consulta.getHorarioInicio(),
                consulta.getHorarioTermino(),
                consulta.getStatusPagamento(),
                consulta.getMetodoPagamento(),
                consulta.getIdPaciente(),
                consulta.getIdDentista(),
                consulta.getId()
        );

        // Atualizar procedimentos: remove e insere novamente
        jdbcTemplate.update("DELETE FROM consulta_procedimento WHERE consulta_id = ?", consulta.getId());
        for (String codigo : consulta.getProcedimentos()) {
            jdbcTemplate.update("INSERT INTO consulta_procedimento (consulta_id, procedimento_codigo) VALUES (?, ?)",
                    consulta.getId(), codigo);
        }
    }

    // ✅ Buscar consulta por ID
    public Consulta buscarPorId(int id) {
        String sql = "SELECT * FROM consulta WHERE id = ?";
        Consulta consulta = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Consulta.class), id);
        consulta.setProcedimentos(buscarProcedimentosPorConsulta(id));
        consulta.setValorConsulta(calcularValorConsulta(id));
        return consulta;
    }

    // ✅ Excluir consulta (ON DELETE CASCADE cuida dos procedimentos)
    public void excluir(int id) {
        jdbcTemplate.update("DELETE FROM consulta WHERE id = ?", id);
    }

    // ✅ Buscar procedimentos associados à consulta
    private List<String> buscarProcedimentosPorConsulta(int consultaId) {
        String sql = "SELECT procedimento_codigo FROM consulta_procedimento WHERE consulta_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, consultaId);
        List<String> codigos = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            codigos.add((String) row.get("procedimento_codigo"));
        }
        return codigos;
    }

    // ✅ Calcular valor total da consulta somando valores dos procedimentos
    private BigDecimal calcularValorConsulta(int consultaId) {
        String sql = """
            SELECT SUM(p.valor) AS total 
            FROM consulta_procedimento cp
            JOIN procedimento p ON cp.procedimento_codigo = p.codigo
            WHERE cp.consulta_id = ?
            """;
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class, consultaId);
        return total != null ? total : BigDecimal.ZERO;
    }

}
