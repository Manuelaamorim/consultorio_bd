package com.consultorio.controllers;

import com.consultorio.dao.AuxiliarDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Auxiliar;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auxiliar")
public class AuxiliarController {

    // Método para verificar se o usuário está logado como auxiliar
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
}
