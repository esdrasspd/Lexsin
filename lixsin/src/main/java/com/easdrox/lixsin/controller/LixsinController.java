package com.easdrox.lixsin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.easdrox.lixsin.pojo.Token;
import com.easdrox.lixsin.services.LixsinService;


@Controller
public class LixsinController {

    @Autowired
    private LixsinService lixsinService;
    
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/analizadorLexico")
    public String analizadorLexico(@RequestParam("inputText") String inputText, Model model) throws InterruptedException{
        List<Token> tokens = lixsinService.analizadorLexico(model, inputText);
        model.addAttribute("tokens", tokens);
        Thread.sleep(1500);
        return "resultado";
    }

    @GetMapping("/resultado")
    public String resultado(){
        return "resultado";
    }
   
    
}
