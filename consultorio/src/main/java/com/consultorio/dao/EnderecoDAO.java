package com.consultorio.dao;

import com.consultorio.models.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnderecoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Endereco endereco) {
        String sql = "INSERT INTO endereco (rua, numero, bairro, cidade, pessoa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getPessoaId());
    }
}
