package com.easdrox.lixsin.pojo;

public class Token {

    public enum Type {
        ARITHMETIC_OPERATOR, 
        RESERVED_KEYWORD, 
        STRING_LITERAL, 
        NUMBER, 
        OTHER
    }
    private Type type;
    private String value;

    public Token(String value) {
        super();
        this.value = value;
    }

    public Token(){

    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }


 
}
