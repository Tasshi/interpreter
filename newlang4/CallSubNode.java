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
public class CallSubNode extends Node {

    String function = "";
    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    ExprListNode handler2;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
    }

    public CallSubNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new CallSubNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        if (ConstNode.isFirst(first)) {
            handler = ConstNode.getHandler(first, env);
            handler.parse();
            function = handler.toString();
        }
        
        first = env.getInput().get();
     
        if (first.getType() == LexicalType.LP) {
            first = env.getInput().get();
            if (ExprListNode.isFirst(first)) {
                handler2 = (ExprListNode) ExprListNode.getHandler(first, env);
                handler2.parse();
            }
            first = env.getInput().get();
        } else {
            if (ExprListNode.isFirst(first)) {
                handler2 = (ExprListNode) ExprListNode.getHandler(first, env);
                handler2.parse();
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return handler.toString() + " " + handler2.toString();
    }

    @Override
    public Value getValue() throws Exception {
        return env.getFunction(function).invoke(handler2);
    }
}
