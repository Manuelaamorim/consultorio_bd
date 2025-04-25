package com.consultorio.controllers;

import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DentistaController {

    @Autowired
    private DentistaDAO dentistaDAO;

    // Serve o HTML quando acessa localhost:8080/
    @GetMapping("/")
    public String mostrarFormulario() {
        return "dentista-form.html"; // Arquivo tem que estar em src/main/resources/static/
    }

    @GetMapping("/dentista")
    public String mostrarHome() {
        return "dentista.html";
    }

    // Método para listar todos os dentistas
    @GetMapping("/dentistas")
    public String listarDentistas(Model model) {
        List<Dentista> dentistas = dentistaDAO.listarDentistas();
        model.addAttribute("dentistas", dentistas);
        return "listar-dentistas.html";
    }

    @PostMapping("/dentistas")
    @ResponseBody
    public String adicionarDentista(@RequestBody Dentista dentista) {
        dentistaDAO.salvar(dentista);
        return "Dentista salvo com sucesso!";
    }

    // Método para excluir um dentista pelo CPF
    @DeleteMapping("/dentistas/{cpf}")
    @ResponseBody
    public String deletarDentista(@PathVariable String cpf) {
        dentistaDAO.deletarPorCpf(cpf);
        return "Dentista com CPF " + cpf + " deletado com sucesso.";
    }

    @GetMapping("/dentista/editar/{cpf}")
    public String editarDentista(@PathVariable String cpf, Model model) {
        Dentista dentista = dentistaDAO.buscarPorCpf(cpf);
        model.addAttribute("dentista", dentista);
        return "editar-dentista";  // Nome da página de edição
    }

    // Método para salvar as alterações no dentista
    @PostMapping("/dentista/editar")
    public String salvarAlteracoes(Dentista dentista) {
        dentistaDAO.atualizar(dentista);
        return "redirect:/dentistas";
    }

}
