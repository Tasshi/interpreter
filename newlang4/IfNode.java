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
public class IfNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    Node handler2;
    Node handler3;
    boolean el = false;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.IF);
        firstSet.add(LexicalType.ELSEIF);
    }

    public IfNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new IfNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
       
        //if_prefix
        first = env.getInput().get();
        if (CondNode.isFirst(first)) {
            handler = CondNode.getHandler(first, env);
            handler.parse();
        }
               
        // THEN
        first = env.getInput().get();
        if (first.getType() != LexicalType.THEN) {
            return false;
        }
        first = env.getInput().get();
        

        // NL or Stmt
        // NL
        if (first.getType() == LexicalType.NL) {
            el = true;
            first = env.getInput().get();
            // StmtList
            handler2 = StmtListNode.getHandler(first, env);
            handler2.parse();
            first = env.getInput().get();

          
            // ELSE_Block
            if ((first.type) == LexicalType.ELSEIF) {
                handler3 = IfNode.getHandler(first, env);
                handler3.parse();
            } else if ((first.type) == LexicalType.ELSE) {
                first = env.getInput().get();
                handler3 = StmtListNode.getHandler(first, env);
                handler3.parse();
            } else {
                first = env.getInput().get();
                handler3 = null;
            }
        } else {

            //NLでない場合
            // Stmt
            if (StmtListNode.isFirst(first)) {
                handler2 = StmtListNode.getHandler(first, env);
                handler2.parse();
            }

            first = env.getInput().get();

            // ELSE & Stmt 
            if (first.getType() == LexicalType.ELSE) {
                el = true;
                first = env.getInput().get();
                if (StmtNode.isFirst(first)) {
                    handler3 = StmtNode.getHandler(first, env);
                    handler3.parse();
                }
            }
        }
        if (first.getType() != LexicalType.NL) {
            return false;
        }
        first = env.getInput().get();
        return true;
    }

    @Override
    public String toString() {
        if (el == false) {
            return handler.toString() + handler2.toString();
        } else if(handler3== null){
            return handler.toString() + "  " + handler2.toString() ;
        }else{
               return handler.toString() + "  " + handler2.toString() + " ELSE " + handler3.toString();
        }
    }

    @Override
    public Value getValue() throws Exception {
        if (handler.getValue().getBValue()) {
            return handler2.getValue();
        } else if(handler3==null){
          return null;
        }else{
              return handler3.getValue();
        }
    }
}
