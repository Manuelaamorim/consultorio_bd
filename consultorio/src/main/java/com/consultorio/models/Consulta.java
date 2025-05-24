package com.consultorio.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Consulta {
    private int id;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioTermino;
    private String statusPagamento;
    private String metodoPagamento;
    private Integer idPaciente;
    private Integer idDentista;

    // ✅ NOVOS CAMPOS
    private List<String> procedimentos;  // Lista de códigos dos procedimentos vinculados à consulta
    private BigDecimal valorConsulta;    // Valor total calculado (soma dos procedimentos)

    // ✅ CONSTRUTORES
    public Consulta() {}

    public Consulta(LocalDate data, LocalTime horarioInicio, LocalTime horarioTermino,
                    String statusPagamento, String metodoPagamento, int idPaciente, int idDentista) {
        this.data = data;
        this.horarioInicio = horarioInicio;
        this.horarioTermino = horarioTermino;
        this.statusPagamento = statusPagamento;
        this.metodoPagamento = metodoPagamento;
        this.idPaciente = idPaciente;
        this.idDentista = idDentista;
    }

    // ✅ GETTERS e SETTERS

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }
    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioTermino() {
        return horarioTermino;
    }
    public void setHorarioTermino(LocalTime horarioTermino) {
        this.horarioTermino = horarioTermino;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }
    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }
    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdDentista() {
        return idDentista;
    }
    public void setIdDentista(Integer idDentista) {
        this.idDentista = idDentista;
    }

    // ✅ Procedimentos vinculados
    public List<String> getProcedimentos() {
        return procedimentos;
    }
    public void setProcedimentos(List<String> procedimentos) {
        this.procedimentos = procedimentos;
    }

    // ✅ Valor calculado da consulta
    public BigDecimal getValorConsulta() {
        return valorConsulta;
    }
    public void setValorConsulta(BigDecimal valorConsulta) {
        this.valorConsulta = valorConsulta;
    }
}
