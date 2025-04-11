package com.consultorio.dao;

import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DentistaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void salvar(Dentista dentista) {
        String sql = "INSERT INTO Dentista (nome, cpf, telefone, email, rua, numero, bairro, cidade, data_nascimento, cro, especialidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                dentista.getNome(),
                dentista.getCpf(),
                dentista.getTelefone(),
                dentista.getEmail(),
                dentista.getRua(),
                dentista.getNumero(),
                dentista.getBairro(),
                dentista.getCidade(),
                dentista.getDataNascimento(),
                dentista.getCro(),
                dentista.getEspecialidade()
        );
    }

    public void deletarPorCpf(String cpf) {
        String sql = "DELETE FROM dentista WHERE cpf = ?";
        jdbcTemplate.update(sql, cpf);
    }
}
