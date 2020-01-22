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
public class EndNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;

    public EndNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.END);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new EndNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        if (isFirst(first)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    public Value getValue() throws Exception {
        System.exit(0);
        return null;
    }

}
