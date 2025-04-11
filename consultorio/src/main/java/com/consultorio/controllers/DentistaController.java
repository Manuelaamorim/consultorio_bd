package com.consultorio.controllers;

import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
public class DentistaController {

    @Autowired
    private DentistaDAO dentistaDAO;

    // Serve o HTML quando acessa localhost:8080/
    @GetMapping("/")
    public String mostrarFormulario() {
        return "dentista-form.html"; // Arquivo tem que estar em src/main/resources/static/
    }

    // Recebe os dados do formulário
    @PostMapping("/dentistas")
    @ResponseBody // Retorna string diretamente como resposta
    public String adicionarDentista(@RequestBody Dentista dentista) {
        dentistaDAO.salvar(dentista);
        return "Dentista salvo com sucesso!";
    }

    @GetMapping("/testar-conexao")
    public ResponseEntity<String> testarConexao() {
        Dentista dentista = new Dentista();
        dentista.setCpf("99999999");
        dentista.setNome("Teste Conexao");
        dentista.setEmail("teste@teste.com");
        dentista.setTelefone("8888888888");
        dentista.setRua("Rua Teste");
        dentista.setNumero("123");
        dentista.setBairro("Centro");
        dentista.setCidade("Cidade Teste");
        dentista.setDataNascimento(LocalDate.of(1990, 1, 1));
        dentista.setCro("CRO12345");
        dentista.setEspecialidade("Ortodontia");

        dentistaDAO.salvar(dentista);
        return ResponseEntity.ok("Conexão funcionando e dentista salvo.");
    }

    @DeleteMapping("/dentistas/{cpf}")
    @ResponseBody
    public String deletarDentista(@PathVariable String cpf) {
        dentistaDAO.deletarPorCpf(cpf);
        return "Dentista com CPF " + cpf + " deletado com sucesso.";
    }
}
