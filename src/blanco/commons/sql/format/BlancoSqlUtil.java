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

/**
 * BlancoSqlFormatter: SQL整形ツール. SQL文を決められたルールに従い整形します。
 * 
 * フォーマットを実施するためには、入力されるSQLがSQL文として妥当であることが前提条件となります。
 * 
 * このクラスが準拠するSQL整形のルールについては、下記URLを参照ください。
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * このクラスは各種ユーティリティをたくわえるクラスです.
 * 
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlUtil {
/**
     * 文字列を置き換える. 同一文字列内に複数の変換対象があってもよい。
     * 
     * @param argTargetString
     *            処理対象となる文字列。
     * @param argFrom
     *            変換前の文字列。(ex." <")
     * @param argTo
     *            変換後の文字列。(ex."&lt;")
     * @return 置換された後の文字列。
     */
    public static String replace(final String argTargetString,
            final String argFrom, final String argTo) {
        String newStr = "";
        int lastpos = 0;

        for (;;) {
            final int pos = argTargetString.indexOf(argFrom, lastpos);
            if (pos == -1) {
                break;
            }

            newStr += argTargetString.substring(lastpos, pos);
            newStr += argTo;
            lastpos = pos + argFrom.length();
        }

        return newStr + argTargetString.substring(lastpos);
    }
}
