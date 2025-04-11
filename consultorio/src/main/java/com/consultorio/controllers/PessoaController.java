package com.consultorio.controllers;

import com.consultorio.dao.PessoaDAO;
import com.consultorio.models.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaDAO pessoaDAO;

    @PostMapping
    public String cadastrarPessoa(@RequestBody Pessoa pessoa) {
        pessoaDAO.salvar(pessoa);
        return "Pessoa cadastrada com sucesso!";
    }
}

