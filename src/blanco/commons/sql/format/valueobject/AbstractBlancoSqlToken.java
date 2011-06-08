/*
 * このソースコードは blanco Frameworkにより自動生成されました。
 */
package blanco.commons.sql.format.valueobject;

/**
 * blancoSqlFormatterで利用されるトークンをあらわすバリューオブジェクト。
 */
public class AbstractBlancoSqlToken {
    /**
     * フィールド [type]
     *
     * 項目の型 [int]<br>
     * トークン種別をあらわします。
     */
    private int fType;

    /**
     * フィールド [string]
     *
     * 項目の型 [java.lang.String]<br>
     * トークンの実際の文字列をあらわします。
     */
    private String fString;

    /**
     * フィールド [pos]
     *
     * 項目の型 [int]<br>
     * 規定値   [-1]<br>
     * 予約語、コメント、値などのトークンの位置をあらわすフィールド。ソース文字列の先頭からの位置をあらわします。値は ゼロ(ZERO)オリジンです。デフォルト値は (-1)で、(-1) の場合には「位置情報に意味がない」ことをあらわします。
     */
    private int fPos = -1;

    /**
     * フィールド [type]のセッターメソッド
     *
     * 項目の型 [int]<br>
     * トークン種別をあらわします。
     *
     * @param argType フィールド[type]に格納したい値
     */
    public void setType(final int argType) {
        fType = argType;
    }

    /**
     * フィールド[type]のゲッターメソッド
     *
     * 項目の型 [int]<br>
     * トークン種別をあらわします。
     *
     * @return フィールド[type]に格納されている値
     */
    public int getType() {
        return fType;
    }

    /**
     * フィールド [string]のセッターメソッド
     *
     * 項目の型 [java.lang.String]<br>
     * トークンの実際の文字列をあらわします。
     *
     * @param argString フィールド[string]に格納したい値
     */
    public void setString(final String argString) {
        fString = argString;
    }

    /**
     * フィールド[string]のゲッターメソッド
     *
     * 項目の型 [java.lang.String]<br>
     * トークンの実際の文字列をあらわします。
     *
     * @return フィールド[string]に格納されている値
     */
    public String getString() {
        return fString;
    }

    /**
     * フィールド [pos]のセッターメソッド
     *
     * 項目の型 [int]<br>
     * 予約語、コメント、値などのトークンの位置をあらわすフィールド。ソース文字列の先頭からの位置をあらわします。値は ゼロ(ZERO)オリジンです。デフォルト値は (-1)で、(-1) の場合には「位置情報に意味がない」ことをあらわします。
     *
     * @param argPos フィールド[pos]に格納したい値
     */
    public void setPos(final int argPos) {
        fPos = argPos;
    }

    /**
     * フィールド[pos]のゲッターメソッド
     *
     * 項目の型 [int]<br>
     * 規定値   [-1]<br>
     * 予約語、コメント、値などのトークンの位置をあらわすフィールド。ソース文字列の先頭からの位置をあらわします。値は ゼロ(ZERO)オリジンです。デフォルト値は (-1)で、(-1) の場合には「位置情報に意味がない」ことをあらわします。
     *
     * @return フィールド[pos]に格納されている値
     */
    public int getPos() {
        return fPos;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。
     *
     * オブジェクトのシャロー範囲でしかtoStringされない点に注意して利用してください。
     *
     * @return バリューオブジェクトの文字列表現。
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.commons.sql.format.valueobject.AbstractBlancoSqlToken[");
        buf.append("type=" + fType);
        buf.append(",string=" + fString);
        buf.append(",pos=" + fPos);
        buf.append("]");
        return buf.toString();
    }
}
