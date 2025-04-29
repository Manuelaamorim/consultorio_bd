package com.consultorio.controllers;

import com.consultorio.dao.AuxiliarDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Auxiliar;
import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginCadastroController {

    @Autowired
    private DentistaDAO dentistaDAO;

    @Autowired
    private AuxiliarDAO auxiliarDAO;

    @GetMapping("/")
    public String exibirLogin(Model model) {
        return "login";
    }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        return "cadastro";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String cpf, @RequestParam String email, HttpSession session, Model model) {
        try {
            Dentista dentista = dentistaDAO.buscarPorCpf(cpf);
            if (dentista != null && dentista.getEmail().equalsIgnoreCase(email)) {
                session.setAttribute("usuario", dentista);
                session.setAttribute("tipo", "dentista");
                return "redirect:/dentista";
            }
        } catch (Exception ignored) {}

        try {
            Auxiliar auxiliar = auxiliarDAO.buscarPorCpfEEmail(cpf, email);
            if (auxiliar != null) {
                session.setAttribute("usuario", auxiliar);
                session.setAttribute("tipo", "auxiliar");
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
            return "cadastro";
        }

        dentistaDAO.salvar(dentista);
        model.addAttribute("mensagem", "Dentista cadastrado com sucesso!");
        return "cadastro";
    }

    @PostMapping("/cadastrar/auxiliar")
    public String cadastrarAuxiliar(@ModelAttribute Auxiliar auxiliar, Model model) {
        if (auxiliarDAO.buscarPorCpf(auxiliar.getCpf()) != null) {
            model.addAttribute("erro", "CPF já cadastrado");
            return "cadastro";
        }

        auxiliarDAO.salvar(auxiliar);
        model.addAttribute("mensagem", "Auxiliar cadastrado com sucesso!");
        return "cadastro";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalidando a sessão corretamente
        }
        return "redirect:/"; // Redireciona para o login
    }
}