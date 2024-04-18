package com.example.lab03.controller;

import com.example.lab03.entity.Sedes;
import com.example.lab03.repository.SedesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sedes")
public class SedesController{
    final SedesRepository sedesRepository;

    public SedesController(SedesRepository sedesRepository) {
        this.sedesRepository = sedesRepository;
    }

    @GetMapping("")
    public String listaSedes(Model model){
        List<Sedes> lista = sedesRepository.findAll();
        model.addAttribute("listaSedes",lista);
        return "/list";
    }

}
