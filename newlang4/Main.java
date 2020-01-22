package newlang4;

import java.io.FileInputStream;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        LexicalAnalyzer lex;
        LexicalUnit first;
        Environment env;

        System.out.println("basic parser");
        lex = new LexicalAnalyzerImpl("test.bas") ;
        env = new Environment(lex);
        first = lex.get();
        
        if (Program.isFirst(first)) {
            Node handler = Program.getHandler(first, env);
            handler.parse();
            System.out.println(handler.toString());
            System.out.println("___ここから実行系____");
            System.out.println("value = " + handler.getValue());
        } else {
            System.out.println("syntax error");
        }
    }
}
