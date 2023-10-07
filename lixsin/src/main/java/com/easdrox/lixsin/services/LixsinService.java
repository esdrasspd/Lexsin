package com.easdrox.lixsin.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.easdrox.lixsin.dao.LixsinDao;
import com.easdrox.lixsin.pojo.Token;

@Service
public class LixsinService {

    @Autowired
    private LixsinDao lixsinDao;
    
    public List<Token> analizadorLexico(Model model, String inputText){
        List<Token> tokens = lixsinDao.analizadorLexico(model, inputText);
        return tokens;
    }

    public boolean analizadorSintactico(List<Token> tokens, Model model, String inputText) {
        return lixsinDao.analizadorSintactico(tokens, model, inputText);
    }
}
