package com.easdrox.lixsin.dao;

import java.util.List;

import org.springframework.ui.Model;

import com.easdrox.lixsin.pojo.Token;

public interface LixsinDao {
    List<Token> analizadorLexico(Model model, String inputText);
    boolean analizadorSintactico(List<Token> tokens, Model model, String inputText);
}
