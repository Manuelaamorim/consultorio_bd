package com.consultorio.controllers;

import com.consultorio.dao.ConsultaDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.dao.PacienteDAO;
import com.consultorio.dao.ProcedimentoDAO;
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

    @Autowired
    private ProcedimentoDAO procedimentoDAO;

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

        List<Consulta> consultas = consultaDAO.listarTodas();
        model.addAttribute("consultas", consultas);

        return "consultas-auxiliar";
    }

    @GetMapping("/consultas/nova")
    public String mostrarFormularioConsulta(Model model, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        model.addAttribute("consulta", new Consulta());
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());

        return "consulta-form-auxiliar";
    }

    @PostMapping("/consultas")
    public String criarConsulta(@ModelAttribute Consulta consulta,
                                @RequestParam("procedimentos") List<String> procedimentos,
                                HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        consulta.setProcedimentos(procedimentos);
        consultaDAO.salvar(consulta);
        return "redirect:/auxiliar/consultas";
    }

    @GetMapping("/editar/{id}")
    public String editarConsulta(HttpSession session, @PathVariable int id, Model model) {
        if (verificaSessaoAuxiliar(session)) return "redirect:/";

        Consulta consulta = consultaDAO.buscarPorId(id);
        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());

        return "editar-consulta-auxiliar";
    }

    @PostMapping("/editar/{id}")
    public String salvarAlteracoes(HttpSession session,
                                   @PathVariable int id,
                                   @ModelAttribute Consulta consulta,
                                   @RequestParam("procedimentos") List<String> procedimentos) {
        if (verificaSessaoAuxiliar(session)) return "redirect:/";

        consulta.setId(id);
        consulta.setProcedimentos(procedimentos);
        consultaDAO.atualizar(consulta);

        return "redirect:/auxiliar/consultas";
    }

    @GetMapping("/consultas/excluir/{id}")
    public String excluirConsulta(@PathVariable int id, HttpSession session) {
        if (verificaSessaoAuxiliar(session)) {
            return "redirect:/";
        }

        consultaDAO.excluir(id);
        return "redirect:/auxiliar/consultas";
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
