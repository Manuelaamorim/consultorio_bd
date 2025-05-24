package com.consultorio.controllers;

import com.consultorio.dao.ConsultaDAO;
import com.consultorio.dao.PacienteDAO;
import com.consultorio.models.Consulta;
import com.consultorio.models.Dentista;
import com.consultorio.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class PacienteController {

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private ConsultaDAO consultaDAO;

    @GetMapping("/paciente-form")
    public String mostrarFormularioPaciente(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        String tipo = (String) session.getAttribute("tipo");

        if (usuario == null || tipo == null) {
            return "redirect:/";
        }

        // Adiciona lista de pacientes para o select
        List<Paciente> pacientes = pacienteDAO.listarIdENome();
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("pacientes", pacientes);

        if ("dentista".equals(tipo)) {
            Dentista dentistaLogado = (Dentista) usuario;
            String cpf = dentistaLogado.getCpf();
            model.addAttribute("cpf", cpf);
            return "paciente-form-dentista";
        } else if ("auxiliar".equals(tipo)) {
            return "paciente-form-auxiliar";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        String tipo = (String) session.getAttribute("tipo");

        if (usuario == null || tipo == null) {
            return "redirect:/";
        }

        List<Paciente> pacientes = pacienteDAO.listarPacientes();
        model.addAttribute("pacientes", pacientes);

        if ("dentista".equals(tipo)) {
            Dentista dentistaLogado = (Dentista) usuario;
            String cpf = dentistaLogado.getCpf();
            model.addAttribute("cpf", cpf);
            return "listar-pacientes-dentista";
        } else if ("auxiliar".equals(tipo)) {
            return "listar-pacientes-auxiliar";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/pacientes")
    public String adicionarPaciente(Paciente paciente) {
        pacienteDAO.salvar(paciente);
        return "redirect:/pacientes";
    }

    @DeleteMapping("/pacientes/{cpf}")
    @ResponseBody
    public String deletarPaciente(@PathVariable String cpf) {
        pacienteDAO.deletarPorCpf(cpf);
        return "Paciente com CPF " + cpf + " deletado com sucesso.";
    }

    @GetMapping("/paciente/editar/{cpf}")
    public String editarPaciente(@PathVariable String cpf, Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        String tipo = (String) session.getAttribute("tipo");

        if (usuario == null || tipo == null) {
            return "redirect:/";
        }

        Paciente paciente = pacienteDAO.buscarPorCpf(cpf);
        model.addAttribute("paciente", paciente);

        if ("dentista".equals(tipo)) {
            Dentista dentistaLogado = (Dentista) usuario;
            String dentistaCpf = dentistaLogado.getCpf();
            model.addAttribute("cpf", dentistaCpf);
            return "editar-paciente-dentista";
        } else if ("auxiliar".equals(tipo)) {
            return "editar-paciente-auxiliar";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/paciente/editar")
    public String salvarAlteracoes(Paciente paciente) {
        pacienteDAO.atualizar(paciente);
        return "redirect:/pacientes";
    }

    @GetMapping("/paciente/historico/{id}")
    public String verHistoricoPaciente(@PathVariable("id") int pacienteId, Model model, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        String tipo = (String) session.getAttribute("tipo");

        if (usuario == null || tipo == null) {
            return "redirect:/";
        }

        List<Consulta> consultas = consultaDAO.listarPorPacienteId(pacienteId);
        model.addAttribute("consultas", consultas);

        Paciente paciente = pacienteDAO.buscarPorId(pacienteId);
        model.addAttribute("paciente", paciente);

        if ("dentista".equals(tipo)) {
            Dentista dentistaLogado = (Dentista) usuario;
            String cpf = dentistaLogado.getCpf();
            model.addAttribute("cpf", cpf);
        }

        return "historico-paciente";
    }
}
