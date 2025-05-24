package com.consultorio.controllers;

import com.consultorio.dao.ConsultaDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.dao.PacienteDAO;
import com.consultorio.dao.ProcedimentoDAO;
import com.consultorio.models.Consulta;
import com.consultorio.models.Dentista;
import com.consultorio.models.Procedimento;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dentista")
public class DentistaController {

    @Autowired
    private DentistaDAO dentistaDAO;

    @Autowired
    private ProcedimentoDAO procedimentoDAO;

    @Autowired
    private ConsultaDAO consultaDAO;

    @Autowired
    private PacienteDAO pacienteDAO;

    private boolean verificaSessaoDentista(HttpSession session) {
        return session.getAttribute("usuario") == null ||
                !"dentista".equals(session.getAttribute("tipo"));
    }

    @GetMapping("")
    public String mostrarHome(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) {
            model.addAttribute("erro", "Faça login antes.");
            return "redirect:/";
        }
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "dentista.html";
    }

    @GetMapping("/form")
    public String mostrarFormulario(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "dentista-form.html";
    }

    @GetMapping("/listar")
    public String listarDentistas(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "listar-dentistas.html";
    }

    @PostMapping("/salvar")
    @ResponseBody
    public String adicionarDentista(HttpSession session, @RequestBody Dentista dentista) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        dentistaDAO.salvar(dentista);
        return "Dentista salvo com sucesso!";
    }

    @DeleteMapping("/deletar/{cpf}")
    @ResponseBody
    public String deletarDentista(HttpSession session, @PathVariable String cpf) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        dentistaDAO.deletarPorCpf(cpf);
        return "Dentista com CPF " + cpf + " deletado com sucesso.";
    }

    @GetMapping("/editar/{cpf}")
    public String editarDentista(HttpSession session, @PathVariable String cpf, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        Dentista dentista = dentistaDAO.buscarPorCpf(cpf);
        model.addAttribute("dentista", dentista);
        model.addAttribute("cpf", cpf);
        return "editar-dentista";
    }

    @PostMapping("/editar")
    public String salvarAlteracoes(HttpSession session, Dentista dentista) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        dentistaDAO.atualizar(dentista);
        return "redirect:/dentista";
    }

    @PostMapping("/procedimento/cadastrar")
    public String cadastrarProcedimento(@ModelAttribute Procedimento procedimento, HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        procedimentoDAO.salvar(procedimento);
        return "redirect:/dentista/procedimentos";
    }

    @GetMapping("/procedimentos")
    public String listarProcedimentos(Model model, HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "procedimentos";
    }

    @GetMapping("/consultas")
    public String listarConsultasDoDentista(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        model.addAttribute("consultas", consultaDAO.listarPorCpfDentista(dentistaLogado.getCpf()));
        return "consultas-dentista";
    }

    @GetMapping("/consulta-form")
    public String mostrarFormularioConsulta(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());
        model.addAttribute("consulta", new Consulta());
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "consulta-form";
    }

    @PostMapping("/consultas")
    public String salvarConsulta(HttpSession session,
                                 @ModelAttribute Consulta consulta,
                                 @RequestParam("procedimentos") List<String> procedimentos) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        consulta.setProcedimentos(procedimentos);
        consultaDAO.salvar(consulta);
        return "redirect:/dentista/consultas";
    }

    @GetMapping("/consultas/excluir/{id}")
    public String excluirConsulta(@PathVariable int id, HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        consultaDAO.excluir(id);
        return "redirect:/dentista/consultas";
    }

    @GetMapping("/consulta/editar/{id}")
    public String editarConsulta(HttpSession session, @PathVariable int id, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        Consulta consulta = consultaDAO.buscarPorId(id);
        model.addAttribute("consulta", consulta);
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "editar-consulta-dentista";
    }

    @PostMapping("/consulta/editar/{id}")
    public String salvarAlteracoesConsulta(HttpSession session,
                                           @PathVariable int id,
                                           @ModelAttribute Consulta consulta,
                                           @RequestParam("procedimentos") List<String> procedimentos) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        consulta.setId(id);
        consulta.setProcedimentos(procedimentos);
        consultaDAO.atualizar(consulta);
        return "redirect:/dentista/consultas";
    }

    @GetMapping("/consultas/nova")
    public String mostrarFormularioNovaConsulta(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        model.addAttribute("pacientes", pacienteDAO.listarPacientes());
        model.addAttribute("dentistas", dentistaDAO.listarDentistas());
        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());
        model.addAttribute("consulta", new Consulta());
        Dentista dentistaLogado = (Dentista) session.getAttribute("usuario");
        model.addAttribute("cpf", dentistaLogado.getCpf());
        return "consulta-form";
    }

    @PostMapping("/consultas/nova")
    public String salvarNovaConsulta(HttpSession session,
                                     @ModelAttribute Consulta consulta,
                                     @RequestParam("procedimentos") List<String> procedimentos) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        consulta.setProcedimentos(procedimentos);
        consultaDAO.salvar(consulta);
        return "redirect:/dentista/consultas";
    }
}
