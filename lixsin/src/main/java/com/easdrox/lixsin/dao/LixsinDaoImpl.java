package com.easdrox.lixsin.dao;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.easdrox.lixsin.pojo.Token;


@Repository
public class LixsinDaoImpl implements LixsinDao {

    @Override
    public List<Token> analizadorLexico(Model model, String inputText) {
        List<Token> tokens = new ArrayList<>();
        System.out.println("Input text: " + inputText);
        
        String[] lines = inputText.split("\n");
        for (String line : lines) {
            String[] tokenStrings = line.split("\\s+|(?<=;)|(?=;)");
            for (String tokenString : tokenStrings) {
                if (!tokenString.isEmpty()) {  
                    Token token = classifyToken(tokenString);
                    tokens.add(token);
                    System.out.println("Token: " + tokenString);
                    System.out.println("Tipo: " + token.getType());
                }
            }
        }
        
        return tokens;
    }
    
    private Token classifyToken(String tokenString) {
        Token token = new Token(tokenString);
        
        if (isArithmeticOperator(tokenString)) {
            token.setType(Token.Type.ARITHMETIC_OPERATOR);
        } else if (isReservedKeyword(tokenString)) {
            token.setType(Token.Type.RESERVED_KEYWORD);
        } else if (isStringLiteral(tokenString)) {
            token.setType(Token.Type.STRING_LITERAL);
        } else if (isNumber(tokenString)) {
            token.setType(Token.Type.NUMBER);
        } else {
            token.setType(Token.Type.OTHER);
        }
        
        return token;
    }
    
    private boolean isArithmeticOperator(String token) {
        return "+-*/=".contains(token);
    }
    
    private boolean isReservedKeyword(String token) {
        return "if else while for switch case break return int float double Integer String ;".contains(token);
    }
    
    private boolean isStringLiteral(String token) {
        return !isArithmeticOperator(token) && !isReservedKeyword(token) && !isNumber(token);
    }
    
    private boolean isNumber(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }
}
