/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

import java.util.HashSet;
import java.util.Set;
import static newlang4.ExprListNode.firstSet;

/**
 *
 * @author c0117200
 */
public class CondNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    LexicalUnit second;
    LexicalUnit operator;
    LexicalType ope;
    Environment env;
    Node handler;
    Node handler2;
    Value v1;
    Value v2;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.INTVAL);
        firstSet.add(LexicalType.DOUBLEVAL);
        firstSet.add(LexicalType.LITERAL);
    }

    public CondNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new CondNode(lu, env);
    }

    @Override
    public boolean parse() throws Exception {
        if (ExprNode.isFirst(first)) {
            handler = ExprNode.getHandler(first, env);
            handler.parse();
        }
      
        first = env.getInput().get();
        
        operator = first;

        first = env.getInput().get();

        if (ExprNode.isFirst(first)) {
            handler2 = ExprNode.getHandler(first, env);
            handler2.parse();
        }
        return true;
    }

    public void setValue(Node handler, Node handler2, LexicalType ope) {
        this.handler = handler;
        this.handler2 = handler2;
        operator = new LexicalUnit(ope);
    }

    @Override
    public String toString() {
        return handler.toString() + " " + operator.getType() + " " + handler2.toString();
    }

    @Override
    public Value getValue() throws Exception {
        v1 = handler.getValue();
        v2 = handler2.getValue();
        
       

        if (v1.getType() == ValueType.STRING && v2.getType() == ValueType.STRING) {
            if (operator.getType() == LexicalType.EQ) {
                return new ValueImpl(v1.getSValue() == v2.getSValue());
            } else if (operator.getType() == LexicalType.NE) {
                return new ValueImpl(v1.getSValue() != v2.getSValue());
            } else {
                System.out.println("文字列は比較しかできません");
                System.exit(1);
            }
        }

        if (v1.getType() == ValueType.STRING || v2.getType() == ValueType.STRING) {
            System.out.println("片方が文字列で比較できません");
            System.exit(1);
        }

        if (operator.getType() == LexicalType.LT) {
            return new ValueImpl(v1.getDValue() < v2.getDValue());
        } else if (operator.getType() == LexicalType.GT) {
            return new ValueImpl(v1.getDValue() > v2.getDValue());
        } else if (operator.getType() == LexicalType.LE) {
            return new ValueImpl(v1.getDValue() <= v2.getDValue());
        } else if (operator.getType() == LexicalType.GE) {
            return new ValueImpl(v1.getDValue() >= v2.getDValue());
        } else if (operator.getType() == LexicalType.EQ) {
            return new ValueImpl(v1.getDValue() == v2.getDValue());
        } else if (operator.getType() == LexicalType.NE) {
            return new ValueImpl(v1.getDValue() != v2.getDValue());
        }
        System.out.println("比較できません");
        System.exit(1);
        return null;
    }
}
