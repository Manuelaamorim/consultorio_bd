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
        String sql = "INSERT INTO dentista (nome, cpf, telefone, telefone2, email, rua, numero, bairro, cidade, data_nascimento, cro, especialidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                dentista.getNome(),
                dentista.getCpf(),
                dentista.getTelefone(),
                dentista.getTelefone2(),
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
        System.out.println("Tentando deletar dentista com CPF: " + cpf);
        int linhasAfetadas = jdbcTemplate.update("DELETE FROM dentista WHERE cpf = ?", cpf);
        System.out.println("Linhas deletadas: " + linhasAfetadas);
    }


    public List<Dentista> listarDentistas() {
        String sql = "SELECT * FROM dentista";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Dentista.class));
    }

    public Dentista buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM dentista WHERE cpf = ?";
        List<Dentista> resultado = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Dentista.class), cpf);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    public void atualizar(Dentista dentista) {
        String sql = "UPDATE dentista SET nome = ?, telefone = ?, telefone2 = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, data_nascimento = ?, cro = ?, especialidade = ? WHERE cpf = ?";
        jdbcTemplate.update(sql,
                dentista.getNome(),
                dentista.getTelefone(),
                dentista.getTelefone2(),
                dentista.getEmail(),
                dentista.getRua(),
                dentista.getNumero(),
                dentista.getBairro(),
                dentista.getCidade(),
                dentista.getDataNascimento(),
                dentista.getCro(),
                dentista.getEspecialidade(),
                dentista.getCpf()
        );
    }
}
