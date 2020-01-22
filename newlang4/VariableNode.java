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
public class VariableNode extends Node {

    Value v;
    int i;
    String var_name;
    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    Value value;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
    }

    public VariableNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public VariableNode(String vname) {
        var_name = vname;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return env.getVariable(lu.value.getSValue());
      //  return new VariableNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        
        return true;
    }

    @Override
    public String toString() {
        return var_name;
    }

    @Override
    public Value getValue() throws Exception {
        return value;
    }

    @Override
    public void setValue(Value my_v) {
        value = my_v;
    }

}
