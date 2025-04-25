package com.consultorio.models;

import java.math.BigDecimal;

public class Procedimento {
    private int codigo;
    private String nome;
    private String pos_operatorio;
    private BigDecimal valor;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPos_operatorio() {
        return pos_operatorio;
    }

    public void setPos_operatorio(String pos_operatorio) {
        this.pos_operatorio = pos_operatorio;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
