package com.consultorio.dto;

import com.consultorio.models.Pessoa;
import com.consultorio.models.Telefone;
import com.consultorio.models.Endereco;

import java.util.List;

public class CadastroFuncionarioDTO {
    private Pessoa pessoa;
    private List<Telefone> telefones;
    private Endereco endereco;
    private String tipoFuncionario; // "dentista" ou "auxiliar"
    private String cro;
    private String especialidade;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTipoFuncionario() {
        return tipoFuncionario;
    }

    public void setTipoFuncionario(String tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}
