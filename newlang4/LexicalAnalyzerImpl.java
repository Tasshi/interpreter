package newlang4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author c0117200
 */
public class LexicalAnalyzerImpl implements LexicalAnalyzer {

    private PushbackReader reader;
    static Map<String, LexicalType> lexcical = new HashMap<>();
    static Map<String, LexicalType> special = new HashMap<>();
    static ArrayList<LexicalUnit> tokenList = new ArrayList<LexicalUnit>();

    //staticイニシャライザー
    static {
        lexcical.put("IF", LexicalType.IF);
        lexcical.put("THEN", LexicalType.THEN);
        lexcical.put("ELSE", LexicalType.ELSE);
        lexcical.put("ELSEIF", LexicalType.ELSEIF);
        lexcical.put("ENDIF", LexicalType.ENDIF);
        lexcical.put("FOR", LexicalType.FOR);
        lexcical.put("FORALL", LexicalType.FORALL);
        lexcical.put("NEXT", LexicalType.NEXT);
        lexcical.put("FUNC", LexicalType.FUNC);
        lexcical.put("DIM", LexicalType.DIM);
        lexcical.put("AS", LexicalType.AS);
        lexcical.put("END", LexicalType.END);
        lexcical.put("WHILE", LexicalType.WHILE);
        lexcical.put("DO", LexicalType.DO);
        lexcical.put("UNTIL", LexicalType.UNTIL);
        lexcical.put("LOOP", LexicalType.LOOP);
        lexcical.put("TO", LexicalType.TO);
        lexcical.put("WEND", LexicalType.WEND);

        special.put("=", LexicalType.EQ);
        special.put("<", LexicalType.LT);
        special.put(">", LexicalType.GT);
        special.put("<=", LexicalType.LE);
        special.put("=<", LexicalType.LE);
        special.put(">=", LexicalType.GE);
        special.put("=>", LexicalType.GE);
        special.put("<>", LexicalType.NE);
        special.put("\n", LexicalType.NL);
        special.put(".", LexicalType.DOT);
        special.put("+", LexicalType.ADD);
        special.put("-", LexicalType.SUB);
        special.put("*", LexicalType.MUL);
        special.put("/", LexicalType.DIV);
        special.put("(", LexicalType.LP);
        special.put(")", LexicalType.RP);
        special.put("comma", LexicalType.COMMA);
    }

    public LexicalAnalyzerImpl(String fname) {
        try {
            FileReader fr;
            fr = new FileReader(fname);
            reader = new PushbackReader(fr);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LexicalAnalyzerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public LexicalUnit get() throws Exception {
        if (tokenList.isEmpty()) {
            int code = reader.read();
            if (-1 == code) {
                return new LexicalUnit(LexicalType.EOF);
            }
            char readchar = (char) code;

            //空白かtabかcrか(\r)
            while (readchar == ' ' || readchar == '\t' || readchar == '\r') {
                code = reader.read();
                if (-1 == code) {
                    return new LexicalUnit(LexicalType.EOF);
                }
                readchar = (char) code;
            }

            //アルファベットかを判別
            if (readchar >= 'a' && readchar <= 'z' || readchar >= 'A' && readchar <= 'Z') {
                reader.unread(code);
                return StringGet();
            }

            //数字を判別
            if (readchar >= '0' && readchar <= '9') {
                reader.unread(code);
                return intget();
            }

            //文字列を判別
            if (readchar == '"') {
                return literalGet();
            }
            reader.unread(code);
            return specialGet();
        } else {
            LexicalUnit ret;
            ret = tokenList.get(tokenList.size() - 1);
            tokenList.remove(tokenList.size() - 1);
            return ret;
        }
    }

    public void unget(LexicalUnit token) throws Exception {
        tokenList.add(token);
    }

    @Override
    public boolean expect(LexicalType type) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private LexicalUnit StringGet() throws IOException {
        String target = "";
        while (true) {
            int ci = reader.read();
            if (ci < 0) {
                break;
            }
            char c = (char) ci;
            if ((c >= 'a') && (c <= 'z')
                    || (c >= 'A') && (c <= 'Z') || ('0' <= c) && ('9' >= c)) {
                target += c;
                continue;
            }
            reader.unread(ci);
            break;
        }
        if (lexcical.containsKey(target.toUpperCase())) {

            return new LexicalUnit(lexcical.get(target.toUpperCase()));
            // return new LexicalUnit(lexcical.get(target),new ValueImpl(target, ValueType.STRING));
        } else {
            return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING));
        }
    }

    private LexicalUnit intget() throws IOException {

        String match = "";
        String target = "";

        boolean point = false;
        while (true) {
            int ci = reader.read();
            if (ci < 0) {
                break;
            }
            char c = (char) ci;
            match = "" + (char) ci;
            if (match.matches("[0-9]*")) {
                target += match;
                continue;
            } else if ((c == '.') && (point == false)) {
                point = true;
                target += match;
                continue;
            }
            reader.unread(ci);
            break;
        }
        if (point == false) {
            return new LexicalUnit(LexicalType.INTVAL,
                    new ValueImpl(target, ValueType.INTEGER));
        } else {
            return new LexicalUnit(LexicalType.DOUBLEVAL,
                    new ValueImpl(target, ValueType.DOUBLE));
        }
    }

    private LexicalUnit literalGet() throws IOException {
        String target = "";
        while (true) {
            int ci = reader.read();
            if (ci < 0) {
                break;
            }
            char c = (char) ci;
            if (c != '"') {
                target += c;
                continue;
            } else {
                break;
            }
        }
        return new LexicalUnit(LexicalType.LITERAL,
                new ValueImpl(target, ValueType.STRING));
    }

    private LexicalUnit specialGet() throws IOException {
        String target = "";
        int ci = reader.read();
        char c = (char) ci;

        if ((c == '=') || (c == '<') || (c == '>')) {
            target += c;
            ci = reader.read();
            c = (char) ci;
            if (special.containsKey(target + c)) {
                return new LexicalUnit(special.get(target + c));
            } else {
                reader.unread(ci);
            }
        } else {
            target += c;
        }
        return new LexicalUnit(special.get(target));
    }

}
