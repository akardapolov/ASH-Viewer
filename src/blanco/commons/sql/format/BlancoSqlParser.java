/*
 * blanco Framework
 * Copyright (C) 2004-2006 WATANABE Yoshinori
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.commons.sql.format;

import java.util.ArrayList;
import java.util.List;

import blanco.commons.sql.format.valueobject.BlancoSqlToken;

/**

 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * 2005.08.12 Tosiki Iga: 
 * 2005.08.12 Tosiki Iga: 
 * 
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlParser {

    private String fBefore;

  
    private char fChar;

   
    private int fPos;

   
    private static final String[] twoCharacterSymbol = { "<>", "<=", ">=", "||" };

    public BlancoSqlParser() {
    }

    public static boolean isSpace(final char argChar) {
        // 2005.07.26 Tosiki Iga 
        // 2005.08.12 Tosiki Iga 
        return argChar == ' ' || argChar == '\t' || argChar == '\n'
                || argChar == '\r' || argChar == 65535;
    }

    
    public static boolean isLetter(final char argChar) {
        
        if (isSpace(argChar)) {
            return false;
        }
        if (isDigit(argChar)) {
            return false;
        }
        if (isSymbol(argChar)) {
            return false;
        }
        return true;
    }

    
    public static boolean isDigit(final char argChar) {
        return '0' <= argChar && argChar <= '9';
    }

  
    public static boolean isSymbol(final char argChar) {
        switch (argChar) {
        case '"': // double quote
        case '?': // question mark
        case '%': // percent
        case '&': // ampersand
        case '\'': // quote
        case '(': // left paren
        case ')': // right paren
        case '|': // vertical bar
        case '*': // asterisk
        case '+': // plus sign
        case ',': // comma
        case '-': // minus sign
        case '.': // period
        case '/': // solidus
        case ':': // colon
        case ';': // semicolon
        case '<': // less than operator
        case '=': // equals operator
        case '>': // greater than operator

          
            // case '!':
            // case '$':
            // case '[':
            // case '\\':
            // case ']':
            // case '^':
            // case '{':
            // case '}':
            // case '~':
            return true;
        default:
            return false;
        }
    }

    BlancoSqlToken nextToken() {
        int start_pos = fPos;
        if (fPos >= fBefore.length()) {
            fPos++;
            return new BlancoSqlToken(BlancoSqlTokenConstants.END, "",
                    start_pos);
        }

        fChar = fBefore.charAt(fPos);

        if (isSpace(fChar)) {
            String workString = "";
            for (;;) {
                workString += fChar;
                fChar = fBefore.charAt(fPos);
                if (!isSpace(fChar)) {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.SPACE,
                            workString, start_pos);
                }
                fPos++;
                if (fPos >= fBefore.length()) {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.SPACE,
                            workString, start_pos);
                }
            }
        } else if (fChar == ';') {
            fPos++;
             return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, ";",
                    start_pos);
        } else if (isDigit(fChar)) {
            String s = "";
            while (isDigit(fChar) || fChar == '.') {
                // if (ch == '.') type = Token.REAL;
                s += fChar;
                fPos++;

                if (fPos >= fBefore.length()) {
                    
                    break;
                }

                fChar = fBefore.charAt(fPos);
            }
            return new BlancoSqlToken(BlancoSqlTokenConstants.VALUE, s,
                    start_pos);
        } else if (isLetter(fChar)) {
            String s = "";
            
            while (isLetter(fChar) || isDigit(fChar) || fChar == '.') {
                s += fChar;
                fPos++;
                if (fPos >= fBefore.length()) {
                    break;
                }

                fChar = fBefore.charAt(fPos);
            }
            for (int i = 0; i < BlancoSqlConstants.SQL_RESERVED_WORDS.length; i++) {
                if (s
                        .compareToIgnoreCase(BlancoSqlConstants.SQL_RESERVED_WORDS[i]) == 0) {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.KEYWORD,
                            s, start_pos);
                }
            }
            return new BlancoSqlToken(BlancoSqlTokenConstants.NAME, s,
                    start_pos);
        }
        // single line comment
        else if (fChar == '-') {
            fPos++;
            char ch2 = fBefore.charAt(fPos);
            
            if (ch2 != '-') {
                return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, "-",
                        start_pos);
            }
            fPos++;
            String s = "--";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\n' || fPos >= fBefore.length()) {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.COMMENT,
                            s, start_pos);
                }
            }
        }
        
        else if (fChar == '/') {
            fPos++;
            char ch2 = fBefore.charAt(fPos);
            
            if (ch2 != '*') {
                return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, "/",
                        start_pos);
            }

            String s = "/*";
            fPos++;
            int ch0 = -1;
            for (;;) {
                ch0 = fChar;
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (ch0 == '*' && fChar == '/') {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.COMMENT,
                            s, start_pos);
                }
            }
        } else if (fChar == '\'') {
            fPos++;
            String s = "'";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\'') {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.VALUE, s,
                            start_pos);
                }
            }
        } else if (fChar == '\"') {
            fPos++;
            String s = "\"";
            for (;;) {
                fChar = fBefore.charAt(fPos);
                s += fChar;
                fPos++;
                if (fChar == '\"') {
                    return new BlancoSqlToken(BlancoSqlTokenConstants.NAME, s,
                            start_pos);
                }
            }
        }

        else if (isSymbol(fChar)) {
            
            String s = "" + fChar;
            fPos++;
            if (fPos >= fBefore.length()) {
                return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, s,
                        start_pos);
            }
           
            char ch2 = fBefore.charAt(fPos);
            for (int i = 0; i < twoCharacterSymbol.length; i++) {
                if (twoCharacterSymbol[i].charAt(0) == fChar
                        && twoCharacterSymbol[i].charAt(1) == ch2) {
                    fPos++;
                    s += ch2;
                    break;
                }
            }
            return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, s,
                    start_pos);
        } else {
            fPos++;
            return new BlancoSqlToken(BlancoSqlTokenConstants.UNKNOWN, ""
                    + fChar, start_pos);
        }
    }

    
    public List<BlancoSqlToken> parse(final String argSql) {
        fPos = 0;
        fBefore = argSql;

        final List<BlancoSqlToken> list = new ArrayList<BlancoSqlToken>();
        for (;;) {
            final BlancoSqlToken token = nextToken();
            if (token.getType() == BlancoSqlTokenConstants.END) {
                break;
            }

            list.add(token);
        }
        return list;
    }
}
