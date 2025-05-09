package com.consultorio.controllers;

import com.consultorio.dao.AuxiliarDAO;
import com.consultorio.dao.ConsultaDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.dao.PacienteDAO;
import com.consultorio.models.Auxiliar;
import com.consultorio.models.Consulta;
import com.consultorio.models.Paciente;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auxiliar")
public class AuxiliarController {

    @Autowired
    private ConsultaDAO consultaDAO;

    @Autowired
    private PacienteDAO pacienteDAO;

    @Autowired
    private DentistaDAO dentistaDAO;

    private Consulta consulta;

    private boolean verificaSessaoAuxiliar(HttpSession session) {
        return session.getAttribute("usuario") != null &&
                !"auxiliar".equals(session.getAttribute("tipo"));
    }

    @GetMapping("")
    public String mostrarHome(HttpSession session, Model model) {
        if (verificaSessaoAuxiliar(session)) {
            model.addAttribute("erro", "Faça login antes.");
            return "redirect:/";
        }
        return "auxiliar.html";
    }

    @PostMapping("/consultas")
    public String criarConsulta(@ModelAttribute Consulta consulta, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        consultaDAO.salvar(consulta);
        return "redirect:/auxiliar"; // ou alguma página de sucesso
    }

    @GetMapping("/consultas/nova")
    public String mostrarFormularioConsulta(Model model, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());

        return "consulta-form";
    }

    @GetMapping("/pacientes/novo")
    public String mostrarFormularioPaciente(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "formulario-paciente";
    }

    @PostMapping("/pacientes/salvar")
    public String salvarPaciente(@ModelAttribute Paciente paciente) {
        pacienteDAO.salvar(paciente);
        return "redirect:/auxiliar/consultas/nova";
    }
}
