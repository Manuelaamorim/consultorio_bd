package com.consultorio.controllers;

import com.consultorio.dao.DentistaDAO;
import com.consultorio.dao.ProcedimentoDAO;
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

    // Método para verificar se o usuário está logado como dentista
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
        return "dentista.html";
    }

    @GetMapping("/form")
    public String mostrarFormulario(HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";

        return "dentista-form.html";
    }

    @GetMapping("/listar")
    public String listarDentistas(HttpSession session, Model model) {
        if (verificaSessaoDentista(session)) return "redirect:/";

        List<Dentista> dentistas = dentistaDAO.listarDentistas();
        model.addAttribute("dentistas", dentistas);
        return "listar-dentistas.html";
    }

    @PostMapping("/salvar")
    @ResponseBody
    public String adicionarDentista(HttpSession session, @RequestBody Dentista dentista) {
        if (verificaSessaoDentista(session))
            return "redirect:/";

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
        return "editar-dentista";
    }

    @PostMapping("/editar")
    public String salvarAlteracoes(HttpSession session, Dentista dentista) {
        if (verificaSessaoDentista(session)) return "redirect:/";

        dentistaDAO.atualizar(dentista);
        return "redirect:/dentista/listar";
    }

    @PostMapping("/procedimento/cadastrar")
    public String cadastrarProcedimento(@ModelAttribute Procedimento procedimento, HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        procedimentoDAO.salvar(procedimento);
        return "redirect:/dentista/procedimentos"; // você vai criar essa tela pra listar depois
    }

    @GetMapping("/procedimentos")
    public String listarProcedimentos(Model model, HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";

        model.addAttribute("procedimentos", procedimentoDAO.listarTodos());
        return "procedimentos"; // página HTML que vai mostrar os procedimentos
    }

    @GetMapping("/consultas-dentista")
    public String consultasDentista() {
        return "consultas-dentista"; // sem .html, o Thymeleaf resolve automaticamente
    }

    @GetMapping("/consulta-form")
    public String mostrarFormularioConsulta(HttpSession session) {
        if (verificaSessaoDentista(session)) return "redirect:/";
        return "consulta-form"; // Thymeleaf resolve para consulta-form.html
    }
}
