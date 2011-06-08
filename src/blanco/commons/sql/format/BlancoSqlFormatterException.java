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
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html
 * 
 * BlancoSqlFormatterException :
 * 
 * @author IGA Tosiki : at 2005.08.03
 */
@SuppressWarnings("serial")
public class BlancoSqlFormatterException extends IOException {

    public BlancoSqlFormatterException() {
        super();
    }

    public BlancoSqlFormatterException(final String argMessage) {
        super(argMessage);
    }
}
