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

import java.util.List;


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
        return session.getAttribute("usuario") == null ||
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


    @GetMapping("/consultas")
    public String listarConsultasAuxiliar(HttpSession session, Model model) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        List<Consulta> consultas = consultaDAO.listarTodas(); // ou criar um método específico para auxiliar
        model.addAttribute("consultas", consultas);

        return "consultas-auxiliar";
    }


    @PostMapping("/consultas")
    public String criarConsulta(@ModelAttribute Consulta consulta, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        consultaDAO.salvar(consulta);
        return "redirect:/auxiliar/consultas"; // ou alguma página de sucesso
    }

    @GetMapping("/consultas/nova")
    public String mostrarFormularioConsulta(Model model, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());

        return "consulta-form-auxiliar";
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
    @GetMapping("/editar/{id}")
    public String editarConsulta(HttpSession session, @PathVariable int id, Model model) {
        if (verificaSessaoAuxiliar(session)) return "redirect:/";
        Consulta consulta = consultaDAO.buscarPorId(id);
        model.addAttribute("consulta", consulta);
        return "editar-consulta-auxiliar";  // Nome do seu arquivo HTML
    }

    // Salvar as alterações
    @PostMapping("/editar/{id}")
    public String salvarAlteracoes(HttpSession session, @ModelAttribute Consulta consulta) {
        if (verificaSessaoAuxiliar(session)) return "redirect:/";
        consultaDAO.atualizar(consulta);
        return "redirect:/auxiliar/consultas";
    }

    // Excluir consulta
    @GetMapping("/consultas/excluir/{id}")
    public String excluirConsulta(@PathVariable int id, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        consultaDAO.excluir(id);
        return "redirect:/auxiliar/consultas";
    }




}
