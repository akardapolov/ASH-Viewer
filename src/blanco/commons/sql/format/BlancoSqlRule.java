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
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlRule {
   
    int keyword = KEYWORD_UPPER_CASE;

    
    public static final int KEYWORD_NONE = 0;

    
    public static final int KEYWORD_UPPER_CASE = 1;

    
    public static final int KEYWORD_LOWER_CASE = 2;


    String indentString = "    ";

 
    private String[] fFunctionNames = null;

    public void setKeywordCase(int keyword) {
        this.keyword = keyword;
    }

    boolean isFunction(String name) {
        if (fFunctionNames == null)
            return false;
        for (int i = 0; i < fFunctionNames.length; i++) {
            if (fFunctionNames[i].equalsIgnoreCase(name))
                return true;
        }
        return false;
    }


    public void setFunctionNames(String[] names) {
        fFunctionNames = names;
    }

}
