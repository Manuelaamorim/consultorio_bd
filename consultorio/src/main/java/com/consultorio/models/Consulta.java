package com.consultorio.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private int id;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioTermino;
    private String statusPagamento;
    private String metodoPagamento;
    private Integer idPaciente;
    private Integer idDentista;

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

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(int idDentista) {
        this.idDentista = idDentista;
    }
}
