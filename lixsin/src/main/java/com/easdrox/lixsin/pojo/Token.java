package com.easdrox.lixsin.pojo;

public class Token {

    public enum Type {
        ARITHMETIC_OPERATOR, 
        RESERVED_KEYWORD, 
        STRING_LITERAL, 
        NUMBER, 
        DOUBLE_QUOTE,
        OTHER
    }
    private Type type;
    private String value;

    public Token(String value, Type type) {
        super();
        this.value = value;
        this.type = type;
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
