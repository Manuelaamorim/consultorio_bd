package com.consultorio.dao;

import com.consultorio.models.Auxiliar;
import com.consultorio.models.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuxiliarDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FuncionarioDAO funcionarioDAO;  // Assumindo que existe um DAO para Funcionario

    public void salvar(Auxiliar auxiliar) {
        // Salvar a Pessoa associada ao Funcionario
        String sqlPessoa = "INSERT INTO pessoa (cpf, nome, email, data_nascimento) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlPessoa,
                auxiliar.getCpf(),  // Vem de Pessoa (herança)
                auxiliar.getNome(), // Vem de Pessoa (herança)
                auxiliar.getEmail(), // Vem de Pessoa (herança)
                java.sql.Date.valueOf(auxiliar.getDataNascimento()));  // Vem de Pessoa (herança)

        Long pessoaId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);  // Obtém o ID da Pessoa

        // Salvar o Funcionario, que é necessário para criar um Auxiliar
        Funcionario funcionario = new Funcionario();
        funcionario.setId(pessoaId);  // Associa o ID da Pessoa ao Funcionario diretamente
        funcionarioDAO.salvar(funcionario);

        Long funcionarioId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);  // Obtém o ID do Funcionario

        // Salvar o Auxiliar, incluindo a associação com o Funcionario
        String sqlAuxiliar = "INSERT INTO auxiliar (funcionario_id) VALUES (?)";
        jdbcTemplate.update(sqlAuxiliar, funcionarioId);  // Associa o ID do Funcionario ao Auxiliar
    }

    public void deletarPorCpf(String cpf) {
        String sql = "DELETE a " +
                "FROM auxiliar a " +
                "JOIN funcionario f ON a.funcionario_id = f.id " +
                "JOIN pessoa p ON f.pessoa_id = p.id " +
                "WHERE p.cpf = ?";

        jdbcTemplate.update(sql, cpf);
    }
}
