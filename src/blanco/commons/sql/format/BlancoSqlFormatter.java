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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Stack;

import blanco.commons.sql.format.valueobject.BlancoSqlToken;

/**
 * BlancoSqlFormatter: SQL整形ツール. SQL文を決められたルールに従い整形します。
 * 
 * フォーマットを実施するためには、入力されるSQLがSQL文として妥当であることが前提条件となります。
 * 
 * このクラスが準拠するSQL整形のルールについては、下記URLを参照ください。
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * 2005.08.08 Tosiki Iga: ( ) については (*) のようにひっつけるように変更。 <br>
 * 2005.08.03 Tosiki Iga: 決められた例外をスローするように変更しました。
 * 
 * @author Yoshinori WATANABE (a-san) : original version at 2005.07.04.
 * @author Tosiki Iga : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlFormatter {
    private final BlancoSqlParser fParser = new BlancoSqlParser();

    private BlancoSqlRule fRule = null;

    /**
     * 丸カッコが関数のものかどうかを覚える。
     */
    private Stack<Boolean> functionBracket = new Stack<Boolean>();

    /**
     * SQL整形ツールのインスタンスを作成します。
     * 
     * @param argRule
     *            SQL変換ルール。
     */
    public BlancoSqlFormatter(final BlancoSqlRule argRule) {
        fRule = argRule;
    }

    /**
     * 与えられたSQLを整形します。
     * 
     * 1.改行で終了するSQL文は、整形後も改行付きであるようにします。
     * 
     * @param argSql
     *            整形前のSQL文
     * @return 整形後のSQL文
     * @throws BlancoSqlFormatterException
     *             内部的に発生した例外は全てこのクラスになります。
     */
    public String format(final String argSql)
            throws BlancoSqlFormatterException {
        functionBracket.clear();
        try {
            boolean isSqlEndsWithNewLine = false;
            if (argSql.endsWith("\n")) {
                isSqlEndsWithNewLine = true;
            }

            List<BlancoSqlToken> list = fParser.parse(argSql);

            list = format(list);

            // 変換結果を文字列に戻す。
            String after = "";
            for (int index = 0; index < list.size(); index++) {
                BlancoSqlToken token = list.get(index);
                after += token.getString();
            }

            if (isSqlEndsWithNewLine) {
                after += "\n";
            }

            return after;
        } catch (Exception ex) {
            final BlancoSqlFormatterException sqlException = new BlancoSqlFormatterException(
                    ex.toString());
            sqlException.initCause(ex);
            throw sqlException;
        }
    }

    /**
     * 字句の配列を、指定されたSQL書式規則に従って変換します。
     * 
     * @param argList
     *            変換前の字句の配列。ArrayList <Token>
     * @return 変換後の字句の配列。ArrayList <Token>
     */
    private List<BlancoSqlToken> format(final List<BlancoSqlToken> argList) {

        // TODO:SQL整形のカスタマイズが必要な方は、ここから、あなたの行いたい書式に変換して下さい。
        // なるべくシンプルで明確な整形ルールにしてください。
        // また、誰も使わないような不必要な選択肢はやめましょう。

        // SQLの前後に空白があると削除する。
        BlancoSqlToken token = argList.get(0);
        if (token.getType() == BlancoSqlTokenConstants.SPACE) {
            argList.remove(0);
        }

        token = argList.get(argList.size() - 1);
        if (token.getType() == BlancoSqlTokenConstants.SPACE) {
            argList.remove(argList.size() - 1);
        }

        // SQLキーワードは大文字とする。or ...
        for (int index = 0; index < argList.size(); index++) {
            token = argList.get(index);
            if (token.getType() == BlancoSqlTokenConstants.KEYWORD) {
                switch (fRule.keyword) {
                case BlancoSqlRule.KEYWORD_NONE:
                    break;
                case BlancoSqlRule.KEYWORD_UPPER_CASE:
                    token.setString(token.getString().toUpperCase());
                    break;
                case BlancoSqlRule.KEYWORD_LOWER_CASE:
                    token.setString(token.getString().toLowerCase());
                    break;
                }
            }
        }

        // いったん、記号の前後の空白を除去する
        for (int index = argList.size() - 1; index >= 1; index--) {
            token = argList.get(index);
            BlancoSqlToken prevToken = argList.get(index - 1);
            if (token.getType() == BlancoSqlTokenConstants.SPACE
                    && (prevToken.getType() == BlancoSqlTokenConstants.SYMBOL || prevToken
                            .getType() == BlancoSqlTokenConstants.COMMENT)) {
                argList.remove(index);
            } else if ((token.getType() == BlancoSqlTokenConstants.SYMBOL || token
                    .getType() == BlancoSqlTokenConstants.COMMENT)
                    && prevToken.getType() == BlancoSqlTokenConstants.SPACE) {
                argList.remove(index - 1);
            } else if (token.getType() == BlancoSqlTokenConstants.SPACE) {
                token.setString(" ");
            }
        }

        // ２つ並んだキーワードは１つのキーワードとみなす。(ex."INSERT INTO", "ORDER BY")
        // 近代の言語はキーワードが２つ並ぶことはない。古い言語では、自然言語（つまり人間の言語）に
        // 近づけるため、キーワードが２つ並ぶことがあった。しかし、近代では"ORDER_BY"、あるいは"OrderBy"
        // のように、可読性を損なうことなく、解析しやすい文法を採用している。
        for (int index = 0; index < argList.size() - 2; index++) {
            BlancoSqlToken t0 = argList.get(index);
            BlancoSqlToken t1 = argList.get(index + 1);
            BlancoSqlToken t2 = argList.get(index + 2);

            if (t0.getType() == BlancoSqlTokenConstants.KEYWORD
                    && t1.getType() == BlancoSqlTokenConstants.SPACE
                    && t2.getType() == BlancoSqlTokenConstants.KEYWORD) {
                if (((t0.getString().equalsIgnoreCase("ORDER") || t0
                        .getString().equalsIgnoreCase("GROUP")) && t2
                        .getString().equalsIgnoreCase("BY"))) {
                    t0.setString(t0.getString() + " " + t2.getString());
                    argList.remove(index + 1);
                    argList.remove(index + 1);
                }
            }

            // Oracle対応 begin 2007/10/24 A.Watanabe
            // Oracleの外部結合演算子"(+)"を１つの演算子とする。
            if (t0.getString().equals("(") && t1.getString().equals("+")
                    && t2.getString().equals(")")) {
                t0.setString("(+)");
                argList.remove(index + 1);
                argList.remove(index + 1);
            }
            // Oracle対応 end
        }

        // インデントを整える。
        int indent = 0;
        // 丸カッコのインデント位置を覚える。
        final Stack<Integer> bracketIndent = new Stack<Integer>();
        BlancoSqlToken prev = new BlancoSqlToken(BlancoSqlTokenConstants.SPACE,
                " ");
        boolean encounterBetween = false;
        for (int index = 0; index < argList.size(); index++) {
            token = argList.get(index);
            if (token.getType() == BlancoSqlTokenConstants.SYMBOL) {
                // indentを１つ増やし、'('のあとで改行。
                if (token.getString().equals("(")) {
                    functionBracket
                            .push(fRule.isFunction(prev.getString()) ? Boolean.TRUE
                                    : Boolean.FALSE);
                    bracketIndent.push(new Integer(indent));
                    indent++;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                // indentを１つ増やし、')'の前と後ろで改行。
                else if (token.getString().equals(")")) {
                    indent = bracketIndent.pop().intValue();
                    index += insertReturnAndIndent(argList, index, indent);
                    functionBracket.pop();
                }
                // ','の前で改行
                else if (token.getString().equals(",")) {
                    index += insertReturnAndIndent(argList, index, indent);
                } else if (token.getString().equals(";")) {
                    // 2005.07.26 Tosiki Iga とりあえずセミコロンでSQL文がつぶれないように改良
                   // indent = 0; //akardapolov comm
                    index += insertReturnAndIndent(argList, index+1, indent); 
                }
            } else if (token.getType() == BlancoSqlTokenConstants.KEYWORD) {
                // indentを２つ増やし、キーワードの後ろで改行
                if (token.getString().equalsIgnoreCase("DELETE")
                        || token.getString().equalsIgnoreCase("SELECT")
                        || token.getString().equalsIgnoreCase("UPDATE")
                        // akardapolov
                        || token.getString().equalsIgnoreCase("DECLARE")
                        || token.getString().equalsIgnoreCase("BEGIN")) {
                    indent += 2;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                // indentを１つ増やし、キーワードの後ろで改行
                if (token.getString().equalsIgnoreCase("INSERT")
                        || token.getString().equalsIgnoreCase("INTO")
                        || token.getString().equalsIgnoreCase("CREATE")
                        || token.getString().equalsIgnoreCase("DROP")
                        || token.getString().equalsIgnoreCase("TRUNCATE")
                        || token.getString().equalsIgnoreCase("TABLE")
                        || token.getString().equalsIgnoreCase("CASE")) {
                    indent++;
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                // キーワードの前でindentを１つ減らして改行、キーワードの後ろでindentを戻して改行。
                if (token.getString().equalsIgnoreCase("FROM")
                        || token.getString().equalsIgnoreCase("WHERE")
                        || token.getString().equalsIgnoreCase("SET")
                        || token.getString().equalsIgnoreCase("ORDER BY")
                        || token.getString().equalsIgnoreCase("GROUP BY")
                        || token.getString().equalsIgnoreCase("HAVING")
                         // akardapolov
                        || token.getString().equalsIgnoreCase("BEGIN")
                        || token.getString().equalsIgnoreCase("END")) {
                    index += insertReturnAndIndent(argList, index, indent - 1);
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                // キーワードの前でindentを１つ減らして改行、キーワードの後ろでindentを戻して改行。
                if (token.getString().equalsIgnoreCase("VALUES")) {
                    indent--;
                    index += insertReturnAndIndent(argList, index, indent);
                }
                // キーワードの前でindentを１つ減らして改行
                if (token.getString().equalsIgnoreCase("END")) {
                    indent--;
                    index += insertReturnAndIndent(argList, index, indent);
                }
                // キーワードの前で改行
                if (token.getString().equalsIgnoreCase("OR")
                        || token.getString().equalsIgnoreCase("THEN")
                        || token.getString().equalsIgnoreCase("ELSE")) {
                    index += insertReturnAndIndent(argList, index, indent);
                }
                // キーワードの前で改行
                if (token.getString().equalsIgnoreCase("ON")
                        || token.getString().equalsIgnoreCase("USING")) {
                    index += insertReturnAndIndent(argList, index, indent + 1);
                }
                // キーワードの前で改行。indentを強制的に０にする。
                if (token.getString().equalsIgnoreCase("UNION")
                        || token.getString().equalsIgnoreCase("INTERSECT")
                        || token.getString().equalsIgnoreCase("EXCEPT")) {
                    indent -= 2;
                    index += insertReturnAndIndent(argList, index, indent);
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
                if (token.getString().equalsIgnoreCase("BETWEEN")) {
                    encounterBetween = true;
                }
                if (token.getString().equalsIgnoreCase("AND")) {
                    // BETWEEN のあとのANDは改行しない。
                    if (!encounterBetween) {
                        index += insertReturnAndIndent(argList, index, indent);
                    }
                    encounterBetween = false;
                }
            } else if (token.getType() == BlancoSqlTokenConstants.COMMENT) {
                if (token.getString().startsWith("/*")) {
                    // マルチラインコメントの後に改行を入れる。
                    index += insertReturnAndIndent(argList, index + 1, indent);
                }
            }
            prev = token;
        }

        // 丸カッコで囲まれた (ひとつの項目)については特別扱いを行う。 @author tosiki iga
        for (int index = argList.size() - 1; index >= 4; index--) {
            if (index >= argList.size()) {
                continue;
            }

            BlancoSqlToken t0 = argList.get(index);
            BlancoSqlToken t1 = argList.get(index - 1);
            BlancoSqlToken t2 = argList.get(index - 2);
            BlancoSqlToken t3 = argList.get(index - 3);
            BlancoSqlToken t4 = argList.get(index - 4);

            if (t4.getString().equalsIgnoreCase("(")
                    && t3.getString().trim().equalsIgnoreCase("")
                    && t1.getString().trim().equalsIgnoreCase("")
                    && t0.getString().equalsIgnoreCase(")")) {
                t4.setString(t4.getString() + t2.getString() + t0.getString());
                argList.remove(index);
                argList.remove(index - 1);
                argList.remove(index - 2);
                argList.remove(index - 3);
            }
        }

        // 前後にスペースを入れる
        for (int index = 1; index < argList.size(); index++) {
            prev = argList.get(index - 1);
            token = argList.get(index);

            if (prev.getType() != BlancoSqlTokenConstants.SPACE
                    && token.getType() != BlancoSqlTokenConstants.SPACE) {
                // カンマの後にはスペース入れない
                if (prev.getString().equals(",")) {
                    continue;
                }
                // 関数名の後ろにはスペースは入れない
                if (fRule.isFunction(prev.getString())
                        && token.getString().equals("(")) {
                    continue;
                }
                argList.add(index, new BlancoSqlToken(
                        BlancoSqlTokenConstants.SPACE, " "));
            }
        }

        return argList;
    }

    /**
     * 改行とインデントを挿入する.
     * 
     * @param argList
     * @param argIndex
     * @param argIndent
     * @return 空白を挿入した場合は１を、空白を置き換えた場合は０を返す。
     */
    private int insertReturnAndIndent(final List<BlancoSqlToken> argList,
            final int argIndex, final int argIndent) {
        // 関数内では改行は挿入しない
        if (functionBracket.contains(Boolean.TRUE))
            return 0;
        try {
            // 挿入する文字列を作成する。
            String s = "\n";
            // もし１つ前にシングルラインコメントがあるなら、改行は不要。
            final BlancoSqlToken prevToken = argList.get(argIndex - 1);
            if (prevToken.getType() == BlancoSqlTokenConstants.COMMENT
                    && prevToken.getString().startsWith("--")) {
                s = "";
            }
            // インデントをつける。
            for (int index = 0; index < argIndent; index++) {
                s += fRule.indentString;
            }

            // 前後にすでにスペースがあれば、それを置き換える。
            BlancoSqlToken token = argList.get(argIndex);
            if (token.getType() == BlancoSqlTokenConstants.SPACE) {
                token.setString(s);
                return 0;
            }

            token = argList.get(argIndex - 1);
            if (token.getType() == BlancoSqlTokenConstants.SPACE) {
                token.setString(s);
                return 0;
            }
            // 前後になければ、新たにスペースを追加する。
            argList.add(argIndex, new BlancoSqlToken(
                    BlancoSqlTokenConstants.SPACE, s));
            return 1;
        } catch (IndexOutOfBoundsException e) {
            // e.printStackTrace();
            return 0;
        }
    }

    public static void main(final String[] args) throws Exception {
        // ルールを設定する
        final BlancoSqlRule rule = new BlancoSqlRule();
        rule.keyword = BlancoSqlRule.KEYWORD_UPPER_CASE;
        rule.indentString = "    ";
        final String[] mySqlFuncs = {
                // getNumericFunctions
                "ABS", "ACOS", "ASIN", "ATAN", "ATAN2", "BIT_COUNT", "CEILING",
                "COS", "COT", "DEGREES", "EXP",
                "FLOOR",
                "LOG",
                "LOG10",
                "MAX",
                "MIN",
                "MOD",
                "PI",
                "POW",
                "POWER",
                "RADIANS",
                "RAND",
                "ROUND",
                "SIN",
                "SQRT",
                "TAN",
                "TRUNCATE",
                // getStringFunctions
                "ASCII", "BIN", "BIT_LENGTH", "CHAR", "CHARACTER_LENGTH",
                "CHAR_LENGTH", "CONCAT", "CONCAT_WS", "CONV", "ELT",
                "EXPORT_SET", "FIELD", "FIND_IN_SET", "HEX,INSERT", "INSTR",
                "LCASE", "LEFT", "LENGTH", "LOAD_FILE", "LOCATE", "LOCATE",
                "LOWER", "LPAD", "LTRIM", "MAKE_SET", "MATCH", "MID", "OCT",
                "OCTET_LENGTH", "ORD", "POSITION", "QUOTE", "REPEAT",
                "REPLACE", "REVERSE", "RIGHT", "RPAD", "RTRIM", "SOUNDEX",
                "SPACE", "STRCMP", "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING",
                "SUBSTRING_INDEX",
                "TRIM",
                "UCASE",
                "UPPER",
                // getSystemFunctions
                "DATABASE", "USER",
                "SYSTEM_USER",
                "SESSION_USER",
                "PASSWORD",
                "ENCRYPT",
                "LAST_INSERT_ID",
                "VERSION",
                // getTimeDateFunctions
                "DAYOFWEEK", "WEEKDAY", "DAYOFMONTH", "DAYOFYEAR", "MONTH",
                "DAYNAME", "MONTHNAME", "QUARTER", "WEEK", "YEAR", "HOUR",
                "MINUTE", "SECOND", "PERIOD_ADD", "PERIOD_DIFF", "TO_DAYS",
                "FROM_DAYS", "DATE_FORMAT", "TIME_FORMAT", "CURDATE",
                "CURRENT_DATE", "CURTIME", "CURRENT_TIME", "NOW", "SYSDATE",
                "CURRENT_TIMESTAMP", "UNIX_TIMESTAMP", "FROM_UNIXTIME",
                "SEC_TO_TIME", "TIME_TO_SEC" };
        rule.setFunctionNames(mySqlFuncs);
        final BlancoSqlFormatter formatter = new BlancoSqlFormatter(rule);

        // テストディレクトリ内のファイルを一括で変換する。
        final File[] files = new File("Test").listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("-- " + files[i]);
            // ファイルのSQLを読み込む.
            final BufferedReader reader = new BufferedReader(new FileReader(
                    files[i]));
            String before = "";
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null)
                    break;
                before += line + "\n";
            }
            reader.close();

            // 整形
            System.out.println("[before]\n" + before);
            String after = formatter.format(before);
            System.out.println("[after]\n" + after);
        }
    }
}
