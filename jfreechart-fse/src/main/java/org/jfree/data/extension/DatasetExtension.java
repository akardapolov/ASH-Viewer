/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------------
 * DatasetExtension.java
 * ---------------------
 * (C) Copyright 2013, by Michael Zinsmaier.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 17-Sep-2013 : Version 1 (MZ);
 *
 */

package org.jfree.data.extension;

import java.io.Serializable;

import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.general.Dataset;

/**
 * Base interface for dataset extensions. A dataset extension typically stores
 * additional information for data items (for example 
 * {@link DatasetSelectionExtension}). Dataset extensions can be implemented in 
 * separate helper objects or as part of a datasets. This allows to extend 
 * older dataset implementations.<br>
 * <br> 
 * The {@link DatasetExtensionManager} class can be used to pair datasets and 
 * external helper objects and allows to treat paired datasets the same way as 
 * datasets that directly implement
 * a datasetextension. 
 * 
 * @author zinsmaie
 */

public interface DatasetExtension extends Serializable {

    /**
     * @return the dataset that is extended
     */
    public Dataset getDataset();
    
}
