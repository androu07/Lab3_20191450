package com.example.lab03.controller;

import com.example.lab03.entity.Sedes;
import com.example.lab03.repository.SedesRepository;
import com.example.lab03.repository.VeterinariosRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/veterinarios")
public class VeterinariosController{
    final VeterinariosRepository veterinariosRepository;

    public VeterinariosController(VeterinariosRepository veterinariosRepository) { this.veterinariosRepository = veterinariosRepository; }

    @GetMapping("")
    public String listaVeterinarios(Model model){
        List<Sedes> lista = veterinariosRepository.findAll();
        model.addAttribute("listaVeterinarios",lista);
        return "/list";
    }

}
