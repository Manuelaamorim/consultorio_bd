package com.consultorio.controllers;

import com.consultorio.dao.DashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @GetMapping("/consultas-mes")
    public List<Map<String, Object>> consultasMes(@RequestParam int dentistaId) {
        return dashboardDAO.getConsultasMesDetalhadas(dentistaId);
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

    @GetMapping("/faturamento-anual")
    @ResponseBody
    public Map<String, Object> faturamentoAnual(@RequestParam int dentistaId, @RequestParam int ano) {
        BigDecimal total = dashboardDAO.getFaturamentoAnual(dentistaId, ano);
        Map<String, Object> response = new HashMap<>();
        response.put("valor", total);
        return response;
    }

    @GetMapping("/pacientes-atendidos")
    @ResponseBody
    public Map<String, Object> pacientesAtendidos(
            @RequestParam int dentistaId,
            @RequestParam int ano) {
        int total = dashboardDAO.getPacientesAtendidos(dentistaId, ano);
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        return response;
    }

    @GetMapping("/faturamento-mensal-acumulado")
    @ResponseBody
    public Map<String, Object> faturamentoMensal(
            @RequestParam int dentistaId,
            @RequestParam List<Integer> anos) {

        Map<String, List<BigDecimal>> resultado = new HashMap<>();

        for (int ano : anos) {
            List<BigDecimal> faturamentoPorMes = dashboardDAO.getFaturamentoMensalAcumulado(dentistaId, ano);
            resultado.put(String.valueOf(ano), faturamentoPorMes);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("faturamento", resultado);
        return response;
    }

    @GetMapping("/total-consultas")
    @ResponseBody
    public Map<String, Object> totalConsultas(@RequestParam int dentistaId, @RequestParam int ano) {
        int total = dashboardDAO.getTotalConsultas(dentistaId, ano);
        return Map.of("total", total);
    }


    @GetMapping("/consultas-pendentes")
    @ResponseBody
    public Map<String, Object> consultasPendentes(@RequestParam int dentistaId) {
        int total = dashboardDAO.getConsultasPendentes(dentistaId);
        return Map.of("total", total);
    }

    @GetMapping("/tempo-medio-consultas")
    @ResponseBody
    public Map<String, Object> tempoMedioConsultas(@RequestParam int dentistaId) {
        double mediaMinutos = dashboardDAO.getTempoMedioConsultas(dentistaId);
        return Map.of("mediaMinutos", mediaMinutos);
    }

    @GetMapping("/procedimento-mais")
    @ResponseBody
    public Map<String, Object> procedimentoMais(@RequestParam int dentistaId) {
        return dashboardDAO.getProcedimentoMaisRealizado(dentistaId);
    }


    @GetMapping("/procedimentos-faturamento")
    @ResponseBody
    public List<Map<String, Object>> procedimentosFaturamento(
            @RequestParam int dentistaId,
            @RequestParam int ano) {

        // 1. Busca a lista de procedimentos com faturamento
        List<Map<String, Object>> lista = dashboardDAO.getProcedimentosFaturamento(dentistaId, ano);

        // 2. Busca o faturamento total anual (reutiliza seu método existente)
        BigDecimal faturamentoTotal = dashboardDAO.getFaturamentoAnual(dentistaId, ano);

        // 3. Calcula o percentual para cada procedimento
        for (Map<String, Object> item : lista) {
            BigDecimal faturamento = (BigDecimal) item.get("faturamento");
            double percentual = faturamentoTotal.compareTo(BigDecimal.ZERO) > 0
                    ? faturamento.multiply(BigDecimal.valueOf(100))
                    .divide(faturamentoTotal, 2, RoundingMode.HALF_UP)
                    .doubleValue()
                    : 0.0;
            item.put("percentual", percentual);
        }

        // 4. Retorna a lista com nome, faturamento e percentual
        return lista;
    }




    // A PARTIR DAQUI COMECA AUXILIAR
    // A PARTIR DAQUI COMECA AUXILIAR




    @GetMapping("/auxiliar/faturamento-anual-total")
    public Map<String, Object> faturamentoAnualTotal(@RequestParam int ano) {
        BigDecimal total = dashboardDAO.getFaturamentoAnualTotal(ano);
        return Map.of("valor", total);
    }

    @GetMapping("/auxiliar/total-consultas-geral")
    public Map<String, Object> totalConsultasGeral(@RequestParam int ano) {
        int total = dashboardDAO.getTotalConsultasGeral(ano);
        return Map.of("total", total);
    }

    @GetMapping("/auxiliar/consultas-pendentes-geral")
    public Map<String, Object> consultasPendentesGeral() {
        int total = dashboardDAO.getConsultasPendentesGeral();
        return Map.of("total", total);
    }

    @GetMapping("/auxiliar/tempo-medio-consultas-geral")
    public Map<String, Object> tempoMedioConsultasGeral() {
        double mediaMinutos = dashboardDAO.getTempoMedioConsultasGeral();
        return Map.of("mediaMinutos", mediaMinutos);
    }

    @GetMapping("/auxiliar/total-pacientes-indicados")
    public Map<String, Integer> totalPacientesIndicados() {
        return dashboardDAO.getTotalPacientesEIndicados();
    }


    @GetMapping("/auxiliar/consultas-hoje")
    public List<Map<String, Object>> consultasHojeTodos() {
        return dashboardDAO.getConsultasHojeTodosDentistas();
    }


    @GetMapping("/auxiliar/status-pagamento")
    public List<Map<String, Object>> statusPagamentoTodos(@RequestParam(defaultValue = "geral") String periodo) {
        return dashboardDAO.getStatusPagamentoTodos(periodo);
    }


    @GetMapping("/auxiliar/cobrancas")
    public List<Map<String, Object>> cobrancasPendentes() {
        return dashboardDAO.getCobrancasPendentes();
    }





}
