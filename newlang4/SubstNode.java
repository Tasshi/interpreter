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
public class SubstNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    VariableNode handler;
    Node handler2;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();
    static {
        firstSet.add(LexicalType.NAME);
    }

    public SubstNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new SubstNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
     
        if (VariableNode.isFirst(first)) {
       
            handler = (VariableNode)VariableNode.getHandler(first, env);
            handler.parse();
        }
        
        first = env.getInput().get();
        if (first.getType() != LexicalType.EQ) {
            return false;
        }

        first = env.getInput().get();
        if (ExprNode.isFirst(first)) {
            handler2 = ExprNode.getHandler(first, env);
            handler2.parse();
        }
        return true;
    }

    @Override
    public String toString() {
        return handler.toString() + "=" + handler2.toString();
    }
    
    @Override
    public Value getValue() throws Exception{
        handler.setValue(handler2.getValue());
        return null;
    }
    
}
