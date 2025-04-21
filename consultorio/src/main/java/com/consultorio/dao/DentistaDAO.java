package com.consultorio.dao;

import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

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

    public List<Dentista> listarDentistas() {
        String sql = "SELECT * FROM Dentista";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Dentista.class));
    }

    public Dentista buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Dentista WHERE cpf = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Dentista.class),
                cpf
        );
    }

    public void atualizar(Dentista dentista) {
        String sql = "UPDATE Dentista SET nome = ?, telefone = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, data_nascimento = ?, cro = ?, especialidade = ? WHERE cpf = ?";
        jdbcTemplate.update(sql,
                dentista.getNome(),
                dentista.getTelefone(),
                dentista.getEmail(),
                dentista.getRua(),
                dentista.getNumero(),
                dentista.getBairro(),
                dentista.getCidade(),
                dentista.getDataNascimento(),
                dentista.getCro(),
                dentista.getEspecialidade(),
                dentista.getCpf()  // Necessário para identificar o dentista a ser atualizado
        );
    }
}
