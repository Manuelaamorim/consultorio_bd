package com.consultorio.controllers;

import com.consultorio.dao.PacienteDAO;
import com.consultorio.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PacienteController {
    @Autowired
    private PacienteDAO pacienteDAO;

    @GetMapping("/paciente-form")
    public String mostrarFormulario() {
        return "paciente-form.html"; // Arquivo que você deve criar em static/
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        List<Paciente> pacientes = pacienteDAO.listarPacientes();
        model.addAttribute("pacientes", pacientes);
        return "listar-pacientes.html"; // Novo arquivo para listar pacientes
    }

    @PostMapping("/pacientes")
    @ResponseBody
    public String adicionarPaciente(@RequestBody Paciente paciente) {
        pacienteDAO.salvar(paciente);
        return "Paciente salvo com sucesso!";
    }

    @DeleteMapping("/pacientes/{cpf}")
    @ResponseBody
    public String deletarPaciente(@PathVariable String cpf) {
        pacienteDAO.deletarPorCpf(cpf);
        return "Paciente com CPF " + cpf + " deletado com sucesso.";
    }

    @GetMapping("/paciente/editar/{cpf}")
    public String editarPaciente(@PathVariable String cpf, Model model) {
        Paciente paciente = pacienteDAO.buscarPorCpf(cpf);
        model.addAttribute("paciente", paciente);
        return "editar-paciente";  // Página de edição do paciente
    }

    @PostMapping("/paciente/editar")
    public String salvarAlteracoes(Paciente paciente) {
        pacienteDAO.atualizar(paciente);
        return "redirect:/pacientes";
    }
}
