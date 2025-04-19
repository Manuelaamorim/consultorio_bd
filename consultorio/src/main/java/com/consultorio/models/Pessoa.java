package com.consultorio.models;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public class Pessoa {
    private Long id;
    private String cpf;
    private String nome;
    private String email;
    private List<Telefone> telefones;
    private Endereco endereco;
    private LocalDate dataNascimento;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Telefone> getTelefones() {return telefones;}

    public void setTelefones(List<Telefone> telefones) {this.telefones = telefones;}

    public Endereco getEndereco() {return endereco;}

    public void setEndereco(Endereco endereco) {this.endereco = endereco;}

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}

