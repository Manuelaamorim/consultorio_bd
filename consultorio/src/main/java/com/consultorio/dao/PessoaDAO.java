package com.consultorio.dao;

import com.consultorio.models.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public class PessoaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (cpf, nome, email, telefone, rua, numero, bairro, cidade, data_nascimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                pessoa.getCpf(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getTelefone(),
                pessoa.getRua(),
                pessoa.getNumero(),
                pessoa.getBairro(),
                pessoa.getCidade(),
                Date.valueOf(pessoa.getDataNascimento()));
    }
}
