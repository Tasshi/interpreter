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
public class ForNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    Node handler2;
    Node handler3;
    Node handler4;
    Node handler5;

    public ForNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.FOR);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new ForNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        first = env.getInput().get();
        if (SubstNode.isFirst(first)) {
            handler = SubstNode.getHandler(first, env);
            handler.parse();
        }

        first = env.getInput().get();
        if (first.getType() != LexicalType.TO) {
            return false;
        }
        first = env.getInput().get();

        if (ConstNode.isFirst(first)) {
            handler2 = ConstNode.getHandler(first, env);
            handler2.parse();
        }

        first = env.getInput().get();
        if (first.getType() != LexicalType.NL) {
            return false;
        }
        first = env.getInput().get();
        if (StmtListNode.isFirst(first)) {
            handler3 = StmtListNode.getHandler(first, env);
            handler3.parse();
        }

        first = env.getInput().get();

        if (first.getType() != LexicalType.NEXT) {
            return false;
        }
        first = env.getInput().get();

        if (VariableNode.isFirst(first)) {
            handler4 = VariableNode.getHandler(first, env);
            handler4.parse();
        }
        return true;
    }

    @Override
    public String toString() {
        return handler.toString() + "to"
                + handler2.toString()
                + handler3.toString()
                + handler4.toString();
    }

    @Override
    public Value getValue() throws Exception {
        handler.getValue();
        handler5 = CondNode.getHandler(null, handler2.env);
        handler5.setValue(handler2, handler4, LexicalType.GE);
      
        while (handler5.getValue().getBValue()) {
            handler3.getValue();
            handler4.setValue(new ValueImpl(handler4.getValue().getIValue()+1));
        }
      
        return null;
    }
}
