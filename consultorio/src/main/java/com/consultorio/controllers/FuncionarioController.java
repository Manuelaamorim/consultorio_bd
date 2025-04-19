package com.consultorio.controllers;

import com.consultorio.dto.CadastroFuncionarioDTO;
import com.consultorio.models.Endereco;
import com.consultorio.models.Pessoa;
import com.consultorio.models.Telefone;
import com.consultorio.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarFuncionario(@RequestBody CadastroFuncionarioDTO dto) {
        funcionarioService.cadastrarFuncionario(dto);
        return ResponseEntity.ok("Funcionário cadastrado com sucesso!");
    }

    // Endpoint de teste
    @PostMapping("/teste")
    public ResponseEntity<?> cadastrarFuncionarioTeste() {
        // Criar objeto Pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setCpf("12345678900");
        pessoa.setNome("João da Silva");
        pessoa.setEmail("joao@teste.com");
        pessoa.setDataNascimento(LocalDate.of(1990, 5, 15));

        // Telefones
        Telefone telefone1 = new Telefone();
        telefone1.setNumero("11999999999");

        Telefone telefone2 = new Telefone();
        telefone2.setNumero("11888888888");

        // Endereço
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Exemplo");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");

        // DTO de cadastro
        CadastroFuncionarioDTO dto = new CadastroFuncionarioDTO();
        dto.setPessoa(pessoa);
        dto.setTelefones(Arrays.asList(telefone1, telefone2));
        dto.setEndereco(endereco);
        dto.setTipoFuncionario("dentista");
        dto.setCro("CRO-SP-12345");
        dto.setEspecialidade("Ortodontia");

        funcionarioService.cadastrarFuncionario(dto);
        return ResponseEntity.ok("Funcionário de teste cadastrado com sucesso!");
    }
}
