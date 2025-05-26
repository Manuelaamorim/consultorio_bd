package com.consultorio.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
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

    // Consultas detalhadas no mês atual, ordenadas cronologicamente
    public List<Map<String, Object>> getConsultasMesDetalhadas(int dentistaId) {
        String sql = "SELECT p.nome, c.data, c.horario_inicio, c.horario_termino " +
                "FROM consulta c " +
                "JOIN paciente p ON c.id_paciente = p.id " +
                "WHERE c.id_dentista = ? " +
                "AND MONTH(c.data) = MONTH(CURDATE()) " +
                "AND YEAR(c.data) = YEAR(CURDATE()) " +
                "ORDER BY c.data ASC, c.horario_inicio ASC";
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

    public BigDecimal getFaturamentoAnual(int dentistaId, int ano) {
        String sql = """
        SELECT SUM(p.valor) 
        FROM consulta c
        JOIN consulta_procedimento cp ON c.id = cp.consulta_id
        JOIN procedimento p ON cp.procedimento_codigo = p.codigo
        WHERE c.id_dentista = ? 
          AND YEAR(c.data) = ? 
          AND LOWER(c.status_pagamento) = 'pago'
    """;
        BigDecimal total = jdbcTemplate.queryForObject(sql, BigDecimal.class, dentistaId, ano);
        return total != null ? total : BigDecimal.ZERO;
    }

    public int getPacientesAtendidos(int dentistaId, int ano) {
        String sql = """
        SELECT COUNT(DISTINCT c.id_paciente) 
        FROM consulta c
        WHERE c.id_dentista = ? 
          AND YEAR(c.data) = ? 
          AND c.data <= CURDATE()
    """;
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, dentistaId, ano);
        return total != null ? total : 0;
    }

    public int getTotalConsultas(int dentistaId, int ano) {
        String sql = """
        SELECT COUNT(*) 
        FROM consulta 
        WHERE id_dentista = ? 
          AND YEAR(data) = ?
    """;
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, dentistaId, ano);
        return total != null ? total : 0;
    }


    public List<BigDecimal> getFaturamentoMensalAcumulado(int dentistaId, int ano) {
        String sql = """
        SELECT MONTH(c.data) AS mes, SUM(p.valor) AS total
        FROM consulta c
        JOIN consulta_procedimento cp ON c.id = cp.consulta_id
        JOIN procedimento p ON cp.procedimento_codigo = p.codigo
        WHERE c.id_dentista = ? 
          AND YEAR(c.data) = ?
          AND LOWER(c.status_pagamento) = 'pago'
        GROUP BY mes
    """;

        List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql, dentistaId, ano);

        BigDecimal[] faturamentoPorMes = new BigDecimal[12];
        Arrays.fill(faturamentoPorMes, BigDecimal.ZERO);

        for (Map<String, Object> row : resultados) {
            int mes = ((Number) row.get("mes")).intValue();
            BigDecimal total = (BigDecimal) row.get("total");
            faturamentoPorMes[mes - 1] = total != null ? total : BigDecimal.ZERO;
        }

        // Acumular
        for (int i = 1; i < 12; i++) {
            faturamentoPorMes[i] = faturamentoPorMes[i].add(faturamentoPorMes[i - 1]);
        }

        return Arrays.asList(faturamentoPorMes);
    }


    public int getConsultasPendentes(int dentistaId) {
        String sql = """
        SELECT COUNT(*) 
        FROM consulta 
        WHERE id_dentista = ? AND LOWER(status_pagamento) = 'pendente'
    """;
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, dentistaId);
        return total != null ? total : 0;
    }

    public double getTempoMedioConsultas(int dentistaId) {
        String sql = """
        SELECT AVG(TIMESTAMPDIFF(MINUTE, horario_inicio, horario_termino)) 
        FROM consulta 
        WHERE id_dentista = ?
          AND horario_inicio IS NOT NULL
          AND horario_termino IS NOT NULL
    """;
        Double media = jdbcTemplate.queryForObject(sql, Double.class, dentistaId);
        return media != null ? media : 0;
    }


    public Map<String, Object> getProcedimentoMaisRealizado(int dentistaId) {
        String sql = """
        SELECT p.nome, COUNT(*) AS qtd
        FROM consulta c
        JOIN consulta_procedimento cp ON c.id = cp.consulta_id
        JOIN procedimento p ON cp.procedimento_codigo = p.codigo
        WHERE c.id_dentista = ?
        GROUP BY p.nome
        ORDER BY qtd DESC
        LIMIT 1
    """;

        List<Map<String, Object>> resultado = jdbcTemplate.queryForList(sql, dentistaId);
        if (resultado.isEmpty()) {
            return Map.of("nome", "-", "qtd", 0);
        } else {
            return resultado.get(0);
        }
    }


    public double getIdadeMediaPacientes(int dentistaId) {
        String sql = """
        SELECT AVG(TIMESTAMPDIFF(YEAR, p.data_nascimento, CURDATE()))
        FROM paciente p
        JOIN consulta c ON p.id = c.id_paciente
        WHERE c.id_dentista = ?
    """;

        Double media = jdbcTemplate.queryForObject(sql, Double.class, dentistaId);
        return media != null ? media : 0;
    }


}
