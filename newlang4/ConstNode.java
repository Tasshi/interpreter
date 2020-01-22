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
public class ConstNode extends Node{
    
    LexicalAnalyzer lex;
    LexicalUnit first;
    Environment env;
    Value value;
    
     static final Set<LexicalType> firstSet = new HashSet<LexicalType>() ;
    
     static {
        firstSet.add(LexicalType.NAME);
        firstSet.add(LexicalType.INTVAL);
        firstSet.add(LexicalType.DOUBLEVAL);
        firstSet.add(LexicalType.LITERAL);
    }
     
      public ConstNode(LexicalUnit lu, Environment env) {
        first = lu;
        this.env = env;
    }
     
    public static boolean isFirst(LexicalUnit lu) {
        return firstSet.contains(lu.getType());
    }
    
     public static Node getHandler(LexicalUnit lu, Environment env) {
         return new ConstNode(lu, env);
     }

    @Override
    public boolean parse() throws Exception {
        if(isFirst(first)){
           value= first.getValue();
        }
        return true;
    }    
    
    @Override
     public String toString(){
        return value.toString();
    }
     
     @Override
     public Value getValue(){
         return value;
     }
}
