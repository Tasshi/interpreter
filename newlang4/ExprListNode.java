/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author c0117200
 */
public class ExprListNode extends Node {

    Value v;
    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    ArrayList<Node> handlerList = new ArrayList<Node>();

    public ExprListNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();
    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.INTVAL);
        firstSet.add(LexicalType.DOUBLEVAL);
        firstSet.add(LexicalType.LITERAL);

    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new ExprListNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        handler = ExprNode.getHandler(first, env);
        handler.parse();
        handlerList.add(handler);
        loop:
        while (true) {
            first = env.getInput().get();
            if (first.getType() != LexicalType.COMMA) {
                if (ExprNode.isFirst(first)) {
                    handler = ExprNode.getHandler(first, env);
                    handler.parse();
                    handlerList.add(handler);
                } else {
                    env.getInput().unget(first);
                    break loop;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return handlerList.toString();
    }
    
    
    public Node getNode(int n){
        return  handlerList.get(n);
    }
    
    public Value getValue(int n) throws Exception{
        if(handlerList.size() > n){
            return handlerList.get(n).getValue();
        }
        return null;
    }
    
}
