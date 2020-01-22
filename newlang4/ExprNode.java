/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

import java.util.HashSet;
import java.util.Set;
import static newlang4.ConstNode.firstSet;
import static newlang4.LexicalType.*;

/**
 *
 * @author c0117200
 */
public class ExprNode extends Node {

    LexicalAnalyzer lex;
    LexicalUnit first;
    LexicalUnit second;
    LexicalUnit operator = null;
    Environment env;
    Node left_operand;
    Node right_operand;
    Value v1;
    Value v2;
    boolean ope = false;
    boolean call = false;

    static final Set<LexicalType> firstSet = new HashSet<LexicalType>();
    static final Set<LexicalType> operatorSet = new HashSet<LexicalType>();

    static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.INTVAL);
        firstSet.add(LexicalType.DOUBLEVAL);
        firstSet.add(LexicalType.LITERAL);
        operatorSet.add(LexicalType.ADD);
        operatorSet.add(LexicalType.SUB);
        operatorSet.add(LexicalType.MUL);
        operatorSet.add(LexicalType.DIV);
    }

    public ExprNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }

    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }

    public static boolean isOperator(LexicalUnit lu) {
        return operatorSet.contains(lu.getType());
    }

    public static Node getHandler(LexicalUnit lu, Environment env) {
        return new ExprNode(lu, env);
    }

    public boolean parse() throws Exception {
        second = env.getInput().get();
        env.getInput().unget(second);
        if (second.getType() == LexicalType.LP) {
            if (CallSubNode.isFirst(first)) {
                left_operand = CallSubNode.getHandler(first, env);
                left_operand.parse();
            }
        } else if (VariableNode.isFirst(first)) {
            left_operand = VariableNode.getHandler(first, env);
            left_operand.parse();
        } else if (ConstNode.isFirst(first)) {
            left_operand = ConstNode.getHandler(first, env);
            left_operand.parse();
        }

        first = env.getInput().get();
        if (isOperator(first)) {
            ope = true;
            operator = first;
            first = env.getInput().get();
            second = env.getInput().get();
            env.getInput().unget(second);
            if (second.getType() == LexicalType.LP) {
                if (CallSubNode.isFirst(first)) {
                    right_operand = CallSubNode.getHandler(first, env);
                    right_operand.parse();
                }
            } else if (VariableNode.isFirst(first)) {
                right_operand = VariableNode.getHandler(first, env);
                right_operand.parse();
            } else if (ConstNode.isFirst(first)) {
                right_operand = ConstNode.getHandler(first, env);
                right_operand.parse();
            }
        } else {
            env.getInput().unget(first);
        }
        return true;
    }

    @Override
    public String toString() {
        if (ope == true) {
            return left_operand.toString() + operator.type + right_operand.toString();
        } else {
            return left_operand.toString();
        }
    }

    @Override
    public Value getValue() throws Exception {
        if (operator == null) {
            return left_operand.getValue();
        }

        v1 = left_operand.getValue();
        v2 = right_operand.getValue();
     
        if (v1.getType() == ValueType.STRING || v2.getType() == ValueType.STRING) {
            if (operator.getType() == LexicalType.ADD) {
                return new ValueImpl(v1.getSValue() + v2.getSValue(), ValueType.STRING);
            }
        }

        if (v1.getType() == ValueType.INTEGER && v2.getType() == ValueType.INTEGER) {
            if (operator.getType() == LexicalType.ADD) {
                return new ValueImpl(v1.getIValue() + v2.getIValue(), ValueType.INTEGER);
            } else if (operator.getType() == LexicalType.SUB) {
                return new ValueImpl(v1.getIValue() - v2.getIValue(), ValueType.INTEGER);
            } else if (operator.getType() == LexicalType.MUL) {
                return new ValueImpl(v1.getIValue() * v2.getIValue(), ValueType.INTEGER);
            } else if (operator.getType() == LexicalType.DIV) {
                return new ValueImpl(v1.getIValue() / v2.getIValue(), ValueType.INTEGER);
            }
        }

        if (v1.getType() == ValueType.DOUBLE || v2.getType() == ValueType.DOUBLE) {
            if (operator.getType() == LexicalType.ADD) {
                return new ValueImpl(v1.getDValue() + v2.getDValue(), ValueType.DOUBLE);
            } else if (operator.getType() == LexicalType.SUB) {
                return new ValueImpl(v1.getDValue() - v2.getDValue(), ValueType.DOUBLE);
            } else if (operator.getType() == LexicalType.MUL) {
                return new ValueImpl(v1.getDValue() * v2.getDValue(), ValueType.DOUBLE);
            } else if (operator.getType() == LexicalType.DIV) {
                return new ValueImpl(v1.getDValue() / v2.getDValue(), ValueType.DOUBLE);
            }
        }
        return null;
    }
}
