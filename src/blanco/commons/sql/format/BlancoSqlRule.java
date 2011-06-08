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
 * このクラスは SQLの変換規則を表します。
 * 
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlRule {
    /** キーワードの変換規則. */
    int keyword = KEYWORD_UPPER_CASE;

    /** キーワードの変換規則:何もしない */
    public static final int KEYWORD_NONE = 0;

    /** キーワードの変換規則:大文字にする */
    public static final int KEYWORD_UPPER_CASE = 1;

    /** キーワードの変換規則:小文字にする */
    public static final int KEYWORD_LOWER_CASE = 2;

    /**
     * インデントの文字列. 設定は自由入力とする。通常は " ", " ", "\t" のいずれか。
     */
    String indentString = "    ";

    /**
     * 関数の名前。
     */
    private String[] fFunctionNames = null;

    public void setKeywordCase(int keyword) {
        this.keyword = keyword;
    }

    /**
     * 関数の名前か？
     * 
     * @param name
     *            調べる名前
     * @return 関数の名前のとき、<code>true</code> を返す。
     */
    boolean isFunction(String name) {
        if (fFunctionNames == null)
            return false;
        for (int i = 0; i < fFunctionNames.length; i++) {
            if (fFunctionNames[i].equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    /**
     * 関数の名前の配列を登録します。
     * 
     * @param names
     *            関数名の配列。null可。
     */
    public void setFunctionNames(String[] names) {
        fFunctionNames = names;
    }

    // TODO カスタマイズ領域。カスタマイズが必要な場合には、以下に規則を追加してください。
}
