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

import java.io.IOException;

/**
 * BlancoSqlFormatter: SQL整形ツール. SQL文を決められたルールに従い整形します。
 * 
 * フォーマットを実施するためには、入力されるSQLがSQL文として妥当であることが前提条件となります。
 * 
 * このクラスが準拠するSQL整形のルールについては、下記URLを参照ください。
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * BlancoSqlFormatterException : SQL整形ツールの例外を表します。
 * 
 * @author IGA Tosiki : 新規作成 at 2005.08.03
 */
@SuppressWarnings("serial")
public class BlancoSqlFormatterException extends IOException {

    /**
     * 例外のコンストラクタ
     */
    public BlancoSqlFormatterException() {
        super();
    }

    /**
     * 例外のコンストラクタ
     * 
     * @param argMessage
     */
    public BlancoSqlFormatterException(final String argMessage) {
        super(argMessage);
    }
}
