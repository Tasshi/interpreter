/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author c0117200
 */
public class Program extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    
     public Program(LexicalUnit lu, Environment env) {
         first=lu;
         this.env=env;
    }

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.FOR);
        firstSet.add(LexicalType.END);
        firstSet.add(LexicalType.IF);
        firstSet.add(LexicalType.WHILE);
        firstSet.add(LexicalType.DO);
        firstSet.add(LexicalType.NL);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new Program(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        if (StmtListNode.isFirst(first)) {
            handler = StmtListNode.getHandler(first, env);
            handler.parse();
        }
        return true;
    }
    
    @Override
    public String toString(){
        return handler.toString();
    }
    
    @Override
    public Value getValue() throws Exception{
        return handler.getValue();
    }
     
}
