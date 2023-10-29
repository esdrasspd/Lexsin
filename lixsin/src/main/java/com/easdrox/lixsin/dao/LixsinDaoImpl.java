package com.easdrox.lixsin.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.easdrox.lixsin.pojo.Token;


@Repository
public class LixsinDaoImpl implements LixsinDao {

    @Override
    public List<Token> analizadorLexico(Model model, String inputText) {
    List<Token> tokens = new ArrayList<>();
    System.out.println("Input text: " + inputText);

   
    Pattern pattern = Pattern.compile("\"(.*?)\"");
    Matcher matcher = pattern.matcher(inputText);

    int lastEnd = 0;

   
    while (matcher.find()) {
       
        String before = inputText.substring(lastEnd, matcher.start());
        addTokens(tokens, before);

      
        String matchedString = matcher.group();
        tokens.add(new Token(matchedString, Token.Type.STRING_LITERAL));

      
        lastEnd = matcher.end();
    }

   
    if (lastEnd < inputText.length()) {
        String after = inputText.substring(lastEnd);
        addTokens(tokens, after);
    }

   
    for (Token token : tokens) {
        System.out.println("Token: " + token.getValue());
        System.out.println("Tipo: " + token.getType());
    }

    return tokens;
}


private void addTokens(List<Token> tokens, String text) {
    String[] tokenStrings = text.split("\\s+|(?<=;)|(?=;)|(?<=<<)|(?=<<)");

    for (String tokenString : tokenStrings) {
        if (!tokenString.isEmpty()) {
            Token token;
            if ("\"".equals(tokenString)) {
                token = new Token(tokenString, Token.Type.DOUBLE_QUOTE);
            } else {
                token = classifyToken(tokenString);
            }
            tokens.add(token);
            System.out.println("Token: " + tokenString);
            System.out.println("Tipo: " + token.getType());
        }
    }
}
    
    private Token classifyToken(String tokenString) {
        Token token = new Token(tokenString, Token.Type.OTHER);
    
        if (token.getType() == Token.Type.OTHER) {
            if (isArithmeticOperator(tokenString)) {
                token.setType(Token.Type.ARITHMETIC_OPERATOR);
            } else if (isReservedKeyword(tokenString)) {
                token.setType(Token.Type.RESERVED_KEYWORD);
            } else if (isStringLiteral(tokenString)) {
                token.setType(Token.Type.STRING_LITERAL);
            } else if (isNumber(tokenString)) {
                token.setType(Token.Type.NUMBER);
            }
        }
    
        return token;
    }
    
    private boolean isArithmeticOperator(String token) {
        return "+-*/=".contains(token);
    }
    
    private boolean isReservedKeyword(String token) {
        return "if else while for switch case break return int { } #include <iostream> float double Integer String cout<< using namespace std main() ; ".contains(token);
    }
    
    private boolean isStringLiteral(String token) {
        return !isArithmeticOperator(token) && !isReservedKeyword(token) && !isNumber(token);
    }
    
    private boolean isNumber(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
public boolean analizadorSintactico(List<Token> tokens, Model model, String inputText) {
    System.out.println("texto: " + inputText);
    System.out.println("token: " + tokens.size());
    boolean includeFound = false;
    boolean namespaceFound = false;
    boolean mainFound = false;
    boolean validateMain = false;

    for (int i = 0; i < tokens.size(); i++) {
        if (!includeFound && isIncludeIOStream(tokens, i)) {
            includeFound = true;
            System.out.println("Encabezado #include <iostream> encontrado");
        } 

        if (!namespaceFound && isUsingNamespaceStd(tokens, i)) {
            namespaceFound = true;
            System.out.println("Declaración 'using namespace std;' encontrada");
        } else {
            model.addAttribute("error", "Error en la declaración 'using namespace std;' ");
        }

        if (!mainFound && isMainFunction(tokens, i)) {
            mainFound = true;
            System.out.println("Función 'int main()' encontrada");
            if (validateMainBody(tokens, i)) {
            validateMain = true;
            System.out.println("Cuerpo de 'int main()' válido");
        } else {
            model.addAttribute("error", "Error en el cuerpo de 'int main()' ");
        }
        }
    }

    if(!includeFound) {
        System.out.println("Error en el encabezado #include <iostream> ");
        model.addAttribute("error", "Error en el encabezado #include <iostream> ");
    }

    if(!namespaceFound) {
        model.addAttribute("error", "Error en la declaración 'using namespace std; ");
    }

    if(!mainFound) {
        model.addAttribute("error", "Error en la función int main() ");
    }

    if(!validateMain) {
        model.addAttribute("error", "Error en el cuerpo de int main() ");
    }

    return includeFound && namespaceFound && mainFound && validateMain;
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
            return true;
        }
        return false;
    }
    
    private boolean validateMainBody(List<Token> tokens, int mainIndex) {
    
        
            
          return containsCout(tokens);
        
    }
    

    private int findOpeningBrace(List<Token> tokens, int index) {
        
        for (int i = index; i < tokens.size(); i++) {
            if (tokens.get(i).getValue().equals("{")) {
                return i;
            }
        }
        return -1; 
    }

    private int findClosingBrace(List<Token> tokens, int openingBraceIndex) {
        int braceCount = 1;
    
        for (int i = openingBraceIndex + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getValue().equals("{")) {
                braceCount++;
            } else if (tokens.get(i).getValue().equals("}")) {
                braceCount--;
    
                if (braceCount == 0) {
                    return i; 
                }
            }
        }
        return -1; 
    }

    private boolean containsCout(List<Token> tokens) {
        for (int i = 0; i < tokens.size() - 3; i++) {
            if (tokens.get(i).getValue().equals("cout") &&
                tokens.get(i + 1).getValue().equals("<<") &&
                tokens.get(i + 2).getType() == Token.Type.STRING_LITERAL &&
                tokens.get(i + 3).getType() == Token.Type.RESERVED_KEYWORD &&
                tokens.get(i + 4).getType() == Token.Type.RESERVED_KEYWORD
                ) {
                return true;
            }    
        }
        return false;
    }

}
    