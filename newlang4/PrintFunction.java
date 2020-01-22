/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

/**
 *
 * @author c0117200
 */
public class PrintFunction extends Function{
    

    PrintFunction() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     public Value invoke(ExprListNode arg) throws Exception{
         System.out.println(arg.getNode(0).getValue());
    	return null;        
    }
    
}
