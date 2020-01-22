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
public class BlockNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();
    static {
        firstSet.add(LexicalType.DO);
        firstSet.add(LexicalType.WHILE);
        firstSet.add(LexicalType.IF);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) throws Exception {
        if (IfNode.isFirst(lu)) {
            return new IfNode(lu, env);
        }
        
        if (LoopNode.isFirst(lu)) {
            return new LoopNode(lu, env);
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
