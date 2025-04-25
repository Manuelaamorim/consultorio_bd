package com.consultorio.controllers;

import com.consultorio.dao.AuxiliarDAO;
import com.consultorio.dao.DentistaDAO;
import com.consultorio.models.Dentista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuxiliarController {

    @Autowired
    private AuxiliarDAO auxiliarDAO;

    @GetMapping("/auxiliar")
    public String mostrarHome() {
        return "auxiliar.html";
    }
}
