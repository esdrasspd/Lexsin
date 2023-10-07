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

    @Override
    public boolean analizadorSintactico(List<Token> tokens, Model model, String inputText) {
        System.out.println("texto: "+inputText);
        System.out.println("token: " + tokens.size());
        boolean includeFound = false;
        boolean namespaceFound = false;
        boolean mainFound = false;
        
        for (int i = 0; i < tokens.size(); i++) {
            if (!includeFound && isIncludeIOStream(tokens, i)) {
                includeFound = true;
                System.out.println("Encabezado #include <iostream> encontrado");
            }
    
            if (!namespaceFound && isUsingNamespaceStd(tokens, i)) {
                namespaceFound = true;
                System.out.println("Declaración 'using namespace std;' encontrada");
            }
    
            if (!mainFound && isMainFunction(tokens, i)) {
                mainFound = true;
                System.out.println("Función 'int main()' encontrada");
            }
        }
        if(includeFound == true && namespaceFound == true && mainFound == true){
            return true;
        }else{
            return false;
        }
    
        // Puedes agregar más lógica después de la búsqueda de estas estructuras...   
    }

    private boolean isIncludeIOStream(List<Token> tokens, int index) {
        return index + 1 < tokens.size() &&
                tokens.get(index).getValue().equals("#include") &&
                tokens.get(index + 1).getValue().equals("<iostream>");
    }
    
    private boolean isUsingNamespaceStd(List<Token> tokens, int index) {
        return index + 2 < tokens.size() &&
                tokens.get(index).getValue().equals("using") &&
                tokens.get(index + 1).getValue().equals("namespace") &&
                tokens.get(index + 2).getValue().equals("std") &&
                tokens.get(index + 3).getValue().equals(";");
    }
    
    private boolean isMainFunction(List<Token> tokens, int index) {
        int openingBraceIndex = findOpeningBrace(tokens, index);
        if (openingBraceIndex != -1 && openingBraceIndex + 2 < tokens.size() &&
            tokens.get(index).getValue().equals("int") &&
            tokens.get(index + 1).getValue().equals("main()")) {
            int closingBraceIndex = findClosingBrace(tokens, openingBraceIndex);
           
            // Buscar la cadena "Hola Mundo" dentro de las llaves de la función
               return true;
                
            
        }
        return false;
    }
    

    private int findOpeningBrace(List<Token> tokens, int index) {
        // Buscar la llave de apertura '{' después de 'int main()'
        for (int i = index; i < tokens.size(); i++) {
            if (tokens.get(i).getValue().equals("{")) {
                return i;
            }
        }
        return -1; // Llave de apertura no encontrada
    }

    private int findClosingBrace(List<Token> tokens, int openingBraceIndex) {
        int braceCount = 1;
    
        for (int i = openingBraceIndex + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getValue().equals("{")) {
                braceCount++;
            } else if (tokens.get(i).getValue().equals("}")) {
                braceCount--;
    
                if (braceCount == 0) {
                    return i; // Encuentra la llave cerrada correspondiente
                }
            }
        }
    
        return -1; // Llave cerrada no encontrada
    }

}
    