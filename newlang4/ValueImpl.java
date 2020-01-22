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
public class ValueImpl extends Value {

    private String target;
    private ValueType type;
    private boolean flag;
    
    // private LexicalType val;
    public ValueImpl(String s) {
        super(s);
    }

    public ValueImpl(int i) {
       super(i);
        target=target.valueOf(i);
       type = ValueType.INTEGER;
    }

    public ValueImpl(double d) {
       super(d);
    }

    public ValueImpl(boolean b) {
       super(b);
       target=target.valueOf(b);
       type = ValueType.BOOL;
    }

    public ValueImpl(String s, ValueType t) {
        super(s,t);
        target = s;
        type = t;
    }
    
     public ValueImpl(int i, ValueType t) {
        super();
        target = String.valueOf(i);
        type = t;
    }

     
      public ValueImpl(double d, ValueType t) {
        super();
        target = String.valueOf(d);
        type = t;
    }
     
    @Override
    public String get_sValue() {
        return target;
    }

    @Override
    public String getSValue() {
       return target;
    }

    @Override
    public int getIValue() {
        return Integer.parseInt(target);
    }

    @Override
    public double getDValue() {
        return Double.parseDouble(target);
    }

    @Override
    public boolean getBValue() {
       return Boolean.parseBoolean(target);
    }

    @Override
    public ValueType getType() {
      return type;
    }
    
    public String toString(){
        return get_sValue();
    }

}
