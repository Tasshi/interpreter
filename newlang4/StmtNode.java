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
public class StmtNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.FOR);
        firstSet.add(LexicalType.END);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) throws Exception {
        LexicalUnit second;
        if (EndNode.isFirst(lu)) {
            return new EndNode(lu, env);
        }
        if(ForNode.isFirst(lu)){
            return new ForNode(lu, env);
        }
        
        second = env.getInput().get();
        env.getInput().unget(second);
        if (second.getType() == LexicalType.EQ) {           
            if (SubstNode.isFirst(lu)) {
                return new SubstNode(lu, env);
            }
        } else {
            if (CallSubNode.isFirst(lu)) {
                return new CallSubNode(lu, env);
            }
        }
        return null;
    }

    @Override
    public boolean parse() throws Exception {
        return true;
    }

    @Override
    public String toString() {
        return handler.toString();
    }
}
