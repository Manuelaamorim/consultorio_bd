package com.consultorio.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. Próximas consultas do dia
    public List<Map<String, Object>> getConsultasHoje(int dentistaId) {
        String sql = "SELECT c.id, p.nome, c.data, c.horario_inicio, c.horario_termino " +
                "FROM consulta c " +
                "JOIN paciente p ON c.id_paciente = p.id " +
                "WHERE c.id_dentista = ? AND c.data = CURDATE()";
        return jdbcTemplate.queryForList(sql, dentistaId);
    }

    // consultas no mes, comparativo e percentual de crescimento em relacao ao ano anterior
    public List<Map<String, Object>> getConsultasPorMes(int dentistaId, int ano) {
        String sql = "SELECT MONTH(data) AS mes, COUNT(*) AS total " +
                "FROM consulta " +
                "WHERE id_dentista = ? AND YEAR(data) = ? " +
                "GROUP BY mes ORDER BY mes";
        return jdbcTemplate.queryForList(sql, dentistaId, ano);
    }

    public Double getMediaConsultas(int dentistaId, int ano, int ateMes) {
        String sql = "SELECT AVG(mensal.total) FROM (" +
                " SELECT COUNT(*) AS total FROM consulta " +
                " WHERE id_dentista = ? AND YEAR(data) = ? AND MONTH(data) <= ? " +
                " GROUP BY MONTH(data)) AS mensal";
        return jdbcTemplate.queryForObject(sql, Double.class, dentistaId, ano, ateMes);
    }


    // grafico pizza consultas pagas e pendentes
    public List<Map<String, Object>> getStatusPagamentoPorPeriodo(int dentistaId, String periodo) {
        String sqlBase = "SELECT status_pagamento, COUNT(*) AS total FROM consulta WHERE id_dentista = ? ";

        switch (periodo.toLowerCase()) {
            case "dia":
                sqlBase += "AND data = CURDATE() ";
                break;
            case "mes":
                sqlBase += "AND MONTH(data) = MONTH(CURDATE()) AND YEAR(data) = YEAR(CURDATE()) ";
                break;
            case "ano":
                sqlBase += "AND YEAR(data) = YEAR(CURDATE()) ";
                break;
            case "geral":
            default:
                // sem filtro de data
                break;
        }

        sqlBase += "GROUP BY status_pagamento";

        return jdbcTemplate.queryForList(sqlBase, dentistaId);
    }

}
