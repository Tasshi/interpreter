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
public class StmtListNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Node handler;
    ArrayList<Node> handlerList = new ArrayList<Node>();

    public StmtListNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.FOR);
        firstSet.add(LexicalType.END);
        firstSet.add(LexicalType.IF);
        firstSet.add(LexicalType.WHILE);
        firstSet.add(LexicalType.DO);
        firstSet.add(LexicalType.NL);
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new StmtListNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        loop:
        while (true) {
            if (first.getType() != LexicalType.NL) {
                if (StmtNode.isFirst(first)) {
                    handler = StmtNode.getHandler(first, env);
                    handler.parse();
                    handlerList.add(handler);
                } else if (BlockNode.isFirst(first)) {
                    handler = BlockNode.getHandler(first, env);           
                    handler.parse();
                    handlerList.add(handler);
                } else {
                    env.getInput().unget(first);
                    break loop;
                }
            }
            first = env.getInput().get();
        }
        return false;
    }

    @Override
    public String toString() {
        return handlerList.toString();
    }
    
     @Override
    public Value getValue() throws Exception{
        for(Node node: handlerList){
              node.getValue();
        }
        return null;
    }
}
