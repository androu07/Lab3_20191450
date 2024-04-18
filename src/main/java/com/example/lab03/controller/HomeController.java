package com.example.lab03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @GetMapping("/index" )
    public String paginaPrincipal(){
        return "index";
    }

    @GetMapping("/sedes" )
    public String paginaSedes(){
        return "sedes";
    }
}
