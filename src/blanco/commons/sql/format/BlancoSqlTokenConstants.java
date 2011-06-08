/*
 * このソースコードは blanco Frameworkにより自動生成されました。
 */
package blanco.commons.sql.format;

/**
 * blancoSqlFormatterのトークンのタイプに関する定数です。
 */
public class BlancoSqlTokenConstants {
    /**
     * 項目番号:1<br>
     * 空文字. TAB,CR等も１つの文字列として含む。
     */
    public static final int SPACE = 0;

    /**
     * 項目番号:2<br>
     * 記号. " <="のような２つで１つの記号もある。
     */
    public static final int SYMBOL = 1;

    /**
     * 項目番号:3<br>
     * キーワード. "SELECT", "ORDER"など.
     */
    public static final int KEYWORD = 2;

    /**
     * 項目番号:4<br>
     * 名前. テーブル名、列名など。ダブルクォーテーションが付く場合がある。
     */
    public static final int NAME = 3;

    /**
     * 項目番号:5<br>
     * 値. 数値（整数、実数）、文字列など。
     */
    public static final int VALUE = 4;

    /**
     * 項目番号:6<br>
     * コメント. シングルラインコメントとマルチラインコメントがある。
     */
    public static final int COMMENT = 5;

    /**
     * 項目番号:7<br>
     * SQL文の終わり.
     */
    public static final int END = 6;

    /**
     * 項目番号:8<br>
     * 解析不可能なトークン. 通常のSQLではありえない。
     */
    public static final int UNKNOWN = 7;
}
