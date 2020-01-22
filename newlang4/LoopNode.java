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
public class LoopNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    Node handler2;
    boolean conditions = false;
    boolean isDo = false;
    boolean isUntil = false;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.WHILE);
        firstSet.add(LexicalType.DO);
    }

    public LoopNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new LoopNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        // WHILE
        if (first.getType() == LexicalType.WHILE) {
            first = env.getInput().get();
            // cond
            handler = CondNode.getHandler(first, env);
            handler.parse();
            first = env.getInput().get();
            // NL
            if (first.getType() != LexicalType.NL) {
                return false;
            }
            //StmtList
            handler2 = StmtListNode.getHandler(first, env);
            handler2.parse();
            first = env.getInput().get();
            if (first.getType() != LexicalType.WEND) {
                return false;
            }
            first = env.getInput().get();
            if (first.getType() != LexicalType.NL) {
                return false;
            }
            //Do
        } else if (first.getType() == LexicalType.DO) {
            isDo = true;
            first = env.getInput().get();
            if (first.getType() != LexicalType.NL) {
                getConditions();
            }
            first = env.getInput().get();
            handler2 = StmtListNode.getHandler(first, env);
            handler2.parse();
            first = env.getInput().get();

            // LOOP
            if (first.getType() != LexicalType.LOOP) {
                return false;
            }

            if (conditions == false) {
                first = env.getInput().get();
                getConditions();
            }

            // NL
            first = env.getInput().get();
            if (first.getType() != LexicalType.NL) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return handler.toString() + handler2.toString();
    }

    private void getConditions() throws Exception {
        conditions = true;
        //WHILE
        if (first.getType() == LexicalType.WHILE) {
            first = env.getInput().get();
            //While cond
            handler = CondNode.getHandler(first, env);
            handler.parse();

        } else if (first.getType() == LexicalType.UNTIL) {
            //While cond
            isUntil = true;
            first = env.getInput().get();
            handler = CondNode.getHandler(first, env);
            handler.parse();
        }
        first = env.getInput().get();
        if (first.getType() != LexicalType.NL) {
        }

    }

    @Override
    public Value getValue() throws Exception {
        handler.getValue();
        if(isDo == true){
            handler2.getValue();
        }
        
        while(true){
            if(Judgment()){
                return null;
            }
            handler2.getValue();
        }    
    }
    
    private boolean Judgment() throws Exception{
        if(handler.getValue().getBValue() ==true && isUntil == false 
         ||handler.getValue().getBValue() == false && isUntil == true){
            return  false;
        } else{
            return true;
        }
    }
}
