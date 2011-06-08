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
 * BlancoSqlFormatter: SQL整形ツール. SQL文を決められたルールに従い整形します。
 * 
 * フォーマットを実施するためには、入力されるSQLがSQL文として妥当であることが前提条件となります。
 * 
 * このクラスが準拠するSQL整形のルールについては、下記URLを参照ください。
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * このクラスは SQL文を解析する部分です。<br>
 * 2005.08.12 Tosiki Iga: いくつかのユーティリティメソッドを public static化しました。<br>
 * 2005.08.12 Tosiki Iga: 65535(もとは-1)はホワイトスペースとして扱うよう変更します。
 * 
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlParser {

    /**
     * 解析前の文字列
     */
    private String fBefore;

    /**
     * 解析中の文字。
     */
    private char fChar;

    /**
     * 解析中の位置
     */
    private int fPos;

    /**
     * ２文字からなる記号。
     * 
     * なお、|| は文字列結合にあたります。
     */
    private static final String[] twoCharacterSymbol = { "<>", "<=", ">=", "||" };

    /**
     * パーサのインスタンスを作成します。
     */
    public BlancoSqlParser() {
    }

    /**
     * 与えられた文字が、ホワイトスペース文字かどうかを判定します。
     * 
     * @param argChar
     * @return
     */
    public static boolean isSpace(final char argChar) {
        // 2005.07.26 Tosiki Iga \r も処理範囲に含める必要があります。
        // 2005.08.12 Tosiki Iga 65535(もとは-1)はホワイトスペースとして扱うよう変更します。
        return argChar == ' ' || argChar == '\t' || argChar == '\n'
                || argChar == '\r' || argChar == 65535;
    }

    /**
     * 文字として認識して妥当かどうかを判定します。
     * 
     * 全角文字なども文字として認識を許容するものと判断します<br>
     * ※このメソッドはBlancoSqlEditorPluginから参照されます。
     * 
     * @param argChar
     * @return
     */
    public static boolean isLetter(final char argChar) {
        // SQLにおいて アンダースコアは英字の仲間です.
        // blanco において # は英字の仲間です.
        // ここに日本語も含めなくてはならない。
        // return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')
        // || (c == '_' || c == '#');
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

    /**
     * 数字かどうかを判定します。
     * 
     * @param argChar
     * @return
     */
    public static boolean isDigit(final char argChar) {
        return '0' <= argChar && argChar <= '9';
    }

    /**
     * 記号かどうかを判定します。
     * 
     * @param argChar
     * @return
     */
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

            // blancoでは # は文字列の一部です case '#':
            // アンダースコアは記号とは扱いません case '_': //underscore
            // これ以降の文字の扱いは保留
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

    /**
     * トークンを次に進めます。
     * 
     * posを進める。sに結果を返す。typeにその種類を設定する。
     * 
     * 不正なSQLの場合、例外が発生します。 ここでは、文法チェックは行っていない点に注目してください。
     * 
     * @return トークンを返す.
     */
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
            // 2005.07.26 Tosiki Iga セミコロンは終了扱いではないようにする。
            return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, ";",
                    start_pos);
        } else if (isDigit(fChar)) {
            String s = "";
            while (isDigit(fChar) || fChar == '.') {
                // if (ch == '.') type = Token.REAL;
                s += fChar;
                fPos++;

                if (fPos >= fBefore.length()) {
                    // 長さを超えている場合には処理中断します。
                    break;
                }

                fChar = fBefore.charAt(fPos);
            }
            return new BlancoSqlToken(BlancoSqlTokenConstants.VALUE, s,
                    start_pos);
        } else if (isLetter(fChar)) {
            String s = "";
            // 文字列中のドットについては、文字列と一体として考える。
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
            // -- じゃなかったとき
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
        // マルチラインコメント
        else if (fChar == '/') {
            fPos++;
            char ch2 = fBefore.charAt(fPos);
            // /* じゃなかったとき
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
            // 記号
            String s = "" + fChar;
            fPos++;
            if (fPos >= fBefore.length()) {
                return new BlancoSqlToken(BlancoSqlTokenConstants.SYMBOL, s,
                        start_pos);
            }
            // ２文字の記号かどうか調べる
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

    /**
     * SQL文字列をトークンの配列に変換します。
     * 
     * @param argSql
     *            変換前のSQL文
     * @return Tokenの配列
     */
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
