package com.consultorio.controllers;

import com.consultorio.dao.DashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardDAO dashboardDAO;

    // 1. Consultas de hoje
    @GetMapping("/consultas-hoje")
    public List<Map<String, Object>> consultasHoje(@RequestParam int dentistaId) {
        return dashboardDAO.getConsultasHoje(dentistaId);
    }

    // 2. Consultas no mês
    @GetMapping("/consultas-por-mes")
    public List<Map<String, Object>> consultasPorMes(
            @RequestParam int dentistaId,
            @RequestParam int ano) {
        return dashboardDAO.getConsultasPorMes(dentistaId, ano);
    }

    @GetMapping("/comparativo")
    public Map<String, Object> comparativo(
            @RequestParam int dentistaId,
            @RequestParam int ano) {

        int mesAtual = LocalDate.now().getMonthValue();

        Double mediaAtual = dashboardDAO.getMediaConsultas(dentistaId, ano, mesAtual);
        Double mediaAnterior = dashboardDAO.getMediaConsultas(dentistaId, ano - 1, mesAtual);

        double variacao = 0.0;
        if (mediaAnterior != null && mediaAnterior != 0) {
            variacao = ((mediaAtual - mediaAnterior) / mediaAnterior) * 100;
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mediaAtual", mediaAtual);
        resultado.put("mediaAnterior", mediaAnterior);
        resultado.put("variacao", variacao);

        return resultado;
    }


    // 3. Status de pagamento
    @GetMapping("/status-pagamento")
    public List<Map<String, Object>> statusPagamentoPorPeriodo(
            @RequestParam int dentistaId,
            @RequestParam(defaultValue = "geral") String periodo) {
        return dashboardDAO.getStatusPagamentoPorPeriodo(dentistaId, periodo);
    }

}
