package com.consultorio.controllers;

import com.consultorio.dao.AuxiliarDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Auxiliar;
import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginCadastroController {

    @Autowired
    private DentistaDAO dentistaDAO;

    @Autowired
    private AuxiliarDAO auxiliarDAO;

    @GetMapping("/login")
    public String exibirLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String cpf, @RequestParam String email, Model model) {
        try {
            Dentista dentista = dentistaDAO.buscarPorCpf(cpf);
            if (dentista != null && dentista.getEmail().equalsIgnoreCase(email)) {
                model.addAttribute("usuario", dentista);
                model.addAttribute("tipo", "dentista");
                return "redirect:/dentista";
            }
        } catch (Exception ignored) {}

        try {
            Auxiliar auxiliar = auxiliarDAO.buscarPorCpfEEmail(cpf, email);
            if (auxiliar != null) {
                model.addAttribute("usuario", auxiliar);
                model.addAttribute("tipo", "auxiliar");
                return "redirect:/auxiliar";
            }
        } catch (Exception ignored) {}

        model.addAttribute("erro", "Login inválido. Verifique o CPF e o email.");
        return "login";
    }

    @PostMapping("/cadastrar/dentista")
    public String cadastrarDentista(@ModelAttribute Dentista dentista, Model model) {
        if (dentistaDAO.buscarPorCpf(dentista.getCpf()) != null) {
            model.addAttribute("erro", "CPF já cadastrado");
            return "login";
        }

        dentistaDAO.salvar(dentista);
        model.addAttribute("mensagem", "Dentista cadastrado com sucesso!");
        return "login";
    }

    @PostMapping("/cadastrar/auxiliar")
    public String cadastrarAuxiliar(@ModelAttribute Auxiliar auxiliar, Model model) {
        if (auxiliarDAO.buscarPorCpf(auxiliar.getCpf()) != null) {
            model.addAttribute("erro", "CPF já cadastrado");
            return "login";
        }

        auxiliarDAO.salvar(auxiliar);
        model.addAttribute("mensagem", "Auxiliar cadastrado com sucesso!");
        return "login";
    }
}
